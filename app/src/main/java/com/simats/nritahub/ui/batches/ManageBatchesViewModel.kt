package com.simats.nritahub.ui.batches

import androidx.lifecycle.ViewModel
import com.simats.nritahub.ui.admin.AdminTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ManageBatchesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ManageBatchesUiState())
    val uiState: StateFlow<ManageBatchesUiState> = _uiState.asStateFlow()

    fun onBatchModeSelected(mode: BatchMode) {
        _uiState.update { it.copy(activeMode = mode) }
    }

    fun onSelectedBatchChanged(batchName: String) {
        _uiState.update { it.copy(selectedBatchName = batchName, batchNameInput = batchName) }
    }

    fun onDanceStyleChanged(style: String) {
        _uiState.update { it.copy(danceStyle = style) }
    }

    fun onLevelChanged(level: String) {
        _uiState.update { it.copy(level = level) }
    }

    fun onInstructorChanged(instructor: String) {
        _uiState.update { it.copy(instructor = instructor) }
    }

    fun onStudioChanged(studio: String) {
        _uiState.update { it.copy(studio = studio) }
    }

    fun onStartTimeChanged(time: String) {
        _uiState.update { it.copy(startTime = time) }
    }

    fun onEndTimeChanged(time: String) {
        _uiState.update { it.copy(endTime = time) }
    }

    fun onBatchNameChanged(name: String) {
        _uiState.update { it.copy(batchNameInput = name) }
    }

    fun onTuitionChanged(tuition: String) {
        _uiState.update { it.copy(monthlyTuition = tuition) }
    }

    fun toggleCreateStudentForm() {
        _uiState.update { it.copy(showCreateStudentForm = !it.showCreateStudentForm) }
    }

    fun onNewStudentNameChanged(name: String) {
        _uiState.update { it.copy(newStudentNameInput = name) }
    }

    fun onNewStudentBatchChanged(batch: String) {
        _uiState.update { it.copy(newStudentBatchInput = batch) }
    }

    fun onNewStudentUsernameChanged(username: String) {
        _uiState.update { it.copy(newStudentUsernameInput = username) }
    }

    fun onNewStudentPasswordChanged(password: String) {
        _uiState.update { it.copy(newStudentPasswordInput = password) }
    }

    fun toggleExpandStudent(id: String) {
        _uiState.update { currentState ->
            val nextId = if (currentState.expandedStudentId == id) null else id
            currentState.copy(expandedStudentId = nextId)
        }
    }

    fun toggleExpandStaff(id: String) {
        _uiState.update { currentState ->
            val nextId = if (currentState.expandedStaffId == id) null else id
            currentState.copy(expandedStaffId = nextId)
        }
    }

    fun onCreateStudentSubmit() {
        _uiState.update { currentState ->
            val name = currentState.newStudentNameInput.ifBlank { "New Student" }
            val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString("").take(2).uppercase().ifBlank { "NS" }
            val newItem = EnrolledStudentAdminItem(
                id = "${System.currentTimeMillis()}",
                initials = initials,
                name = name,
                batchName = currentState.newStudentBatchInput,
                loginStatus = "Login Active",
                username = currentState.newStudentUsernameInput.ifBlank { "${name.lowercase().replace(" ", ".")}@nritahub" },
                passwordMasked = currentState.newStudentPasswordInput.ifBlank { "Nrita2026#" }
            )
            currentState.copy(
                studentsList = listOf(newItem) + currentState.studentsList,
                showCreateStudentForm = false,
                isProfileCreated = true,
                createdStudentSuccessName = name
            )
        }
    }

    fun toggleCreateStaffForm() {
        _uiState.update { it.copy(showCreateStaffForm = !it.showCreateStaffForm) }
    }

    fun onNewStaffNameChanged(name: String) {
        _uiState.update { it.copy(newStaffNameInput = name) }
    }

    fun onNewStaffRoleChanged(role: String) {
        _uiState.update { it.copy(newStaffRoleInput = role) }
    }

    fun onNewStaffBatchesChanged(batches: String) {
        _uiState.update { it.copy(newStaffBatchesInput = batches) }
    }

    fun onNewStaffSalaryChanged(salary: String) {
        _uiState.update { it.copy(newStaffSalaryInput = salary) }
    }

    fun onNewStaffUsernameChanged(username: String) {
        _uiState.update { it.copy(newStaffUsernameInput = username) }
    }

    fun onNewStaffPasswordChanged(password: String) {
        _uiState.update { it.copy(newStaffPasswordInput = password) }
    }

    fun onCreateStaffSubmit() {
        _uiState.update { currentState ->
            val name = currentState.newStaffNameInput.ifBlank { "New Staff Member" }
            val newItem = StaffMemberItem(
                id = "s_${System.currentTimeMillis()}",
                name = name,
                role = currentState.newStaffRoleInput,
                assignedBatches = currentState.newStaffBatchesInput,
                monthlySalary = currentState.newStaffSalaryInput,
                status = "Active",
                username = currentState.newStaffUsernameInput.ifBlank { "${name.lowercase().replace(" ", ".")}@nritahub" },
                passwordMasked = currentState.newStaffPasswordInput.ifBlank { "StaffPass2026#" }
            )
            currentState.copy(
                staffList = listOf(newItem) + currentState.staffList,
                showCreateStaffForm = false,
                isProfileCreated = true,
                createdStaffSuccessName = name
            )
        }
    }

    fun toggleNotifyStudents() {
        _uiState.update { it.copy(notifyStudentsChecked = !it.notifyStudentsChecked) }
    }

    fun toggleDay(day: String) {
        _uiState.update { currentState ->
            val updated = if (currentState.selectedDays.contains(day)) {
                currentState.selectedDays - day
            } else {
                currentState.selectedDays + day
            }
            currentState.copy(selectedDays = updated)
        }
    }

    fun onSaveBatchClicked() {
        _uiState.update {
            it.copy(
                isSaved = true,
                navigationEvent = ManageBatchesNavigationEvent.SaveBatchSuccess
            )
        }
    }

    fun onTabSelected(tab: AdminTab) {
        if (tab == AdminTab.OVERVIEW) {
            _uiState.update { it.copy(navigationEvent = ManageBatchesNavigationEvent.NavigateToOverview) }
        } else {
            _uiState.update { it.copy(selectedTab = tab) }
        }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = ManageBatchesNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = ManageBatchesNavigationEvent.None) }
    }
}
