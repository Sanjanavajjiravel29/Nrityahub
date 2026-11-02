package com.simats.nritahub.ui.feemanagement

import androidx.lifecycle.ViewModel
import com.simats.nritahub.ui.admin.AdminTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FeeManagementViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeeManagementUiState())
    val uiState: StateFlow<FeeManagementUiState> = _uiState.asStateFlow()

    fun onFeeViewModeSelected(mode: FeeViewMode) {
        _uiState.update { it.copy(activeViewMode = mode) }
    }

    fun toggleAddFeeStructureForm() {
        _uiState.update { it.copy(showAddFeeStructureForm = !it.showAddFeeStructureForm) }
    }

    fun onNewFeeProgramNameChanged(name: String) {
        _uiState.update { it.copy(newFeeProgramNameInput = name) }
    }

    fun onNewFeeAmountChanged(amount: String) {
        _uiState.update { it.copy(newFeeAmountInput = amount) }
    }

    fun onNewFeeStudentNameChanged(name: String) {
        _uiState.update { it.copy(newFeeStudentNameInput = name) }
    }

    fun onNewFeeStudentIdChanged(studentId: String) {
        _uiState.update { it.copy(newFeeStudentIdInput = studentId) }
    }

    fun onNewFeeDueDateChanged(dueDate: String) {
        _uiState.update { it.copy(newFeeDueDateInput = dueDate) }
    }

    fun onAddFeeStructureSubmit() {
        _uiState.update { currentState ->
            val studentName = currentState.newFeeStudentNameInput.ifBlank { "Student" }
            val programName = currentState.newFeeProgramNameInput.ifBlank { "Program Fee" }
            val newRecord = StudentFeeRecord(
                studentId = "${System.currentTimeMillis()}",
                studentName = studentName,
                batchName = "$programName · ID: ${currentState.newFeeStudentIdInput}",
                feeAmount = currentState.newFeeAmountInput.ifBlank { "₹3,500" },
                isPaid = false
            )
            currentState.copy(
                studentFeeRecords = listOf(newRecord) + currentState.studentFeeRecords,
                showAddFeeStructureForm = false,
                createdFeeSuccessName = studentName
            )
        }
    }

    fun togglePaymentStatus(studentId: String) {
        _uiState.update { currentState ->
            val updated = currentState.studentFeeRecords.map { record ->
                if (record.studentId == studentId) {
                    record.copy(isPaid = !record.isPaid)
                } else {
                    record
                }
            }
            currentState.copy(studentFeeRecords = updated)
        }
    }

    fun onTabSelected(tab: AdminTab) {
        if (tab == AdminTab.OVERVIEW) {
            _uiState.update { it.copy(navigationEvent = FeeManagementNavigationEvent.NavigateToOverview) }
        } else {
            _uiState.update { it.copy(selectedTab = tab) }
        }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = FeeManagementNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = FeeManagementNavigationEvent.None) }
    }
}
