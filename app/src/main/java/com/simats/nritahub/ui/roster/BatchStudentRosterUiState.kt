package com.simats.nritahub.ui.roster

import com.simats.nritahub.ui.instructor.InstructorTab

data class BatchInfo(
    val id: String,
    val title: String,
    val schedule: String,
    val studentCountBadge: String,
    val studioBadge: String,
    val numericId: Int? = null
)

data class StudentRosterItem(
    val initials: String,
    val name: String,
    val levelAndAttendance: String,
    val numericId: Int? = null,
    val email: String? = null
)

sealed interface BatchStudentRosterNavigationEvent {
    data object None : BatchStudentRosterNavigationEvent
    data object NavigateToToday : BatchStudentRosterNavigationEvent
    data object NavigateBack : BatchStudentRosterNavigationEvent
    data class StudentClicked(val student: StudentRosterItem) : BatchStudentRosterNavigationEvent
    data object OpenBatchAttendanceClicked : BatchStudentRosterNavigationEvent
}

data class BatchStudentRosterUiState(
    val headerNumber: String = "25",
    val title: String = "Your learners",
    val selectedBatchId: String = "f2",
    val batches: List<BatchInfo> = listOf(
        BatchInfo("f2", "Bharatanatyam Foundation F2", "Tue / Thu / Sat · 4:30 PM", "18 students", "Studio 1"),
        BatchInfo("f1", "Bharatanatyam Foundation F1", "Tue / Thu / Sat · 6:00 PM", "20 students", "Studio 2")
    ),
    val batchStudents: List<StudentRosterItem> = emptyList(),
    val batch1Students: List<StudentRosterItem> = listOf(
        StudentRosterItem("AR", "Ananya Rao", "Foundation F2 · 92% · 12/13 classes"),
        StudentRosterItem("AV", "Arjun Varma", "Foundation F2 · 77% · 10/13 classes"),
        StudentRosterItem("DM", "Diya Menon", "Foundation F2 · 85% · 11/13 classes"),
        StudentRosterItem("KS", "Kavya Shah", "Foundation F2 · 100% · 13/13 classes"),
        StudentRosterItem("RP", "Rohan Patel", "Foundation F2 · 92% · 12/13 classes"),
        StudentRosterItem("SN", "Sana Nair", "Foundation F2 · 92% · 12/13 classes"),
        StudentRosterItem("MP", "Meera Patel", "Foundation F2 · 88% · 11/13 classes"),
        StudentRosterItem("PS", "Priya Sundaram", "Foundation F2 · 95% · 12/13 classes")
    ),
    val batch2Students: List<StudentRosterItem> = listOf(
        StudentRosterItem("AK", "Aditi Kulkarni", "Foundation F1 · 95% · 12/13 classes"),
        StudentRosterItem("DB", "Dev Bhasin", "Foundation F1 · 80% · 10/13 classes"),
        StudentRosterItem("II", "Ishaan Iyer", "Foundation F1 · 90% · 12/13 classes"),
        StudentRosterItem("MD", "Maya Deshmukh", "Foundation F1 · 100% · 13/13 classes"),
        StudentRosterItem("NS", "Niharika Sen", "Foundation F1 · 85% · 11/13 classes"),
        StudentRosterItem("PJ", "Pranav Joshi", "Foundation F1 · 92% · 12/13 classes"),
        StudentRosterItem("RV", "Riya Verma", "Foundation F1 · 88% · 11/13 classes"),
        StudentRosterItem("TH", "Tanvi Hegde", "Foundation F1 · 96% · 12/13 classes")
    ),
    val guidanceNotice: String = "Select a learner to view practice recordings, attendance, and feedback.",
    val selectedTab: InstructorTab = InstructorTab.BATCHES,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigationEvent: BatchStudentRosterNavigationEvent = BatchStudentRosterNavigationEvent.None
)
