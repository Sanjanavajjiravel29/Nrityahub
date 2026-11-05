package com.simats.nritahub.ui.markattendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.nritahub.data.remote.dto.BatchAttendanceStudentDto
import com.simats.nritahub.data.remote.dto.MarkStudentAttendanceItem
import com.simats.nritahub.data.repository.AttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MarkAttendanceViewModel(
    private val attendanceRepository: AttendanceRepository = AttendanceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MarkAttendanceUiState(
            selectedDate = getTodayDateString(),
            formattedDateText = getTodayFormattedDate()
        )
    )
    val uiState: StateFlow<MarkAttendanceUiState> = _uiState.asStateFlow()

    init {
        loadBatches()
    }

    /**
     * Loads batches available to the caller (all active batches for Admin, assigned batches for Instructor).
     */
    fun loadBatches(preferredBatchId: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            attendanceRepository.listBatches(status = "Active")
                .onSuccess { response ->
                    val batchList = response.batches.map { b ->
                        AttendanceBatchInfo(
                            id = b.id.toString(),
                            title = b.batchName,
                            scheduleAndStudio = "${b.schedule} · ${b.studio}"
                        )
                    }

                    if (batchList.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                batches = emptyList(),
                                currentStudents = emptyList(),
                                noticeText = "No active batches found."
                            )
                        }
                    } else {
                        val activeBatchId = preferredBatchId
                            ?: _uiState.value.selectedBatchId.ifEmpty { batchList.first().id }
                        val resolvedBatchId = if (batchList.any { it.id == activeBatchId }) {
                            activeBatchId
                        } else {
                            batchList.first().id
                        }

                        _uiState.update {
                            it.copy(
                                batches = batchList,
                                selectedBatchId = resolvedBatchId
                            )
                        }

                        loadBatchAttendance(resolvedBatchId, _uiState.value.selectedDate)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Failed to load batches."
                        )
                    }
                }
        }
    }

    /**
     * Fetches attendance status and enrolled students for the specified batch and date.
     */
    fun loadBatchAttendance(batchIdStr: String, dateStr: String) {
        val batchId = batchIdStr.toIntOrNull() ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

            attendanceRepository.getBatchAttendance(batchId, dateStr)
                .onSuccess { response ->
                    val mappedStudents = response.students.map { s ->
                        AttendanceStudentItem(
                            id = s.studentId.toString(),
                            initials = s.initials ?: calculateInitials(s.name),
                            name = s.name,
                            status = mapBackendStatus(s.status),
                            attendanceId = s.attendanceId,
                            notes = s.notes ?: ""
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSaved = response.isSaved,
                            draftBadge = if (response.isSaved) "Saved · Recorded" else "Draft · Not saved",
                            noticeText = if (response.isSaved) {
                                "Attendance previously saved on ${it.formattedDateText}. Modify and save to update."
                            } else {
                                "Review attendance selections before saving. Changes will update student records."
                            },
                            currentStudents = mappedStudents
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Failed to load batch attendance."
                        )
                    }
                }
        }
    }

    fun onBatchSelected(batchId: String) {
        if (_uiState.value.selectedBatchId == batchId) return
        _uiState.update { it.copy(selectedBatchId = batchId, isSaved = false, successMessage = null) }
        loadBatchAttendance(batchId, _uiState.value.selectedDate)
    }

    fun onDateSelected(newDate: String) {
        if (_uiState.value.selectedDate == newDate) return
        val formatted = formatDisplayDate(newDate)
        _uiState.update {
            it.copy(
                selectedDate = newDate,
                formattedDateText = formatted,
                isSaved = false,
                successMessage = null
            )
        }
        if (_uiState.value.selectedBatchId.isNotEmpty()) {
            loadBatchAttendance(_uiState.value.selectedBatchId, newDate)
        }
    }

    fun setStudentStatus(id: String, status: AttendanceStatus) {
        _uiState.update { state ->
            val updated = state.currentStudents.map { student ->
                if (student.id == id) {
                    student.copy(status = status)
                } else {
                    student
                }
            }
            state.copy(
                currentStudents = updated,
                isSaved = false,
                draftBadge = "Unsaved changes",
                noticeText = "You have unsaved changes. Tap 'Save attendance' to apply."
            )
        }
    }

    fun onSaveAttendanceClicked() {
        val batchId = _uiState.value.selectedBatchId.toIntOrNull() ?: return
        val date = _uiState.value.selectedDate
        val students = _uiState.value.currentStudents

        if (students.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "No students to mark in this batch.") }
            return
        }

        val requestItems = students.map {
            MarkStudentAttendanceItem(
                studentId = it.id.toInt(),
                status = it.status.name.lowercase().replaceFirstChar { c -> c.uppercase() },
                notes = it.notes
            )
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }

            attendanceRepository.markAttendance(batchId, date, requestItems)
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            isSaved = true,
                            draftBadge = "Saved ✓",
                            successMessage = response.message ?: "Attendance saved successfully for ${response.markedCount ?: students.size} student(s)!",
                            noticeText = "Attendance saved successfully for ${response.markedCount ?: students.size} student(s)!"
                        )
                    }
                    // Refresh attendance to sync attendance IDs
                    loadBatchAttendance(batchId.toString(), date)
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = error.localizedMessage ?: "Failed to save attendance."
                        )
                    }
                }
        }
    }

    fun refresh() {
        val currentBatch = _uiState.value.selectedBatchId
        loadBatches(preferredBatchId = currentBatch)
    }

    fun dismissMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = MarkAttendanceNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = MarkAttendanceNavigationEvent.None) }
    }

    private fun mapBackendStatus(statusStr: String?): AttendanceStatus {
        return when (statusStr?.lowercase()?.trim()) {
            "absent" -> AttendanceStatus.ABSENT
            "excused" -> AttendanceStatus.EXCUSED
            else -> AttendanceStatus.PRESENT
        }
    }

    private fun calculateInitials(name: String): String {
        val parts = name.trim().split("\\s+".toRegex())
        return when {
            parts.isEmpty() -> "S"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts[0].firstOrNull()?.uppercase() ?: ""}${parts[1].firstOrNull()?.uppercase() ?: ""}"
        }
    }

    companion object {
        fun getTodayDateString(): String {
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        }

        fun getTodayFormattedDate(): String {
            val date = Date()
            val sdf = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
            return "Today, " + sdf.format(date)
        }

        fun formatDisplayDate(dateStr: String): String {
            return try {
                val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val parsed = parser.parse(dateStr) ?: return dateStr
                val todayStr = parser.format(Date())
                val formatter = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
                if (dateStr == todayStr) {
                    "Today, " + formatter.format(parsed)
                } else {
                    formatter.format(parsed)
                }
            } catch (_: Exception) {
                dateStr
            }
        }
    }
}
