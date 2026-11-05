package com.simats.nritahub.ui.attendance

enum class AttendanceStatus { PRESENT, ABSENT, EXCUSED }

data class ClassRecord(
    val dateAndTime: String,
    val instructorAndStudio: String,
    val status: AttendanceStatus
)

sealed interface AttendanceHistoryNavigationEvent {
    data object None : AttendanceHistoryNavigationEvent
    data object NavigateBack : AttendanceHistoryNavigationEvent
}

data class AttendanceHistoryUiState(
    val headerNumber: String = "15",
    val title: String = "Showing up, together",
    val attendancePercentage: String = "100%",
    val attendanceSummary: String = "0 of 0 classes attended",
    val attendanceSubtext: String = "Enrolled Batch Attendance",
    val presentCount: Int = 0,
    val absentCount: Int = 0,
    val excusedCount: Int = 0,
    val recordsTitle: String = "Class records",
    val classRecords: List<ClassRecord> = emptyList(),
    val correctionNotice: String = "Need a correction or an excused absence? Contact the academy office with the class date.",
    val footerNote: String = "Attendance helps us support your learning. It is not a ranking.",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigationEvent: AttendanceHistoryNavigationEvent = AttendanceHistoryNavigationEvent.None
)
