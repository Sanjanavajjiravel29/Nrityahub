package com.simats.nritahub.data.repository

import com.google.gson.Gson
import com.simats.nritahub.data.remote.RetrofitClient
import com.simats.nritahub.data.remote.api.AdminApiService
import com.simats.nritahub.data.remote.dto.AdminDashboardStatsResponse
import com.simats.nritahub.data.remote.dto.AdminResetPasswordRequest
import com.simats.nritahub.data.remote.dto.AssignStudentRequest
import com.simats.nritahub.data.remote.dto.BatchDetailsResponse
import com.simats.nritahub.data.remote.dto.BatchDto
import com.simats.nritahub.data.remote.dto.CreateBatchRequest
import com.simats.nritahub.data.remote.dto.CreateUserRequest
import com.simats.nritahub.data.remote.dto.UpdateBatchRequest
import com.simats.nritahub.data.remote.dto.UserDto
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed interface AdminStatsResult {
    data class Success(val stats: AdminDashboardStatsResponse) : AdminStatsResult
    data class Error(val message: String, val statusCode: Int? = null) : AdminStatsResult
}

class AdminRepository(
    private val adminApiService: AdminApiService = RetrofitClient.createService()
) {
    private val gson = Gson()

    /**
     * Fetches real-time academy overview metrics and statistics.
     * The Bearer token is automatically attached via OkHttp authInterceptor.
     */
    suspend fun getDashboardStats(): AdminStatsResult {
        return try {
            val response = adminApiService.getDashboardStats()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    AdminStatsResult.Success(body)
                } else {
                    AdminStatsResult.Error(body?.message ?: "Failed to retrieve dashboard statistics.")
                }
            } else {
                val errorBodyStr = response.errorBody()?.string()
                val parsedMessage = parseErrorMessage(errorBodyStr)
                val msg = when (response.code()) {
                    401 -> "Session expired or invalid. Please log in again."
                    403 -> "Access denied. Administrator authorization is required."
                    else -> parsedMessage ?: "Failed to load dashboard statistics (HTTP ${response.code()})."
                }
                AdminStatsResult.Error(msg, response.code())
            }
        } catch (e: UnknownHostException) {
            AdminStatsResult.Error("Cannot connect to server. Please check your internet connection.")
        } catch (e: SocketTimeoutException) {
            AdminStatsResult.Error("Connection timed out. Server took too long to respond.")
        } catch (e: IOException) {
            AdminStatsResult.Error("Network error: ${e.localizedMessage ?: "Failed to connect to the academy server."}")
        } catch (e: Exception) {
            AdminStatsResult.Error("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}")
        }
    }

    /**
     * Lists academy users with optional role, batch, or search filter.
     */
    suspend fun listUsers(
        role: String? = null,
        batchId: Int? = null,
        search: String? = null
    ): Result<List<UserDto>> {
        return try {
            val response = adminApiService.listUsers(role = role, batchId = batchId, search = search)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body.users)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to retrieve users list."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Administrator privileges required."
                        else -> "Failed to load users list (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    /**
     * Creates a new student or instructor user account (Admin only).
     */
    suspend fun createUser(request: CreateUserRequest): Result<UserDto> {
        return try {
            val response = adminApiService.createUser(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.user != null) {
                    Result.success(body.user)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to create user account."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Administrator privileges required to create accounts."
                        409 -> "A user with this username or email already exists."
                        else -> "Failed to create user account (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    /**
     * Resets a user's password and revokes their active sessions (Admin only).
     */
    suspend fun resetPassword(userId: Int? = null, email: String? = null, newPassword: String): Result<String> {
        return try {
            val request = AdminResetPasswordRequest(userId = userId, email = email, newPassword = newPassword)
            val response = adminApiService.resetPassword(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to reset password."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Only an administrator can reset user passwords."
                        404 -> "User not found."
                        else -> "Failed to reset user password (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    // --- Batch Management Operations ---

    /**
     * Fetches all batches or filtered by status/instructor/student.
     */
    suspend fun listBatches(
        status: String? = null,
        instructorId: Int? = null,
        studentId: Int? = null
    ): Result<List<BatchDto>> {
        return try {
            val response = adminApiService.listBatches(status = status, instructorId = instructorId, studentId = studentId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body.batches)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to retrieve batches."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. You do not have permission to list batches."
                        else -> "Failed to load batches (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    /**
     * Fetches details and enrolled student roster for a single batch.
     */
    suspend fun getBatch(batchId: Int): Result<BatchDetailsResponse> {
        return try {
            val response = adminApiService.getBatch(batchId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to retrieve batch details."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. You do not have permission to access this batch."
                        404 -> "Batch not found."
                        else -> "Failed to load batch details (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    /**
     * Creates a new dance batch (Admin only).
     */
    suspend fun createBatch(request: CreateBatchRequest): Result<BatchDto> {
        return try {
            val response = adminApiService.createBatch(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.batch != null) {
                    Result.success(body.batch)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to create batch."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Administrator privileges required to create batches."
                        else -> "Failed to create batch (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    /**
     * Updates an existing dance batch (Admin only).
     */
    suspend fun updateBatch(request: UpdateBatchRequest): Result<BatchDto> {
        return try {
            val response = adminApiService.updateBatch(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.batch != null) {
                    Result.success(body.batch)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to update batch."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Administrator privileges required to update batches."
                        404 -> "Batch not found."
                        else -> "Failed to update batch (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    /**
     * Assigns a student to a batch (Admin only).
     */
    suspend fun assignStudent(batchId: Int, studentId: Int): Result<String> {
        return try {
            val response = adminApiService.assignStudent(AssignStudentRequest(batchId = batchId, studentId = studentId))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to assign student to batch."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Administrator privileges required."
                        404 -> "Batch or student not found."
                        409 -> "Student is already enrolled in this batch."
                        else -> "Failed to assign student (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    /**
     * Removes a student from a batch (Admin only).
     */
    suspend fun removeStudent(batchId: Int, studentId: Int): Result<String> {
        return try {
            val response = adminApiService.removeStudent(AssignStudentRequest(batchId = batchId, studentId = studentId))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to remove student from batch."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Administrator privileges required."
                        404 -> "Student is not enrolled in this batch."
                        else -> "Failed to remove student (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    // --- Fee & Salary Management Operations ---

    suspend fun listFees(
        type: String? = null,
        userId: Int? = null,
        status: String? = null
    ): Result<com.simats.nritahub.data.remote.dto.FeesListResponse> {
        return try {
            val response = adminApiService.listFees(type = type, recordType = type, userId = userId, status = status)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to retrieve fee records."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Administrator privileges required."
                        else -> "Failed to load fee records (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    suspend fun createFee(request: com.simats.nritahub.data.remote.dto.CreateFeeRequest): Result<com.simats.nritahub.data.remote.dto.FeeRecordDto> {
        return try {
            val response = adminApiService.createFee(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.feeRecord != null) {
                    Result.success(body.feeRecord)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to create fee record."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        400 -> "Invalid fee details provided."
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Administrator privileges required."
                        404 -> "Target student not found."
                        else -> "Failed to create fee record (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    suspend fun updateFeeStatus(feeId: Int, status: String): Result<String> {
        return try {
            val request = com.simats.nritahub.data.remote.dto.UpdateFeeStatusRequest(id = feeId, status = status)
            val response = adminApiService.updateFeeStatus(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    Result.success(body.message)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to update payment status."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                    ?: when (response.code()) {
                        400 -> "Invalid status or fee ID."
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Administrator privileges required."
                        404 -> "Fee record not found."
                        else -> "Failed to update fee status (HTTP ${response.code()})."
                    }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Cannot connect to server. Please check your internet connection."))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection timed out. Server took too long to respond."))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Failed to communicate with academy server."}"))
        } catch (e: Exception) {
            Result.failure(Exception("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}"))
        }
    }

    private fun parseErrorMessage(errorBody: String?): String? {
        if (errorBody.isNullOrBlank()) return null
        return try {
            val parsed = gson.fromJson(errorBody, Map::class.java)
            parsed["message"]?.toString()
        } catch (_: Exception) {
            null
        }
    }
}
