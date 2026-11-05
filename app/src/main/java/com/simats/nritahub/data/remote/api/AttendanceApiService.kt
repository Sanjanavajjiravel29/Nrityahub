package com.simats.nritahub.data.remote.api

import com.simats.nritahub.data.remote.dto.BatchAttendanceResponse
import com.simats.nritahub.data.remote.dto.BatchesListResponse
import com.simats.nritahub.data.remote.dto.MarkAttendanceRequest
import com.simats.nritahub.data.remote.dto.MarkAttendanceResponse
import com.simats.nritahub.data.remote.dto.StudentAttendanceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Retrofit service definition for Attendance endpoints.
 * All requests are automatically signed with the user's Bearer token via RetrofitClient.
 */
interface AttendanceApiService {

    /**
     * Lists active batches for attendance selection.
     * For Admin: Returns all batches.
     * For Instructor: Backend restricts to batches assigned to them.
     */
    @GET("batches/list_batches.php")
    suspend fun listBatches(
        @Query("status") status: String? = "Active",
        @Query("instructor_id") instructorId: Int? = null
    ): Response<BatchesListResponse>

    /**
     * Retrieves enrolled students and their attendance status for a batch on a specified date.
     * Restricted to Admin and Instructor (enforces instructor batch ownership).
     */
    @GET("attendance/get_batch_attendance.php")
    suspend fun getBatchAttendance(
        @Query("batch_id") batchId: Int,
        @Query("date") date: String? = null
    ): Response<BatchAttendanceResponse>

    /**
     * Records or updates attendance for a batch on a specified date.
     * Uses ON DUPLICATE KEY UPDATE on backend to avoid duplication.
     */
    @POST("attendance/mark_attendance.php")
    suspend fun markAttendance(
        @Body request: MarkAttendanceRequest
    ): Response<MarkAttendanceResponse>

    /**
     * Retrieves attendance history and statistics for a student.
     * Students can only retrieve their own attendance records.
     */
    @GET("attendance/get_student_attendance.php")
    suspend fun getStudentAttendance(
        @Query("student_id") studentId: Int? = null
    ): Response<StudentAttendanceResponse>
}

