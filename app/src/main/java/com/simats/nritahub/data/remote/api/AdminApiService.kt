package com.simats.nritahub.data.remote.api

import com.simats.nritahub.data.remote.dto.AdminDashboardStatsResponse
import com.simats.nritahub.data.remote.dto.AdminResetPasswordRequest
import com.simats.nritahub.data.remote.dto.AssignStudentRequest
import com.simats.nritahub.data.remote.dto.BatchDetailsResponse
import com.simats.nritahub.data.remote.dto.BatchesListResponse
import com.simats.nritahub.data.remote.dto.CreateBatchRequest
import com.simats.nritahub.data.remote.dto.CreateUserRequest
import com.simats.nritahub.data.remote.dto.CreateUserResponse
import com.simats.nritahub.data.remote.dto.GenericApiResponse
import com.simats.nritahub.data.remote.dto.UpdateBatchRequest
import com.simats.nritahub.data.remote.dto.UsersListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Retrofit service definition for Admin-exclusive endpoints.
 * All requests are automatically signed with the Admin Bearer token via RetrofitClient.
 */
interface AdminApiService {

    @GET("admin/dashboard_stats.php")
    suspend fun getDashboardStats(): Response<AdminDashboardStatsResponse>

    @GET("users/list_users.php")
    suspend fun listUsers(
        @Query("role") role: String? = null,
        @Query("batch_id") batchId: Int? = null,
        @Query("q") search: String? = null
    ): Response<UsersListResponse>

    @POST("users/create_user.php")
    suspend fun createUser(
        @Body request: CreateUserRequest
    ): Response<CreateUserResponse>

    @POST("auth/resetpasswd.php")
    suspend fun resetPassword(
        @Body request: AdminResetPasswordRequest
    ): Response<GenericApiResponse>

    // --- Batch Management Endpoints ---

    @GET("batches/list_batches.php")
    suspend fun listBatches(
        @Query("status") status: String? = null,
        @Query("instructor_id") instructorId: Int? = null,
        @Query("student_id") studentId: Int? = null
    ): Response<BatchesListResponse>

    @GET("batches/get_batch.php")
    suspend fun getBatch(
        @Query("id") batchId: Int
    ): Response<BatchDetailsResponse>

    @POST("batches/create_batch.php")
    suspend fun createBatch(
        @Body request: CreateBatchRequest
    ): Response<BatchDetailsResponse>

    @POST("batches/update_batch.php")
    suspend fun updateBatch(
        @Body request: UpdateBatchRequest
    ): Response<BatchDetailsResponse>

    @POST("batches/assign_student.php")
    suspend fun assignStudent(
        @Body request: AssignStudentRequest
    ): Response<GenericApiResponse>

    @POST("batches/remove_student.php")
    suspend fun removeStudent(
        @Body request: AssignStudentRequest
    ): Response<GenericApiResponse>

    // --- Fee & Salary Management Endpoints ---

    @GET("fees/list_fees.php")
    suspend fun listFees(
        @Query("type") type: String? = null,
        @Query("record_type") recordType: String? = null,
        @Query("user_id") userId: Int? = null,
        @Query("status") status: String? = null
    ): Response<com.simats.nritahub.data.remote.dto.FeesListResponse>

    @POST("fees/create_fee.php")
    suspend fun createFee(
        @Body request: com.simats.nritahub.data.remote.dto.CreateFeeRequest
    ): Response<com.simats.nritahub.data.remote.dto.CreateFeeResponse>

    @POST("fees/update_fee_status.php")
    suspend fun updateFeeStatus(
        @Body request: com.simats.nritahub.data.remote.dto.UpdateFeeStatusRequest
    ): Response<GenericApiResponse>
}
