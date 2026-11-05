package com.simats.nritahub.data.repository

import com.google.gson.Gson
import com.simats.nritahub.data.remote.RetrofitClient
import com.simats.nritahub.data.remote.api.AttendanceApiService
import com.simats.nritahub.data.remote.dto.BatchAttendanceResponse
import com.simats.nritahub.data.remote.dto.BatchesListResponse
import com.simats.nritahub.data.remote.dto.GenericApiResponse
import com.simats.nritahub.data.remote.dto.MarkAttendanceRequest
import com.simats.nritahub.data.remote.dto.MarkAttendanceResponse
import com.simats.nritahub.data.remote.dto.MarkStudentAttendanceItem
import com.simats.nritahub.data.remote.dto.StudentAttendanceResponse
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Repository responsible for attendance operations:
 * - Fetching active batches for the caller (Admin sees all; Instructor sees assigned).
 * - Fetching batch attendance records for a selected date.
 * - Marking/updating attendance records.
 * - Retrieving student personal attendance history.
 */
class AttendanceRepository(
    private val attendanceApiService: AttendanceApiService = RetrofitClient.createService()
) {
    private val gson = Gson()

    /**
     * Lists active batches for attendance selection.
     */
    suspend fun listBatches(
        status: String? = "Active",
        instructorId: Int? = null
    ): Result<BatchesListResponse> {
        return try {
            val response = attendanceApiService.listBatches(status = status, instructorId = instructorId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to retrieve batches."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. You do not have permission to view batches."
                        else -> "Failed to load batches (HTTP ${response.code()})."
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

    /**
     * Retrieves attendance for all enrolled students in a batch on a given date.
     */
    suspend fun getBatchAttendance(
        batchId: Int,
        date: String? = null
    ): Result<BatchAttendanceResponse> {
        return try {
            val response = attendanceApiService.getBatchAttendance(batchId = batchId, date = date)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to retrieve batch attendance."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired. Please log in again."
                        403 -> "Access denied. You are not authorized to view attendance for this batch."
                        404 -> "Batch not found."
                        else -> "Failed to load attendance (HTTP ${response.code()})."
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

    /**
     * Marks or updates attendance for students in a batch on a specified date.
     */
    suspend fun markAttendance(
        batchId: Int,
        date: String,
        students: List<MarkStudentAttendanceItem>
    ): Result<MarkAttendanceResponse> {
        return try {
            val request = MarkAttendanceRequest(
                batchId = batchId,
                attendanceDate = date,
                students = students
            )
            val response = attendanceApiService.markAttendance(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to record attendance."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired. Please log in again."
                        403 -> "Access denied. You are not authorized to mark attendance for this batch."
                        else -> "Failed to save attendance (HTTP ${response.code()})."
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

    /**
     * Retrieves attendance history and statistics for a student.
     */
    suspend fun getStudentAttendance(
        studentId: Int? = null
    ): Result<StudentAttendanceResponse> {
        return try {
            val response = attendanceApiService.getStudentAttendance(studentId = studentId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to retrieve student attendance history."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired. Please log in again."
                        403 -> "Access denied. Students can only view their own attendance records."
                        404 -> "Student not found."
                        else -> "Failed to load attendance history (HTTP ${response.code()})."
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

