package com.simats.nritahub.ui.feemanagement

import com.simats.nritahub.ui.admin.AdminTab

enum class FeeViewMode { STUDENT_FEES, STAFF_SALARY }

data class StudentFeeRecord(
    val studentId: String,
    val studentName: String,
    val batchName: String,
    val feeAmount: String,
    val isPaid: Boolean,
    val numericId: Int? = null,
    val status: String = if (isPaid) "Paid" else "Pending",
    val dueDate: String? = null
)

data class StaffSalaryRecord(
    val staffId: String,
    val staffName: String,
    val role: String,
    val activeBatches: String,
    val calculatedSalary: String,
    val payoutStatus: String = "Calculated",
    val numericId: Int? = null
)

sealed interface FeeManagementNavigationEvent {
    data object None : FeeManagementNavigationEvent
    data object NavigateToOverview : FeeManagementNavigationEvent
    data object NavigateBack : FeeManagementNavigationEvent
}

data class FeeManagementUiState(
    val headerNumber: String = "31",
    val title: String = "Fees & Staff Salary Calculation",
    val subtitle: String = "ADMIN CONTROL PANEL",
    val activeViewMode: FeeViewMode = FeeViewMode.STUDENT_FEES,
    val showAddFeeStructureForm: Boolean = false,
    val newFeeProgramNameInput: String = "Autumn Recital & Exam Fee",
    val newFeeAmountInput: String = "₹3,500",
    val newFeeStudentNameInput: String = "Ananya Rao",
    val newFeeStudentIdInput: String = "ananya.rao@nritahub",
    val newFeeDueDateInput: String = "25 October 2026",
    val createdFeeSuccessName: String? = null,
    val showStaffPayoutForm: Boolean = false,
    val payoutStaffNameInput: String = "Meera Iyer",
    val payoutStaffRoleInput: String = "Senior Instructor",
    val payoutStaffSalaryInput: String = "₹45,000",
    val payoutStaffUsernameInput: String = "meera.iyer@nritahub",
    val processedPayoutSuccessName: String? = null,
    val studentFeeRecords: List<StudentFeeRecord> = emptyList(),
    val staffSalaryRecords: List<StaffSalaryRecord> = emptyList(),
    val selectedTab: AdminTab = AdminTab.FEES,
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val navigationEvent: FeeManagementNavigationEvent = FeeManagementNavigationEvent.None
)
