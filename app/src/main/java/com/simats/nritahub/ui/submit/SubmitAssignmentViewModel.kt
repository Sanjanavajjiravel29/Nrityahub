package com.simats.nritahub.ui.submit

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.nritahub.data.remote.dto.HomeworkItemDto
import com.simats.nritahub.data.repository.HomeworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubmitAssignmentViewModel(
    private val homeworkRepository: HomeworkRepository = HomeworkRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubmitAssignmentUiState())
    val uiState: StateFlow<SubmitAssignmentUiState> = _uiState.asStateFlow()

    init {
        loadAssignedHomework()
    }

    fun loadAssignedHomework() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = homeworkRepository.listHomework()
            result.onSuccess { response ->
                val list = response.homework
                if (list.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            homeworkList = emptyList(),
                            title = "No Homework Assigned",
                            badgeText = "UP TO DATE",
                            subtitle = "Your instructor has not assigned any homework for your batch yet.",
                            isFileAttached = false,
                            selectedHomeworkId = null,
                            selectedHomework = null
                        )
                    }
                } else {
                    val currentSelectedId = _uiState.value.selectedHomeworkId
                    val selectedItem = list.find { it.id == currentSelectedId } ?: list.first()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            homeworkList = list
                        )
                    }
                    selectHomeworkItem(selectedItem)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to load homework assignments."
                    )
                }
            }
        }
    }

    fun onSelectHomework(homeworkId: Int) {
        val item = _uiState.value.homeworkList.find { it.id == homeworkId } ?: return
        selectHomeworkItem(item)
    }

    private fun selectHomeworkItem(item: HomeworkItemDto) {
        val instructorName = item.instructorName ?: "Instructor"
        val badge = buildString {
            append(item.topic ?: item.danceStyle ?: "PRACTICE")
            if (!item.dueDate.isNullOrBlank()) {
                append(" · DUE ")
                append(item.dueDate)
            }
        }
        val buttonText = "Submit to $instructorName"
        val subtitle = item.description?.takeIf { it.isNotBlank() }
            ?: "Let $instructorName see what you've been working on. This is a space for learning, not comparison."

        _uiState.update {
            it.copy(
                selectedHomeworkId = item.id,
                selectedHomework = item,
                title = item.title,
                badgeText = badge,
                subtitle = subtitle,
                submitButtonText = buttonText,
                submissionStatus = if (item.isSubmitted == true) item.mySubmission?.status ?: "Submitted" else null,
                errorMessage = null,
                successMessage = null
            )
        }

        // If homework is already submitted, fetch the submission details to display reviews & feedback
        if (item.isSubmitted == true && item.mySubmission != null) {
            fetchSubmissionDetails(item.mySubmission.id)
        } else {
            _uiState.update {
                it.copy(
                    isFileAttached = false,
                    attachedFileName = "",
                    attachedFileDetails = "",
                    selectedFileUri = null,
                    selectedFileBytes = null,
                    reviewerName = null,
                    reviewedFeedback = null,
                    reviewedNextStep = null,
                    reviewedAt = null,
                    reviewedChecklist = emptyList(),
                    hasReview = false
                )
            }
        }
    }

    private fun fetchSubmissionDetails(submissionId: Int) {
        viewModelScope.launch {
            val result = homeworkRepository.getSubmission(submissionId)
            result.onSuccess { response ->
                val sub = response.submission ?: return@onSuccess
                _uiState.update { current ->
                    current.copy(
                        submissionStatus = sub.status,
                        reviewerName = sub.reviewerName,
                        reviewedFeedback = sub.feedback,
                        reviewedNextStep = sub.nextStep,
                        reviewedAt = sub.reviewedAt,
                        reviewedChecklist = sub.checklistItems ?: emptyList(),
                        hasReview = sub.hasReview == true || !sub.feedback.isNullOrBlank(),
                        instructorNote = sub.studentNote ?: current.instructorNote,
                        isFileAttached = !sub.fileName.isNullOrBlank() || !sub.videoUrl.isNullOrBlank(),
                        attachedFileName = sub.fileName ?: sub.videoUrl ?: current.attachedFileName,
                        attachedFileDetails = sub.fileDetails ?: "Submitted practice recording"
                    )
                }
            }
        }
    }

    fun onInstructorNoteChanged(note: String) {
        _uiState.update { it.copy(instructorNote = note) }
    }

    fun toggleConsent() {
        _uiState.update { it.copy(isConsentChecked = !it.isConsentChecked) }
    }

    fun onChooseFileClicked() {
        _uiState.update { it.copy(navigationEvent = SubmitAssignmentNavigationEvent.ChooseFileClicked) }
    }

    fun onFilePicked(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val fileData = withContext(Dispatchers.IO) {
                    var name = "practice_recording.mp4"
                    var size: Long = 0
                    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            val sizeIdx = cursor.getColumnIndex(OpenableColumns.SIZE)
                            if (nameIdx != -1) name = cursor.getString(nameIdx) ?: name
                            if (sizeIdx != -1) size = cursor.getLong(sizeIdx)
                        }
                    }
                    val mime = context.contentResolver.getType(uri) ?: "video/mp4"
                    val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    val sizeFormatted = if (size > 0) {
                        "${size / (1024 * 1024)} MB"
                    } else {
                        "${(bytes?.size ?: 0) / 1024} KB"
                    }
                    Triple(name, "$sizeFormatted · Ready to submit", bytes to mime)
                }

                _uiState.update {
                    it.copy(
                        isFileAttached = true,
                        attachedFileName = fileData.first,
                        attachedFileDetails = fileData.second,
                        selectedFileUri = uri,
                        selectedFileBytes = fileData.third.first,
                        selectedFileMimeType = fileData.third.second,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to load selected file: ${e.localizedMessage}")
                }
            }
        }
    }

    fun onRemoveFileClicked() {
        _uiState.update {
            it.copy(
                isFileAttached = false,
                attachedFileName = "",
                attachedFileDetails = "",
                selectedFileUri = null,
                selectedFileBytes = null,
                selectedFileMimeType = null,
                navigationEvent = SubmitAssignmentNavigationEvent.RemoveFileClicked
            )
        }
    }

    fun onSubmitClicked() {
        val currentState = _uiState.value
        val homework = currentState.selectedHomework

        if (homework == null) {
            _uiState.update { it.copy(errorMessage = "Please select an assigned homework to submit.") }
            return
        }

        if (!currentState.isConsentChecked) {
            _uiState.update { it.copy(errorMessage = "Please accept the consent checkbox before submitting.") }
            return
        }

        if (!currentState.isFileAttached && currentState.instructorNote.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please attach a practice recording or provide a submission note.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null, successMessage = null) }

            val topic = homework.topic ?: homework.title
            val result = homeworkRepository.submitHomework(
                homeworkId = homework.id,
                batchId = homework.batchId,
                topic = topic,
                studentNote = currentState.instructorNote.takeIf { it.isNotBlank() },
                fileName = currentState.attachedFileName.takeIf { it.isNotBlank() },
                fileDetails = currentState.attachedFileDetails.takeIf { it.isNotBlank() },
                videoUrl = if (currentState.selectedFileBytes == null) currentState.attachedFileName.takeIf { it.isNotBlank() } else null,
                fileBytes = currentState.selectedFileBytes,
                mimeType = currentState.selectedFileMimeType
            )

            result.onSuccess { response ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        submissionStatus = response.submission?.status ?: "Pending",
                        successMessage = "Your practice submission was sent to ${homework.instructorName ?: "your instructor"} successfully!",
                        navigationEvent = SubmitAssignmentNavigationEvent.SubmittedToInstructor
                    )
                }
                // Reload homework assignments so the is_submitted flag is updated
                loadAssignedHomework()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = error.localizedMessage ?: "Failed to submit practice. Please try again."
                    )
                }
            }
        }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = SubmitAssignmentNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = SubmitAssignmentNavigationEvent.None) }
    }
}
