package com.simats.nritahub.data.repository

import com.google.gson.Gson
import com.simats.nritahub.data.remote.RetrofitClient
import com.simats.nritahub.data.remote.api.HomeworkApiService
import com.simats.nritahub.data.remote.dto.ChecklistItemDto
import com.simats.nritahub.data.remote.dto.CreateHomeworkRequest
import com.simats.nritahub.data.remote.dto.CreateHomeworkResponse
import com.simats.nritahub.data.remote.dto.GenericApiResponse
import com.simats.nritahub.data.remote.dto.HomeworkListResponse
import com.simats.nritahub.data.remote.dto.ReviewSubmissionRequest
import com.simats.nritahub.data.remote.dto.ReviewSubmissionResponse
import com.simats.nritahub.data.remote.dto.SubmissionDetailsResponse
import com.simats.nritahub.data.remote.dto.SubmissionsListResponse
import com.simats.nritahub.data.remote.dto.SubmitHomeworkJsonRequest
import com.simats.nritahub.data.remote.dto.SubmitHomeworkResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Repository handling Homework assignments, student submissions, and instructor practice reviews.
 */
class HomeworkRepository(
    private val homeworkApiService: HomeworkApiService = RetrofitClient.createService()
) {
    private val gson = Gson()

    suspend fun listHomework(
        batchId: Int? = null,
        instructorId: Int? = null,
        studentId: Int? = null
    ): Result<HomeworkListResponse> {
        return try {
            val response = homeworkApiService.listHomework(batchId, instructorId, studentId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to retrieve homework assignments."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired. Please log in again."
                        403 -> "Access denied. You are not authorized to view these assignments."
                        else -> "Failed to load homework assignments (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    suspend fun createHomework(
        batchId: Int,
        title: String,
        topic: String? = null,
        description: String? = null,
        dueDate: String? = null
    ): Result<CreateHomeworkResponse> {
        return try {
            val request = CreateHomeworkRequest(
                batchId = batchId,
                title = title,
                topic = topic ?: title,
                description = description,
                dueDate = dueDate
            )
            val response = homeworkApiService.createHomework(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to create homework assignment."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired. Please log in again."
                        403 -> "Access denied. You are not authorized to create homework for this batch."
                        else -> "Failed to create assignment (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    suspend fun listSubmissions(
        batchId: Int? = null,
        instructorId: Int? = null,
        studentId: Int? = null,
        status: String? = null
    ): Result<SubmissionsListResponse> {
        return try {
            val response = homeworkApiService.listSubmissions(batchId, instructorId, studentId, status)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to retrieve student submissions."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired. Please log in again."
                        403 -> "Access denied. You are not authorized to view these submissions."
                        else -> "Failed to load submissions (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    suspend fun getSubmission(id: Int): Result<SubmissionDetailsResponse> {
        return try {
            val response = homeworkApiService.getSubmission(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to retrieve submission details."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired. Please log in again."
                        403 -> "Access denied. You are not authorized to view this submission."
                        404 -> "Submission not found."
                        else -> "Failed to load submission details (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    suspend fun reviewSubmission(
        submissionId: Int,
        feedback: String,
        nextStep: String,
        checklist: List<ChecklistItemDto>?,
        status: String = "Published"
    ): Result<ReviewSubmissionResponse> {
        return try {
            val request = ReviewSubmissionRequest(
                submissionId = submissionId,
                feedback = feedback,
                nextStep = nextStep,
                checklist = checklist,
                status = status
            )
            val response = homeworkApiService.reviewSubmission(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to publish review."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired. Please log in again."
                        403 -> "Access denied. You are not authorized to review this submission."
                        else -> "Failed to save review (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    suspend fun submitHomework(
        homeworkId: Int? = null,
        batchId: Int? = null,
        studentId: Int? = null,
        topic: String,
        studentNote: String? = null,
        fileName: String? = null,
        fileDetails: String? = null,
        videoUrl: String? = null,
        fileBytes: ByteArray? = null,
        mimeType: String? = null
    ): Result<SubmitHomeworkResponse> {
        return try {
            val response = if (fileBytes != null && fileBytes.isNotEmpty() && !fileName.isNullOrBlank()) {
                val textMediaType = "text/plain".toMediaTypeOrNull()
                val hwIdPart = homeworkId?.toString()?.toRequestBody(textMediaType)
                val batchIdPart = batchId?.toString()?.toRequestBody(textMediaType)
                val studentIdPart = studentId?.toString()?.toRequestBody(textMediaType)
                val topicPart = topic.toRequestBody(textMediaType)
                val notePart = studentNote?.toRequestBody(textMediaType)

                val fileMediaType = (mimeType ?: "video/mp4").toMediaTypeOrNull()
                val fileRequestBody = fileBytes.toRequestBody(fileMediaType)
                val filePart = MultipartBody.Part.createFormData("video_file", fileName, fileRequestBody)

                homeworkApiService.submitHomeworkMultipart(
                    homeworkId = hwIdPart,
                    batchId = batchIdPart,
                    studentId = studentIdPart,
                    topic = topicPart,
                    studentNote = notePart,
                    file = filePart
                )
            } else {
                val jsonRequest = SubmitHomeworkJsonRequest(
                    homeworkId = homeworkId,
                    batchId = batchId,
                    studentId = studentId,
                    topic = topic,
                    studentNote = studentNote,
                    fileName = fileName,
                    fileDetails = fileDetails,
                    videoUrl = videoUrl
                )
                homeworkApiService.submitHomeworkJson(jsonRequest)
            }

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to submit homework."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired. Please log in again."
                        403 -> "Access denied. You can only submit for your own account and enrolled batch."
                        else -> "Failed to submit homework (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    private fun parseErrorMessage(errorBody: String?): String? {
        if (errorBody.isNullOrBlank()) return null
        return try {
            val parsed = gson.fromJson(errorBody, GenericApiResponse::class.java)
            parsed?.message
        } catch (_: Exception) {
            null
        }
    }
}

