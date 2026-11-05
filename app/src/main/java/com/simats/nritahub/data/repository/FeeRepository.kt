package com.simats.nritahub.data.repository

import com.google.gson.Gson
import com.simats.nritahub.data.remote.RetrofitClient
import com.simats.nritahub.data.remote.api.FeeApiService
import com.simats.nritahub.data.remote.dto.CreateFeeRequest
import com.simats.nritahub.data.remote.dto.FeeRecordDto
import com.simats.nritahub.data.remote.dto.FeesListResponse
import com.simats.nritahub.data.remote.dto.GenericApiResponse
import com.simats.nritahub.data.remote.dto.UpdateFeeStatusRequest
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class FeeRepository(
    private val feeApiService: FeeApiService = RetrofitClient.createService()
) {
    private val gson = Gson()

    /**
     * Lists fees and/or staff salaries according to caller permissions and filters.
     * Bearer token is automatically attached.
     */
    suspend fun listFees(
        type: String? = null,
        userId: Int? = null,
        status: String? = null
    ): Result<FeesListResponse> {
        return try {
            val response = feeApiService.listFees(type = type, recordType = type, userId = userId, status = status)
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
                        403 -> "Access denied. You are not authorized to view these fee records."
                        else -> "Failed to load fee records (HTTP ${response.code()})."
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
     * Creates a new student fee invoice or staff salary payout record (Admin only).
     */
    suspend fun createFee(request: CreateFeeRequest): Result<FeeRecordDto> {
        return try {
            val response = feeApiService.createFee(request)
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
                        400 -> "Invalid fee details provided. Please check all fields."
                        401 -> "Session expired or invalid. Please log in again."
                        403 -> "Access denied. Only an administrator can create fee records."
                        404 -> "Target student or user not found."
                        else -> "Failed to create fee record (HTTP ${response.code()})."
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
     * Updates payment status of a fee or payout record (Admin only).
     */
    suspend fun updateFeeStatus(feeId: Int, status: String): Result<String> {
        return try {
            val request = UpdateFeeStatusRequest(id = feeId, status = status)
            val response = feeApiService.updateFeeStatus(request)
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
                        403 -> "Access denied. Only an administrator can update fee status."
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

