package com.simats.nritahub.ui.feemanagement

import com.simats.nritahub.ui.admin.AdminTab

enum class FeeViewMode { STUDENT_FEES, STAFF_SALARY }

data class StudentFeeRecord(
    val studentId: String,
    val studentName: String,
    val batchName: String,
    val feeAmount: String,
    val isPaid: Boolean
)

data class StaffSalaryRecord(
    val staffId: String,
    val staffName: String,
    val role: String,
    val activeBatches: String,
    val calculatedSalary: String,
    val payoutStatus: String = "Calculated"
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
    val studentFeeRecords: List<StudentFeeRecord> = listOf(
        StudentFeeRecord("001", "Ananya Rao", "Bharatanatyam F2 · Autumn Recital Fee", "₹3,500", true),
        StudentFeeRecord("002", "Kavya Shah", "Bharatanatyam F2 · Autumn Recital Fee", "₹3,500", false),
        StudentFeeRecord("003", "Diya Menon", "Bharatanatyam F2 · Autumn Recital Fee", "₹3,500", true),
        StudentFeeRecord("004", "Rohan Patel", "Bharatanatyam F2 · Autumn Recital Fee", "₹3,500", false),
        StudentFeeRecord("005", "Aditi Kulkarni", "Bharatanatyam F1 · Autumn Recital Fee", "₹3,500", true)
    ),
    val staffSalaryRecords: List<StaffSalaryRecord> = listOf(
        StaffSalaryRecord("s1", "Meera Iyer", "Senior Instructor", "Foundation F1 & F2 (38 students)", "₹45,000"),
        StaffSalaryRecord("s2", "Dr. Rukmini Devi", "Chief Administrator", "All Academy Batches", "₹65,000"),
        StaffSalaryRecord("s3", "Siddharth Menon", "Nattuvangam Instructor", "Advanced Batches", "₹35,000"),
        StaffSalaryRecord("s4", "Lakshmi Narayanan", "Abhinaya Specialist", "Senior Batches", "₹35,000")
    ),
    val selectedTab: AdminTab = AdminTab.FEES,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigationEvent: FeeManagementNavigationEvent = FeeManagementNavigationEvent.None
)
