package com.simats.nritahub.ui.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.nritahub.data.repository.AttendanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AttendanceHistoryViewModel(
    private val attendanceRepository: AttendanceRepository = AttendanceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttendanceHistoryUiState(isLoading = true))
    val uiState: StateFlow<AttendanceHistoryUiState> = _uiState.asStateFlow()

    init {
        loadAttendance()
    }

    /**
     * Loads the authenticated student's attendance records and statistics from the backend.
     */
    fun loadAttendance() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            attendanceRepository.getStudentAttendance()
                .onSuccess { response ->
                    val mappedRecords = response.records.map { rec ->
                        ClassRecord(
                            dateAndTime = formatClassDateAndTiming(rec.attendanceDate, rec.timing),
                            instructorAndStudio = "${rec.instructor ?: "Instructor"} · ${rec.studio ?: "Studio"}",
                            status = mapBackendStatus(rec.status)
                        )
                    }

                    val summaryText = if (response.totalClasses > 0) {
                        "${response.presentClasses} of ${response.totalClasses} classes attended"
                    } else {
                        "No classes recorded yet"
                    }

                    val subtext = if (!response.batchName.isNullOrBlank()) {
                        "${response.batchName} · ${response.instructor ?: "Academy Class"}"
                    } else {
                        "Academy Class Records"
                    }

                    val titleText = if (response.totalClasses > 0) {
                        "Class records (${response.totalClasses} total)"
                    } else {
                        "No class records yet"
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            attendancePercentage = response.percentageText,
                            attendanceSummary = summaryText,
                            attendanceSubtext = subtext,
                            presentCount = response.presentClasses,
                            absentCount = response.absentClasses,
                            excusedCount = response.excusedClasses,
                            recordsTitle = titleText,
                            classRecords = mappedRecords
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Failed to load attendance history."
                        )
                    }
                }
        }
    }

    fun refresh() {
        loadAttendance()
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = AttendanceHistoryNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = AttendanceHistoryNavigationEvent.None) }
    }

    private fun mapBackendStatus(statusStr: String?): AttendanceStatus {
        return when (statusStr?.lowercase()?.trim()) {
            "absent" -> AttendanceStatus.ABSENT
            "excused" -> AttendanceStatus.EXCUSED
            else -> AttendanceStatus.PRESENT
        }
    }

    private fun formatClassDateAndTiming(dateStr: String, timingStr: String?): String {
        val datePart = try {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = parser.parse(dateStr)
            if (date != null) {
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
            } else {
                dateStr
            }
        } catch (_: Exception) {
            dateStr
        }

        return if (!timingStr.isNullOrBlank()) {
            "$datePart · $timingStr"
        } else {
            datePart
        }
    }
}
