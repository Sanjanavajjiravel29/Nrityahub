package com.simats.nritahub.ui.submit

import android.net.Uri
import com.simats.nritahub.data.remote.dto.ChecklistItemDto
import com.simats.nritahub.data.remote.dto.HomeworkItemDto

sealed interface SubmitAssignmentNavigationEvent {
    data object None : SubmitAssignmentNavigationEvent
    data object NavigateBack : SubmitAssignmentNavigationEvent
    data object ChooseFileClicked : SubmitAssignmentNavigationEvent
    data object RemoveFileClicked : SubmitAssignmentNavigationEvent
    data object SubmittedToInstructor : SubmitAssignmentNavigationEvent
}

data class SubmitAssignmentUiState(
    val headerNumber: String = "13",
    val title: String = "Share your practice",
    val badgeText: String = "STEADY STEPS · DUE 9 OCT",
    val subtitle: String = "Let Meera see what you've been working on. This is a space for learning, not comparison.",
    val isFileAttached: Boolean = false,
    val attachedFileName: String = "",
    val attachedFileDetails: String = "",
    val instructorNote: String = "",
    val isConsentChecked: Boolean = true,
    val privacyNotice: String = "Only you and authorized academy staff can view your submission. It is not posted publicly.",
    val submitButtonText: String = "Submit to Instructor",
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val navigationEvent: SubmitAssignmentNavigationEvent = SubmitAssignmentNavigationEvent.None,

    // Live Homework and Submission integration
    val homeworkList: List<HomeworkItemDto> = emptyList(),
    val selectedHomeworkId: Int? = null,
    val selectedHomework: HomeworkItemDto? = null,
    val selectedFileUri: Uri? = null,
    val selectedFileBytes: ByteArray? = null,
    val selectedFileMimeType: String? = null,

    // Submission Status & Instructor Review
    val submissionStatus: String? = null, // e.g. "Pending", "Reviewed"
    val reviewerName: String? = null,
    val reviewedFeedback: String? = null,
    val reviewedNextStep: String? = null,
    val reviewedAt: String? = null,
    val reviewedChecklist: List<ChecklistItemDto> = emptyList(),
    val hasReview: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SubmitAssignmentUiState

        if (headerNumber != other.headerNumber) return false
        if (title != other.title) return false
        if (badgeText != other.badgeText) return false
        if (subtitle != other.subtitle) return false
        if (isFileAttached != other.isFileAttached) return false
        if (attachedFileName != other.attachedFileName) return false
        if (attachedFileDetails != other.attachedFileDetails) return false
        if (instructorNote != other.instructorNote) return false
        if (isConsentChecked != other.isConsentChecked) return false
        if (privacyNotice != other.privacyNotice) return false
        if (submitButtonText != other.submitButtonText) return false
        if (isLoading != other.isLoading) return false
        if (isSubmitting != other.isSubmitting) return false
        if (errorMessage != other.errorMessage) return false
        if (successMessage != other.successMessage) return false
        if (navigationEvent != other.navigationEvent) return false
        if (homeworkList != other.homeworkList) return false
        if (selectedHomeworkId != other.selectedHomeworkId) return false
        if (selectedHomework != other.selectedHomework) return false
        if (selectedFileUri != other.selectedFileUri) return false
        if (selectedFileBytes != null) {
            if (other.selectedFileBytes == null) return false
            if (!selectedFileBytes.contentEquals(other.selectedFileBytes)) return false
        } else if (other.selectedFileBytes != null) return false
        if (selectedFileMimeType != other.selectedFileMimeType) return false
        if (submissionStatus != other.submissionStatus) return false
        if (reviewerName != other.reviewerName) return false
        if (reviewedFeedback != other.reviewedFeedback) return false
        if (reviewedNextStep != other.reviewedNextStep) return false
        if (reviewedAt != other.reviewedAt) return false
        if (reviewedChecklist != other.reviewedChecklist) return false
        if (hasReview != other.hasReview) return false

        return true
    }

    override fun hashCode(): Int {
        var result = headerNumber.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + badgeText.hashCode()
        result = 31 * result + subtitle.hashCode()
        result = 31 * result + isFileAttached.hashCode()
        result = 31 * result + attachedFileName.hashCode()
        result = 31 * result + attachedFileDetails.hashCode()
        result = 31 * result + instructorNote.hashCode()
        result = 31 * result + isConsentChecked.hashCode()
        result = 31 * result + privacyNotice.hashCode()
        result = 31 * result + submitButtonText.hashCode()
        result = 31 * result + isLoading.hashCode()
        result = 31 * result + isSubmitting.hashCode()
        result = 31 * result + (errorMessage?.hashCode() ?: 0)
        result = 31 * result + (successMessage?.hashCode() ?: 0)
        result = 31 * result + navigationEvent.hashCode()
        result = 31 * result + homeworkList.hashCode()
        result = 31 * result + (selectedHomeworkId ?: 0)
        result = 31 * result + (selectedHomework?.hashCode() ?: 0)
        result = 31 * result + (selectedFileUri?.hashCode() ?: 0)
        result = 31 * result + (selectedFileBytes?.contentHashCode() ?: 0)
        result = 31 * result + (selectedFileMimeType?.hashCode() ?: 0)
        result = 31 * result + (submissionStatus?.hashCode() ?: 0)
        result = 31 * result + (reviewerName?.hashCode() ?: 0)
        result = 31 * result + (reviewedFeedback?.hashCode() ?: 0)
        result = 31 * result + (reviewedNextStep?.hashCode() ?: 0)
        result = 31 * result + (reviewedAt?.hashCode() ?: 0)
        result = 31 * result + reviewedChecklist.hashCode()
        result = 31 * result + hasReview.hashCode()
        return result
    }
}
