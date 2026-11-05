package com.simats.nritahub.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.nritahub.data.remote.dto.ChecklistItemDto
import com.simats.nritahub.data.remote.dto.SubmissionItemDto
import com.simats.nritahub.data.repository.AdminRepository
import com.simats.nritahub.data.repository.HomeworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReviewPracticeViewModel(
    private val homeworkRepository: HomeworkRepository = HomeworkRepository(),
    private val adminRepository: AdminRepository = AdminRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewPracticeUiState())
    val uiState: StateFlow<ReviewPracticeUiState> = _uiState.asStateFlow()

    private val defaultChecklist = listOf(
        ChecklistItem("1", "Anga Shuddham", "Body posture and clarity of form", false),
        ChecklistItem("2", "Aramandi", "Proper half-sitting stance maintenance", false),
        ChecklistItem("3", "Mudras Clarity", "Hand gesture accuracy and precision", false),
        ChecklistItem("4", "Bhavam", "Expressive facial and emotional engagement", false),
        ChecklistItem("5", "Steps", "Correct execution of adavu steps", false),
        ChecklistItem("6", "Tala", "Rhythm and time cycle synchronization", false),
        ChecklistItem("7", "Leg Posture", "Foot placement and leg positioning", false),
        ChecklistItem("8", "Neatness", "Cleanliness of movements and execution", false),
        ChecklistItem("9", "Body Stiffness", "Flexibility and absence of unwanted tension", false),
        ChecklistItem("10", "Back Position", "Straight spine and torso alignment", false)
    )

    init {
        loadSubmissions()
        loadAuthorizedBatches()
    }

    fun loadSubmissions(status: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val filterParam = when (status ?: _uiState.value.selectedFilter) {
                "Pending" -> "Pending"
                "Reviewed" -> "Reviewed"
                else -> null
            }

            val result = homeworkRepository.listSubmissions(status = filterParam)
            result.onSuccess { response ->
                val submissions = response.submissions
                if (submissions.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            submissionsList = emptyList(),
                            selectedSubmissionId = null,
                            studentNameAndBatch = "No Submissions Found",
                            studentInitials = "--",
                            statusBadge = "Empty",
                            submissionDetails = "No student practice recordings have been submitted yet.",
                            studentNoteText = "No notes available.",
                            videoUrl = null,
                            videoFullUrl = null,
                            feedbackText = "",
                            nextStepText = "",
                            isPublished = false,
                            isDraftSaved = false
                        )
                    }
                } else {
                    val currentSelectedId = _uiState.value.selectedSubmissionId
                    val itemToSelect = submissions.find { it.id == currentSelectedId } ?: submissions.first()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            submissionsList = submissions,
                            selectedSubmissionId = itemToSelect.id
                        )
                    }
                    selectSubmission(itemToSelect.id)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to load submissions."
                    )
                }
            }
        }
    }

    fun selectSubmission(submissionId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, selectedSubmissionId = submissionId, errorMessage = null) }
            val result = homeworkRepository.getSubmission(submissionId)
            result.onSuccess { response ->
                val sub = response.submission ?: return@onSuccess
                val isReviewed = sub.status.equals("Reviewed", ignoreCase = true)

                // Map checklist items
                val checklist = if (!sub.checklistItems.isNullOrEmpty()) {
                    sub.checklistItems.map { dto ->
                        ChecklistItem(
                            id = dto.id,
                            title = dto.title,
                            description = dto.description,
                            isChecked = dto.isChecked
                        )
                    }
                } else {
                    defaultChecklist
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selectedSubmissionId = sub.id,
                        studentInitials = sub.studentInitials ?: getInitials(sub.studentName ?: "Student"),
                        studentNameAndBatch = sub.studentNameAndBatch ?: "${sub.studentName ?: "Student"} · ${sub.batchName ?: "Batch"}",
                        statusBadge = if (isReviewed) "Reviewed" else "Submitted Video",
                        submissionDetails = "${sub.topic} · Submitted ${sub.submittedTimeFormatted ?: sub.submittedAt} · ${sub.fileDetails ?: "Recording"}",
                        studentNoteText = if (!sub.studentNote.isNullOrBlank()) "\"${sub.studentNote}\"" else "\"No student submission note provided.\"",
                        checklistItems = checklist,
                        feedbackText = sub.feedback ?: "",
                        nextStepText = sub.nextStep ?: "",
                        videoUrl = sub.videoUrl,
                        videoFullUrl = sub.videoFullUrl,
                        isPublished = isReviewed,
                        isDraftSaved = false,
                        privacyNotice = "Comments and checklist feedback will be saved for student ${sub.studentName ?: ""}."
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to load submission details."
                    )
                }
            }
        }
    }

    fun setFilter(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
        loadSubmissions(filter)
    }

    fun onFeedbackChanged(text: String) {
        _uiState.update { it.copy(feedbackText = text) }
    }

    fun onNextStepChanged(text: String) {
        _uiState.update { it.copy(nextStepText = text) }
    }

    fun onChecklistItemToggled(itemId: String) {
        _uiState.update { state ->
            val updatedList = state.checklistItems.map { item ->
                if (item.id == itemId) item.copy(isChecked = !item.isChecked) else item
            }
            state.copy(checklistItems = updatedList)
        }
    }

    fun onPublishFeedbackClicked() {
        val submissionId = _uiState.value.selectedSubmissionId
        if (submissionId == null) {
            _uiState.update { it.copy(errorMessage = "Please select a student submission first.") }
            return
        }

        val feedback = _uiState.value.feedbackText.trim()
        val nextStep = _uiState.value.nextStepText.trim()

        if (feedback.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please provide detailed feedback for the student.") }
            return
        }

        if (nextStep.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please provide an actionable next step for the student.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }

            val checklistDtos = _uiState.value.checklistItems.map {
                ChecklistItemDto(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    isChecked = it.isChecked
                )
            }

            val result = homeworkRepository.reviewSubmission(
                submissionId = submissionId,
                feedback = feedback,
                nextStep = nextStep,
                checklist = checklistDtos,
                status = "Published"
            )

            result.onSuccess {
                _uiState.update { current ->
                    // Update in submission list
                    val updatedList = current.submissionsList.map { s ->
                        if (s.id == submissionId) s.copy(status = "Reviewed") else s
                    }
                    current.copy(
                        isSaving = false,
                        isPublished = true,
                        statusBadge = "Reviewed",
                        submissionsList = updatedList,
                        successMessage = "Feedback published successfully!",
                        navigationEvent = ReviewPracticeNavigationEvent.PublishFeedbackSuccess
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.localizedMessage ?: "Failed to publish feedback."
                    )
                }
            }
        }
    }

    fun onSaveDraftClicked() {
        val submissionId = _uiState.value.selectedSubmissionId
        if (submissionId == null) {
            _uiState.update { it.copy(errorMessage = "Please select a student submission first.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, successMessage = null) }

            val checklistDtos = _uiState.value.checklistItems.map {
                ChecklistItemDto(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    isChecked = it.isChecked
                )
            }

            val result = homeworkRepository.reviewSubmission(
                submissionId = submissionId,
                feedback = _uiState.value.feedbackText.trim(),
                nextStep = _uiState.value.nextStepText.trim(),
                checklist = checklistDtos,
                status = "Draft"
            )

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        isDraftSaved = true,
                        successMessage = "Review saved as draft successfully!",
                        navigationEvent = ReviewPracticeNavigationEvent.SaveDraftSuccess
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.localizedMessage ?: "Failed to save draft."
                    )
                }
            }
        }
    }

    fun loadAuthorizedBatches() {
        viewModelScope.launch {
            val result = adminRepository.listBatches()
            result.onSuccess { batches ->
                _uiState.update {
                    it.copy(
                        authorizedBatches = batches,
                        newHomeworkBatchId = it.newHomeworkBatchId ?: batches.firstOrNull()?.id
                    )
                }
            }
        }
    }

    fun openCreateHomeworkDialog() {
        _uiState.update {
            it.copy(
                showCreateHomeworkDialog = true,
                createHomeworkError = null,
                createHomeworkSuccessMessage = null,
                newHomeworkTitle = "",
                newHomeworkTopic = "",
                newHomeworkDescription = "",
                newHomeworkDueDate = ""
            )
        }
    }

    fun dismissCreateHomeworkDialog() {
        _uiState.update { it.copy(showCreateHomeworkDialog = false) }
    }

    fun onNewBatchSelected(batchId: Int) {
        _uiState.update { it.copy(newHomeworkBatchId = batchId) }
    }

    fun onNewTitleChanged(title: String) {
        _uiState.update { it.copy(newHomeworkTitle = title) }
    }

    fun onNewTopicChanged(topic: String) {
        _uiState.update { it.copy(newHomeworkTopic = topic) }
    }

    fun onNewDescriptionChanged(desc: String) {
        _uiState.update { it.copy(newHomeworkDescription = desc) }
    }

    fun onNewDueDateChanged(dueDate: String) {
        _uiState.update { it.copy(newHomeworkDueDate = dueDate) }
    }

    fun createHomework() {
        val state = _uiState.value
        val batchId = state.newHomeworkBatchId
        if (batchId == null) {
            _uiState.update { it.copy(createHomeworkError = "Please select an authorized batch.") }
            return
        }

        val title = state.newHomeworkTitle.trim()
        if (title.isBlank()) {
            _uiState.update { it.copy(createHomeworkError = "Please enter homework title.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingHomework = true, createHomeworkError = null) }

            val result = homeworkRepository.createHomework(
                batchId = batchId,
                title = title,
                topic = state.newHomeworkTopic.trim().takeIf { it.isNotBlank() },
                description = state.newHomeworkDescription.trim().takeIf { it.isNotBlank() },
                dueDate = state.newHomeworkDueDate.trim().takeIf { it.isNotBlank() }
            )

            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isCreatingHomework = false,
                        showCreateHomeworkDialog = false,
                        successMessage = "Homework '$title' assigned successfully!",
                        navigationEvent = ReviewPracticeNavigationEvent.CreateHomeworkSuccess
                    )
                }
                loadSubmissions()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isCreatingHomework = false,
                        createHomeworkError = error.localizedMessage ?: "Failed to create homework."
                    )
                }
            }
        }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = ReviewPracticeNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = ReviewPracticeNavigationEvent.None) }
    }

    private fun getInitials(name: String): String {
        val parts = name.trim().split("\\s+".toRegex())
        return when {
            parts.size >= 2 -> "${parts[0].firstOrNull()?.uppercase() ?: ""}${parts[1].firstOrNull()?.uppercase() ?: ""}"
            parts.isNotEmpty() -> parts[0].take(2).uppercase()
            else -> "ST"
        }
    }
}
