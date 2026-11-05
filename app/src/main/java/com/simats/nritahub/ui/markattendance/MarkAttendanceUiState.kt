package com.simats.nritahub.ui.markattendance

enum class AttendanceStatus { PRESENT, ABSENT, EXCUSED }

data class AttendanceStudentItem(
    val id: String,
    val initials: String,
    val name: String,
    val status: AttendanceStatus,
    val attendanceId: Int? = null,
    val notes: String = ""
)

data class AttendanceBatchInfo(
    val id: String,
    val title: String,
    val scheduleAndStudio: String
)

sealed interface MarkAttendanceNavigationEvent {
    data object None : MarkAttendanceNavigationEvent
    data object NavigateBack : MarkAttendanceNavigationEvent
    data object SaveAttendanceSuccess : MarkAttendanceNavigationEvent
}

data class MarkAttendanceUiState(
    val headerNumber: String = "26",
    val title: String = "Mark attendance",
    val selectedBatchId: String = "",
    val selectedDate: String = "",
    val formattedDateText: String = "",
    val batches: List<AttendanceBatchInfo> = emptyList(),
    val draftBadge: String = "Draft · Not saved",
    val currentStudents: List<AttendanceStudentItem> = emptyList(),
    val noticeText: String = "Review attendance selections before saving. Changes will update student records.",
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val navigationEvent: MarkAttendanceNavigationEvent = MarkAttendanceNavigationEvent.None
) {
    val presentCount: Int get() = currentStudents.count { it.status == AttendanceStatus.PRESENT }
    val absentCount: Int get() = currentStudents.count { it.status == AttendanceStatus.ABSENT }
    val excusedCount: Int get() = currentStudents.count { it.status == AttendanceStatus.EXCUSED }
}
