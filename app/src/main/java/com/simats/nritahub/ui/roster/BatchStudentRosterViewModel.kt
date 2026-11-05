package com.simats.nritahub.ui.roster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.nritahub.data.remote.dto.UserDto
import com.simats.nritahub.data.repository.AdminRepository
import com.simats.nritahub.ui.instructor.InstructorTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BatchStudentRosterViewModel(
    private val adminRepository: AdminRepository = AdminRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(BatchStudentRosterUiState())
    val uiState: StateFlow<BatchStudentRosterUiState> = _uiState.asStateFlow()

    init {
        fetchInstructorBatches()
    }

    fun fetchInstructorBatches() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = adminRepository.listBatches()
            result.onSuccess { batches ->
                val batchInfos = batches.map { b ->
                    BatchInfo(
                        id = b.id.toString(),
                        title = b.batchName,
                        schedule = b.schedule ?: "${b.days} · ${b.startTime}",
                        studentCountBadge = "${b.enrolledCount ?: 0} students",
                        studioBadge = b.studio ?: "Studio 1",
                        numericId = b.id
                    )
                }

                val currentSelectedId = _uiState.value.selectedBatchId
                val selectedBatch = batchInfos.find { it.id == currentSelectedId } ?: batchInfos.firstOrNull()

                _uiState.update { current ->
                    current.copy(
                        batches = if (batchInfos.isNotEmpty()) batchInfos else current.batches,
                        selectedBatchId = selectedBatch?.id ?: current.selectedBatchId,
                        isLoading = false
                    )
                }

                selectedBatch?.numericId?.let { fetchBatchStudents(it) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to load assigned batches."
                    )
                }
            }
        }
    }

    fun fetchBatchStudents(batchId: Int) {
        viewModelScope.launch {
            val result = adminRepository.getBatch(batchId)
            result.onSuccess { details ->
                val students = details.students.map { it.toStudentRosterItem() }
                _uiState.update {
                    it.copy(
                        batchStudents = students
                    )
                }
            }
        }
    }

    fun onBatchSelected(batchId: String) {
        _uiState.update { it.copy(selectedBatchId = batchId) }
        val numId = batchId.toIntOrNull()
        if (numId != null) {
            fetchBatchStudents(numId)
        }
    }

    fun onStudentClicked(student: StudentRosterItem) {
        _uiState.update { it.copy(navigationEvent = BatchStudentRosterNavigationEvent.StudentClicked(student)) }
    }

    fun onOpenBatchAttendanceClicked() {
        _uiState.update { it.copy(navigationEvent = BatchStudentRosterNavigationEvent.OpenBatchAttendanceClicked) }
    }

    fun onTabSelected(tab: InstructorTab) {
        if (tab == InstructorTab.TODAY) {
            _uiState.update { it.copy(navigationEvent = BatchStudentRosterNavigationEvent.NavigateToToday) }
        } else {
            _uiState.update { it.copy(selectedTab = tab) }
        }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = BatchStudentRosterNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = BatchStudentRosterNavigationEvent.None) }
    }

    private fun extractInitials(fullName: String): String {
        val parts = fullName.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
        return when {
            parts.isEmpty() -> "NH"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts[0].first()}${parts[1].first()}".uppercase()
        }
    }

    private fun UserDto.toStudentRosterItem(): StudentRosterItem {
        return StudentRosterItem(
            initials = initials ?: extractInitials(fullName),
            name = fullName,
            levelAndAttendance = levelAndAttendance ?: "${batchName ?: "Foundation"} · Active",
            numericId = id,
            email = email
        )
    }
}
