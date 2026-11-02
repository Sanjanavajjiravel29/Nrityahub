package com.simats.nritahub.ui.batches

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
    val passwordMasked: String = ""
)

data class EnrolledStudentAdminItem(
    val id: String,
    val initials: String,
    val name: String,
    val batchName: String,
    val loginStatus: String = "Login Active",
    val username: String = "",
    val passwordMasked: String = ""
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
    val selectedBatchName: String = "Bharatanatyam - Foundation F1",
    val statusBadge: String = "Active",
    val enrollmentBadge: String = "20 enrolled · 4 spaces",
    val batchNameInput: String = "Bharatanatyam Foundation F1",
    val danceStyle: String = "Bharatanatyam",
    val level: String = "Foundation",
    val instructor: String = "Meera Iyer",
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
    val danceStyleOptions: List<String> = listOf("Bharatanatyam", "Kathak", "Kuchipudi", "Odissi"),
    val levelOptions: List<String> = listOf("Foundation", "Intermediate", "Advanced"),
    val instructorOptions: List<String> = listOf("Meera Iyer", "Dr. Rukmini Devi", "Siddharth Menon", "Lakshmi Narayanan"),
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
    val studentsList: List<EnrolledStudentAdminItem> = listOf(
        EnrolledStudentAdminItem("1", "AR", "Ananya Rao", "Bharatanatyam Foundation F2", "Login Active", "ananya.rao@nritahub", "Ananya@2026"),
        EnrolledStudentAdminItem("2", "AV", "Arjun Varma", "Bharatanatyam Foundation F2", "Login Active", "arjun.varma@nritahub", "Arjun@2026"),
        EnrolledStudentAdminItem("3", "DM", "Diya Menon", "Bharatanatyam Foundation F2", "Login Active", "diya.menon@nritahub", "Diya@2026"),
        EnrolledStudentAdminItem("4", "KS", "Kavya Shah", "Bharatanatyam Foundation F2", "Login Active", "kavya.shah@nritahub", "Kavya@2026"),
        EnrolledStudentAdminItem("5", "RP", "Rohan Patel", "Bharatanatyam Foundation F2", "Login Active", "rohan.patel@nritahub", "Rohan@2026"),
        EnrolledStudentAdminItem("6", "SN", "Sana Nair", "Bharatanatyam Foundation F2", "Login Active", "sana.nair@nritahub", "Sana@2026"),
        EnrolledStudentAdminItem("7", "AK", "Aditi Kulkarni", "Bharatanatyam Foundation F1", "Login Active", "aditi.kulkarni@nritahub", "Aditi@2026"),
        EnrolledStudentAdminItem("8", "DB", "Dev Bhasin", "Bharatanatyam Foundation F1", "Login Active", "dev.bhasin@nritahub", "Dev@2026"),
        EnrolledStudentAdminItem("9", "II", "Ishaan Iyer", "Bharatanatyam Foundation F1", "Login Active", "ishaan.iyer@nritahub", "Ishaan@2026"),
        EnrolledStudentAdminItem("10", "MD", "Maya Deshmukh", "Bharatanatyam Foundation F1", "Login Active", "maya.deshmukh@nritahub", "Maya@2026")
    ),
    val staffList: List<StaffMemberItem> = listOf(
        StaffMemberItem("s1", "Meera Iyer", "Senior Bharatanatyam Instructor", "Foundation F1 & F2", "₹45,000 / mo", "Active", "meera.iyer@nritahub", "Meera#2026"),
        StaffMemberItem("s2", "Dr. Rukmini Devi", "Founder & Chief Admin", "All Academy Batches", "₹65,000 / mo", "Active", "rukmini.admin@nritahub", "Admin#2026"),
        StaffMemberItem("s3", "Siddharth Menon", "Nattuvangam & Rhythm Instructor", "Advanced Batches", "₹35,000 / mo", "Active", "siddharth.menon@nritahub", "Siddharth#2026"),
        StaffMemberItem("s4", "Lakshmi Narayanan", "Abhinaya & Theory Specialist", "Senior Batches", "₹35,000 / mo", "Active", "lakshmi.narayanan@nritahub", "Lakshmi#2026")
    ),
    val selectedTab: AdminTab = AdminTab.BATCHES,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigationEvent: ManageBatchesNavigationEvent = ManageBatchesNavigationEvent.None
)
