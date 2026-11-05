package com.simats.nritahub.ui.feemanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.nritahub.data.remote.dto.CreateFeeRequest
import com.simats.nritahub.data.remote.dto.FeeRecordDto
import com.simats.nritahub.data.repository.FeeRepository
import com.simats.nritahub.ui.admin.AdminTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeeManagementViewModel(
    private val feeRepository: FeeRepository = FeeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeeManagementUiState())
    val uiState: StateFlow<FeeManagementUiState> = _uiState.asStateFlow()

    init {
        fetchFees()
    }

    fun fetchFees() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = feeRepository.listFees()
            result.onSuccess { response ->
                val studentRecords = response.studentFees.map { it.toStudentFeeRecord() }
                val staffRecords = response.staffSalaries.map { it.toStaffSalaryRecord() }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        studentFeeRecords = studentRecords,
                        staffSalaryRecords = staffRecords
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to load fee records."
                    )
                }
            }
        }
    }

    fun onFeeViewModeSelected(mode: FeeViewMode) {
        _uiState.update { it.copy(activeViewMode = mode) }
    }

    fun toggleAddFeeStructureForm() {
        _uiState.update { it.copy(showAddFeeStructureForm = !it.showAddFeeStructureForm, errorMessage = null) }
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
        val currentState = _uiState.value
        val studentName = currentState.newFeeStudentNameInput.trim()
        val studentIdentifier = currentState.newFeeStudentIdInput.trim()
        val programName = currentState.newFeeProgramNameInput.trim().ifBlank { "Academy Tuition Fee" }
        val rawAmount = currentState.newFeeAmountInput.trim().ifBlank { "₹2,400" }
        val dueDate = currentState.newFeeDueDateInput.trim().ifBlank { "Due in 15 days" }

        if (studentName.isBlank() && studentIdentifier.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Student name or identifier is required.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val request = CreateFeeRequest(
                recordType = "student_fee",
                userName = studentName.ifBlank { null },
                userIdentifier = studentIdentifier.ifBlank { null },
                title = programName,
                amount = rawAmount,
                dueDate = dueDate,
                status = "Pending"
            )

            val result = feeRepository.createFee(request)
            result.onSuccess { createdFee ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        showAddFeeStructureForm = false,
                        createdFeeSuccessName = createdFee.userName ?: studentName,
                        successMessage = "Fee invoice successfully created for ${createdFee.userName ?: studentName}."
                    )
                }
                fetchFees()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = error.localizedMessage ?: "Failed to create fee invoice."
                    )
                }
            }
        }
    }

    fun toggleStaffPayoutForm() {
        _uiState.update { it.copy(showStaffPayoutForm = !it.showStaffPayoutForm, errorMessage = null) }
    }

    fun onPayoutStaffNameChanged(name: String) {
        _uiState.update { it.copy(payoutStaffNameInput = name) }
    }

    fun onPayoutStaffRoleChanged(role: String) {
        _uiState.update { it.copy(payoutStaffRoleInput = role) }
    }

    fun onPayoutStaffSalaryChanged(salary: String) {
        _uiState.update { it.copy(payoutStaffSalaryInput = salary) }
    }

    fun onPayoutStaffUsernameChanged(username: String) {
        _uiState.update { it.copy(payoutStaffUsernameInput = username) }
    }

    fun onConfirmStaffPayoutSubmit() {
        val currentState = _uiState.value
        val staffName = currentState.payoutStaffNameInput.trim().ifBlank { "Staff Member" }
        val matchedRecord = currentState.staffSalaryRecords.find { it.staffName.contains(staffName, ignoreCase = true) }

        if (matchedRecord?.numericId != null) {
            viewModelScope.launch {
                _uiState.update { it.copy(isSubmitting = true) }
                val result = feeRepository.updateFeeStatus(matchedRecord.numericId, "Paid")
                result.onSuccess {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            showStaffPayoutForm = false,
                            processedPayoutSuccessName = staffName,
                            successMessage = "Payout of ${matchedRecord.calculatedSalary} successfully processed for $staffName."
                        )
                    }
                    fetchFees()
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = error.localizedMessage ?: "Failed to update payout status."
                        )
                    }
                }
            }
        } else {
            // Local fallback feedback
            _uiState.update {
                it.copy(
                    showStaffPayoutForm = false,
                    processedPayoutSuccessName = staffName
                )
            }
        }
    }

    fun togglePaymentStatus(studentId: String) {
        val currentState = _uiState.value
        val record = currentState.studentFeeRecords.find { it.studentId == studentId }
        val recordId = record?.numericId ?: studentId.toIntOrNull()

        if (recordId == null) return

        val newStatus = if (record?.isPaid == true) "Pending" else "Paid"

        viewModelScope.launch {
            val result = feeRepository.updateFeeStatus(recordId, newStatus)
            result.onSuccess {
                fetchFees()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = error.localizedMessage ?: "Failed to update payment status.")
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
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

    private fun FeeRecordDto.toStudentFeeRecord(): StudentFeeRecord {
        val paid = isPaid || status.equals("paid", ignoreCase = true)
        val subtitle = if (!batchName.isNullOrBlank()) "$title · $batchName" else title
        return StudentFeeRecord(
            studentId = id.toString(),
            studentName = userName ?: "Student #$userId",
            batchName = subtitle,
            feeAmount = amount,
            isPaid = paid,
            numericId = id,
            status = status,
            dueDate = dueDate
        )
    }

    private fun FeeRecordDto.toStaffSalaryRecord(): StaffSalaryRecord {
        return StaffSalaryRecord(
            staffId = id.toString(),
            staffName = userName ?: "Instructor #$userId",
            role = staffRole ?: userRole?.replaceFirstChar { it.uppercase() } ?: "Instructor",
            activeBatches = batchName ?: title,
            calculatedSalary = amount,
            payoutStatus = status,
            numericId = id
        )
    }
}
