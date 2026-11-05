package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Individual checklist evaluation item in submission review.
 */
data class ChecklistItemDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("isChecked")
    val isChecked: Boolean = false
)

/**
 * Summary of a student's own submission on a homework assignment.
 */
data class StudentSubmissionSummaryDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("video_url")
    val videoUrl: String? = null,

    @SerializedName("submitted_at")
    val submittedAt: String? = null
)

/**
 * Homework assignment item returned by `homework/list_homework.php`.
 */
data class HomeworkItemDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("batch_id")
    val batchId: Int,

    @SerializedName("instructor_id")
    val instructorId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("topic")
    val topic: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("due_date")
    val dueDate: String? = null,

    @SerializedName("reference_media_url")
    val referenceMediaUrl: String? = null,

    @SerializedName("reference_type")
    val referenceType: String? = "video",

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("batch_name")
    val batchName: String? = null,

    @SerializedName("dance_style")
    val danceStyle: String? = null,

    @SerializedName("level")
    val level: String? = null,

    @SerializedName("instructor_name")
    val instructorName: String? = null,

    @SerializedName("instructor_role")
    val instructorRole: String? = null,

    @SerializedName("reference_media_full_url")
    val referenceMediaFullUrl: String? = null,

    @SerializedName("is_submitted")
    val isSubmitted: Boolean? = false,

    @SerializedName("my_submission")
    val mySubmission: StudentSubmissionSummaryDto? = null
)

/**
 * Response returned by `homework/list_homework.php`.
 */
data class HomeworkListResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("count")
    val count: Int? = 0,

    @SerializedName("homework")
    val homework: List<HomeworkItemDto> = emptyList()
)

/**
 * Request payload for `homework/create_homework.php`.
 */
data class CreateHomeworkRequest(
    @SerializedName("batch_id")
    val batchId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("topic")
    val topic: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("due_date")
    val dueDate: String? = null,

    @SerializedName("reference_media_url")
    val referenceMediaUrl: String? = null,

    @SerializedName("reference_type")
    val referenceType: String? = "video"
)

/**
 * Response returned by `homework/create_homework.php`.
 */
data class CreateHomeworkResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("homework")
    val homework: HomeworkItemDto? = null
)

/**
 * Student submission item returned by `homework/list_submissions.php` and `homework/get_submission.php`.
 */
data class SubmissionItemDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("homework_id")
    val homeworkId: Int? = null,

    @SerializedName("batch_id")
    val batchId: Int? = null,

    @SerializedName("student_id")
    val studentId: Int,

    @SerializedName("student_name")
    val studentName: String? = null,

    @SerializedName("student_email")
    val studentEmail: String? = null,

    @SerializedName("student_image")
    val studentImage: String? = null,

    @SerializedName("student_image_url")
    val studentImageUrl: String? = null,

    @SerializedName("student_initials")
    val studentInitials: String? = null,

    @SerializedName("batch_name")
    val batchName: String? = null,

    @SerializedName("batch_level")
    val batchLevel: String? = null,

    @SerializedName("student_and_level")
    val studentAndLevel: String? = null,

    @SerializedName("student_name_and_batch")
    val studentNameAndBatch: String? = null,

    @SerializedName("topic")
    val topic: String,

    @SerializedName("video_url")
    val videoUrl: String? = null,

    @SerializedName("video_full_url")
    val videoFullUrl: String? = null,

    @SerializedName("file_name")
    val fileName: String? = null,

    @SerializedName("file_details")
    val fileDetails: String? = null,

    @SerializedName("student_note")
    val studentNote: String? = null,

    @SerializedName("status")
    val status: String = "Pending",

    @SerializedName("submitted_at")
    val submittedAt: String,

    @SerializedName("submitted_time_formatted")
    val submittedTimeFormatted: String? = null,

    @SerializedName("submission_details_formatted")
    val submissionDetailsFormatted: String? = null,

    @SerializedName("review_id")
    val reviewId: Int? = null,

    @SerializedName("instructor_id")
    val instructorId: Int? = null,

    @SerializedName("reviewer_name")
    val reviewerName: String? = null,

    @SerializedName("feedback")
    val feedback: String? = null,

    @SerializedName("next_step")
    val nextStep: String? = null,

    @SerializedName("reviewed_at")
    val reviewedAt: String? = null,

    @SerializedName("checklist_items")
    val checklistItems: List<ChecklistItemDto>? = null,

    @SerializedName("has_review")
    val hasReview: Boolean? = false
)

/**
 * Response returned by `homework/list_submissions.php`.
 */
data class SubmissionsListResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("count")
    val count: Int? = 0,

    @SerializedName("submissions")
    val submissions: List<SubmissionItemDto> = emptyList()
)

/**
 * Response returned by `homework/get_submission.php`.
 */
data class SubmissionDetailsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("submission")
    val submission: SubmissionItemDto? = null
)

/**
 * Request payload for JSON-based homework submission.
 */
data class SubmitHomeworkJsonRequest(
    @SerializedName("homework_id")
    val homeworkId: Int? = null,

    @SerializedName("batch_id")
    val batchId: Int? = null,

    @SerializedName("student_id")
    val studentId: Int? = null,

    @SerializedName("topic")
    val topic: String,

    @SerializedName("student_note")
    val studentNote: String? = null,

    @SerializedName("file_name")
    val fileName: String? = null,

    @SerializedName("file_details")
    val fileDetails: String? = null,

    @SerializedName("video_url")
    val videoUrl: String? = null
)

/**
 * Response returned by `homework/submit_homework.php`.
 */
data class SubmitHomeworkResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("submission")
    val submission: SubmissionItemDto? = null
)

/**
 * Request payload for `homework/review_submission.php`.
 */
data class ReviewSubmissionRequest(
    @SerializedName("submission_id")
    val submissionId: Int,

    @SerializedName("feedback")
    val feedback: String,

    @SerializedName("next_step")
    val nextStep: String,

    @SerializedName("checklist")
    val checklist: List<ChecklistItemDto>? = null,

    @SerializedName("status")
    val status: String = "Published"
)

/**
 * Response returned by `homework/review_submission.php`.
 */
data class ReviewSubmissionResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("review")
    val review: Map<String, Any>? = null
)

