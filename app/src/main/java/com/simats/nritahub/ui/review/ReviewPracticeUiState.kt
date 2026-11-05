package com.simats.nritahub.ui.review

import com.simats.nritahub.data.remote.dto.BatchDto
import com.simats.nritahub.data.remote.dto.SubmissionItemDto

data class ChecklistItem(
    val id: String,
    val title: String,
    val description: String,
    val isChecked: Boolean = false
)

sealed interface ReviewPracticeNavigationEvent {
    data object None : ReviewPracticeNavigationEvent
    data object NavigateBack : ReviewPracticeNavigationEvent
    data object PublishFeedbackSuccess : ReviewPracticeNavigationEvent
    data object SaveDraftSuccess : ReviewPracticeNavigationEvent
    data object CreateHomeworkSuccess : ReviewPracticeNavigationEvent
}

data class ReviewPracticeUiState(
    val headerNumber: String = "27",
    val title: String = "Review practice",
    val studentInitials: String = "AR",
    val studentNameAndBatch: String = "Ananya Rao · Foundation F1",
    val statusBadge: String = "Submitted Video",
    val submissionDetails: String = "Tatta Adavu · Submitted to instructor on 5 Oct, 7:40 PM · 01:18 recording",
    val studentNoteTitle: String = "STUDENT SUBMISSION NOTE",
    val studentNoteText: String = "\"My count feels steadier now. I'd love help keeping my shoulders relaxed in the last set.\"",
    val checklistTitle: String = "Instructor Evaluation Checklist",
    val checklistSubtitle: String = "",
    val checklistItems: List<ChecklistItem> = listOf(
        ChecklistItem("1", "Anga Shuddham", "Body posture and clarity of form", true),
        ChecklistItem("2", "Aramandi", "Proper half-sitting stance maintenance", true),
        ChecklistItem("3", "Mudras Clarity", "Hand gesture accuracy and precision", true),
        ChecklistItem("4", "Bhavam", "Expressive facial and emotional engagement", false),
        ChecklistItem("5", "Steps", "Correct execution of adavu steps", true),
        ChecklistItem("6", "Tala", "Rhythm and time cycle synchronization", true),
        ChecklistItem("7", "Leg Posture", "Foot placement and leg positioning", true),
        ChecklistItem("8", "Neatness", "Cleanliness of movements and execution", true),
        ChecklistItem("9", "Body Stiffness", "Flexibility and absence of unwanted tension", false),
        ChecklistItem("10", "Back Position", "Straight spine and torso alignment", true)
    ),
    val commentBoxTitle: String = "Instructor Comments",
    val commentBoxSubtitle: String = "",
    val feedbackText: String = "Your rhythm is settling beautifully! Practice relaxing your shoulders during the 3rd speed set.",
    val nextStepText: String = "Practice a comfortable araimandi for 2 minutes daily before recording.",
    val privacyNotice: String = "Comments and checklist feedback will be saved for student.",
    val isPublished: Boolean = false,
    val isDraftSaved: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val navigationEvent: ReviewPracticeNavigationEvent = ReviewPracticeNavigationEvent.None,

    // Live Submissions List & Active Selection
    val submissionsList: List<SubmissionItemDto> = emptyList(),
    val selectedSubmissionId: Int? = null,
    val selectedFilter: String = "All", // "All", "Pending", "Reviewed"
    val videoUrl: String? = null,
    val videoFullUrl: String? = null,

    // Create Homework Dialog State
    val showCreateHomeworkDialog: Boolean = false,
    val authorizedBatches: List<BatchDto> = emptyList(),
    val newHomeworkBatchId: Int? = null,
    val newHomeworkTitle: String = "",
    val newHomeworkTopic: String = "",
    val newHomeworkDescription: String = "",
    val newHomeworkDueDate: String = "",
    val isCreatingHomework: Boolean = false,
    val createHomeworkError: String? = null,
    val createHomeworkSuccessMessage: String? = null
)
