package com.simats.nritahub.data.repository

import com.google.gson.Gson
import com.simats.nritahub.data.remote.RetrofitClient
import com.simats.nritahub.data.remote.api.AdminApiService
import com.simats.nritahub.data.remote.dto.AdminDashboardStatsResponse
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

