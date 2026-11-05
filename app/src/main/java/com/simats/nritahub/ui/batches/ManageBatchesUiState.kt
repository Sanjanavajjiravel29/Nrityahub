package com.simats.nritahub.ui.batches

import com.simats.nritahub.data.remote.dto.BatchDto
import com.simats.nritahub.ui.admin.AdminTab

enum class BatchMode { BATCHES, STUDENTS, STAFF }

data class StaffMemberItem(
    val id: String,
    val name: String,
    val role: String,
    val assignedBatches: String,
    val monthlySalary: String,
    val status: String = "Active",
    val username: String = "",
    val passwordMasked: String = "",
    val numericId: Int? = null,
    val email: String = ""
)

data class EnrolledStudentAdminItem(
    val id: String,
    val initials: String,
    val name: String,
    val batchName: String,
    val loginStatus: String = "Login Active",
    val username: String = "",
    val passwordMasked: String = "",
    val numericId: Int? = null,
    val email: String = "",
    val attendanceSummary: String? = null
)

sealed interface ManageBatchesNavigationEvent {
    data object None : ManageBatchesNavigationEvent
    data object NavigateToOverview : ManageBatchesNavigationEvent
    data object NavigateBack : ManageBatchesNavigationEvent
    data object SaveBatchSuccess : ManageBatchesNavigationEvent
    data object CreateProfileSuccess : ManageBatchesNavigationEvent
}

data class ManageBatchesUiState(
    val headerNumber: String = "30",
    val title: String = "Manage Academy Rosters",
    val activeMode: BatchMode = BatchMode.BATCHES,
    val selectedBatchId: Int? = null,
    val selectedBatchName: String = "Bharatanatyam - Foundation F1",
    val statusBadge: String = "Active",
    val enrollmentBadge: String = "0 enrolled · 24 spaces",
    val batchNameInput: String = "Bharatanatyam Foundation F1",
    val danceStyle: String = "Bharatanatyam",
    val level: String = "Foundation",
    val instructor: String = "Meera Iyer",
    val instructorId: Int? = null,
    val capacity: String = "24 students",
    val studio: String = "Studio 2",
    val allWeekDays: List<String> = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
    val selectedDays: List<String> = listOf("Tue", "Thu", "Sat"),
    val startTime: String = "6:00 PM",
    val endTime: String = "7:00 PM",
    val batchOptions: List<String> = listOf(
        "Bharatanatyam - Foundation F1",
        "Bharatanatyam - Foundation F2",
        "Bharatanatyam - Intermediate I1",
        "Bharatanatyam - Advanced A1"
    ),
    val batchesList: List<BatchDto> = emptyList(),
    val isCreateBatchMode: Boolean = false,
    val danceStyleOptions: List<String> = listOf("Bharatanatyam", "Kathak", "Kuchipudi", "Odissi"),
    val levelOptions: List<String> = listOf("Foundation", "Intermediate", "Advanced"),
    val instructorOptions: List<String> = listOf("Meera Iyer", "Dr. Rukmini Devi", "Siddharth Menon", "Lakshmi Narayanan", "Ananya Kumar", "Priya Sundaram", "Vidwan Krishna Prasad"),
    val studioOptions: List<String> = listOf("Studio 1", "Studio 2", "Studio 3"),
    val timeOptions: List<String> = listOf("4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM"),
    val timezoneNote: String = "India Standard Time · Repeats weekly",
    val monthlyTuition: String = "₹2,400",
    val notifyStudentsChecked: Boolean = true,
    val conflictNotice: String = "Only Admin can create new student or staff profiles.",
    val isSaved: Boolean = false,
    val isProfileCreated: Boolean = false,
    val showCreateStudentForm: Boolean = false,
    val newStudentNameInput: String = "Aarav Sharma",
    val newStudentBatchInput: String = "Bharatanatyam Foundation F2",
    val newStudentUsernameInput: String = "aarav.sharma@nritahub",
    val newStudentPasswordInput: String = "Nrita2026#",
    val createdStudentSuccessName: String? = null,
    val showCreateStaffForm: Boolean = false,
    val newStaffNameInput: String = "Priya Sundaram",
    val newStaffRoleInput: String = "Senior Abhinaya Instructor",
    val newStaffBatchesInput: String = "Foundation F1 & Intermediate I1",
    val newStaffSalaryInput: String = "₹40,000 / mo",
    val newStaffUsernameInput: String = "priya.staff@nritahub",
    val newStaffPasswordInput: String = "StaffPass2026#",
    val createdStaffSuccessName: String? = null,
    val expandedStudentId: String? = null,
    val expandedStaffId: String? = null,
    val studentsList: List<EnrolledStudentAdminItem> = emptyList(),
    val staffList: List<StaffMemberItem> = emptyList(),
    val batchRosterStudents: List<EnrolledStudentAdminItem> = emptyList(),
    val isRosterLoading: Boolean = false,
    val selectedTab: AdminTab = AdminTab.BATCHES,
    val isLoading: Boolean = false,
    val isUsersLoading: Boolean = false,
    val isBatchesLoading: Boolean = false,
    val isSavingBatch: Boolean = false,
    val isCreatingUser: Boolean = false,
    val errorMessage: String? = null,
    val userManagementErrorMessage: String? = null,
    val userManagementSuccessMessage: String? = null,
    // Password reset dialog state
    val showResetPasswordDialog: Boolean = false,
    val resetTargetUserId: Int? = null,
    val resetTargetUserName: String = "",
    val resetTargetUserRole: String = "",
    val newPasswordInput: String = "",
    val isResettingPassword: Boolean = false,
    val resetPasswordError: String? = null,
    // Assign student dialog state
    val showAssignStudentDialog: Boolean = false,
    val selectedStudentIdToAssign: Int? = null,
    val isAssigningStudent: Boolean = false,
    val assignStudentError: String? = null,
    // Remove student confirmation dialog state
    val showRemoveStudentConfirmDialog: Boolean = false,
    val studentToRemoveId: Int? = null,
    val studentToRemoveName: String = "",
    val isRemovingStudent: Boolean = false,
    val navigationEvent: ManageBatchesNavigationEvent = ManageBatchesNavigationEvent.None
)
