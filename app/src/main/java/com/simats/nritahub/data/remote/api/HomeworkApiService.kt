package com.simats.nritahub.data.remote.api

import com.simats.nritahub.data.remote.dto.CreateHomeworkRequest
import com.simats.nritahub.data.remote.dto.CreateHomeworkResponse
import com.simats.nritahub.data.remote.dto.HomeworkListResponse
import com.simats.nritahub.data.remote.dto.ReviewSubmissionRequest
import com.simats.nritahub.data.remote.dto.ReviewSubmissionResponse
import com.simats.nritahub.data.remote.dto.SubmissionDetailsResponse
import com.simats.nritahub.data.remote.dto.SubmissionsListResponse
import com.simats.nritahub.data.remote.dto.SubmitHomeworkJsonRequest
import com.simats.nritahub.data.remote.dto.SubmitHomeworkResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Retrofit service definition for Homework and Submission endpoints.
 * All requests are automatically signed with the user's Bearer token via RetrofitClient.
 */
interface HomeworkApiService {

    /**
     * Lists homework assignments.
     * - Students: Automatically scoped to their enrolled batch(es).
     * - Instructors: Scoped to their assigned batches.
     * - Admin: Can view all or filter by batch/instructor.
     */
    @GET("homework/list_homework.php")
    suspend fun listHomework(
        @Query("batch_id") batchId: Int? = null,
        @Query("instructor_id") instructorId: Int? = null,
        @Query("student_id") studentId: Int? = null
    ): Response<HomeworkListResponse>

    /**
     * Creates and publishes a homework assignment for a batch.
     * Restricted to Admin and Instructor (authorized for the batch).
     */
    @POST("homework/create_homework.php")
    suspend fun createHomework(
        @Body request: CreateHomeworkRequest
    ): Response<CreateHomeworkResponse>

    /**
     * Lists student homework submissions.
     * - Instructors: Returns submissions for their batches.
     * - Students: Returns only their own submissions.
     * - Admin: Can view all submissions.
     */
    @GET("homework/list_submissions.php")
    suspend fun listSubmissions(
        @Query("batch_id") batchId: Int? = null,
        @Query("instructor_id") instructorId: Int? = null,
        @Query("student_id") studentId: Int? = null,
        @Query("status") status: String? = null
    ): Response<SubmissionsListResponse>

    /**
     * Retrieves full details of an individual submission including checklist and review.
     */
    @GET("homework/get_submission.php")
    suspend fun getSubmission(
        @Query("id") id: Int
    ): Response<SubmissionDetailsResponse>

    /**
     * Publishes feedback review and checklist evaluation for a submission.
     * Restricted to Instructors (batch owners) and Admin.
     */
    @POST("homework/review_submission.php")
    suspend fun reviewSubmission(
        @Body request: ReviewSubmissionRequest
    ): Response<ReviewSubmissionResponse>

    /**
     * Submits homework with JSON metadata.
     */
    @POST("homework/submit_homework.php")
    suspend fun submitHomeworkJson(
        @Body request: SubmitHomeworkJsonRequest
    ): Response<SubmitHomeworkResponse>

    /**
     * Submits homework with multipart file attachment (video / PDF).
     */
    @Multipart
    @POST("homework/submit_homework.php")
    suspend fun submitHomeworkMultipart(
        @Part("homework_id") homeworkId: RequestBody?,
        @Part("batch_id") batchId: RequestBody?,
        @Part("student_id") studentId: RequestBody?,
        @Part("topic") topic: RequestBody,
        @Part("student_note") studentNote: RequestBody?,
        @Part file: MultipartBody.Part?
    ): Response<SubmitHomeworkResponse>
}

