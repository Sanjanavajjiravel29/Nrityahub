package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Statistics summary returned in BatchAttendanceResponse.
 */
data class BatchAttendanceStatsDto(
    @SerializedName("present")
    val present: Int = 0,

    @SerializedName("absent")
    val absent: Int = 0,

    @SerializedName("excused")
    val excused: Int = 0,

    @SerializedName("total")
    val total: Int = 0
)

/**
 * Individual student entry in BatchAttendanceResponse.
 */
data class BatchAttendanceStudentDto(
    @SerializedName("student_id")
    val studentId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("initials")
    val initials: String? = null,

    @SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @SerializedName("status")
    val status: String = "Present",

    @SerializedName("notes")
    val notes: String? = null,

    @SerializedName("attendance_id")
    val attendanceId: Int? = null
)

/**
 * Response returned by `attendance/get_batch_attendance.php`.
 */
data class BatchAttendanceResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("batch_id")
    val batchId: Int? = null,

    @SerializedName("batch_name")
    val batchName: String? = null,

    @SerializedName("date")
    val date: String? = null,

    @SerializedName("is_saved")
    val isSaved: Boolean = false,

    @SerializedName("stats")
    val stats: BatchAttendanceStatsDto? = null,

    @SerializedName("students")
    val students: List<BatchAttendanceStudentDto> = emptyList()
)

/**
 * Item sent in MarkAttendanceRequest.
 */
data class MarkStudentAttendanceItem(
    @SerializedName("student_id")
    val studentId: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("notes")
    val notes: String? = ""
)

/**
 * Request payload for `attendance/mark_attendance.php`.
 */
data class MarkAttendanceRequest(
    @SerializedName("batch_id")
    val batchId: Int,

    @SerializedName("attendance_date")
    val attendanceDate: String,

    @SerializedName("students")
    val students: List<MarkStudentAttendanceItem>
)

/**
 * Response returned by `attendance/mark_attendance.php`.
 */
data class MarkAttendanceResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("batch_id")
    val batchId: Int? = null,

    @SerializedName("date")
    val date: String? = null,

    @SerializedName("marked_count")
    val markedCount: Int? = null
)

/**
 * Historical record in StudentAttendanceResponse.
 */
data class StudentAttendanceRecordDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("attendance_date")
    val attendanceDate: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("notes")
    val notes: String? = null,

    @SerializedName("batch_name")
    val batchName: String? = null,

    @SerializedName("studio")
    val studio: String? = null,

    @SerializedName("timing")
    val timing: String? = null,

    @SerializedName("instructor")
    val instructor: String? = null
)

/**
 * Response returned by `attendance/get_student_attendance.php`.
 */
data class StudentAttendanceResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("student_name")
    val studentName: String? = null,

    @SerializedName("batch_name")
    val batchName: String? = null,

    @SerializedName("studio")
    val studio: String? = null,

    @SerializedName("timing")
    val timing: String? = null,

    @SerializedName("instructor")
    val instructor: String? = null,

    @SerializedName("percentage")
    val percentage: Int = 100,

    @SerializedName("percentage_text")
    val percentageText: String = "100%",

    @SerializedName("present_classes")
    val presentClasses: Int = 0,

    @SerializedName("absent_classes")
    val absentClasses: Int = 0,

    @SerializedName("excused_classes")
    val excusedClasses: Int = 0,

    @SerializedName("total_classes")
    val totalClasses: Int = 0,

    @SerializedName("records")
    val records: List<StudentAttendanceRecordDto> = emptyList()
)

