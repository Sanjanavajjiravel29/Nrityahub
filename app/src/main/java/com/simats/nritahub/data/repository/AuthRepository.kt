package com.simats.nritahub.data.repository

import com.google.gson.Gson
import com.simats.nritahub.data.remote.RetrofitClient
import com.simats.nritahub.data.remote.api.AuthApiService
import com.simats.nritahub.data.remote.dto.LoginRequest
import com.simats.nritahub.data.remote.dto.LoginResponse
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed interface AuthResult {
    data class Success(val response: LoginResponse) : AuthResult
    data class Error(val message: String) : AuthResult
}

class AuthRepository(
    private val authApiService: AuthApiService = RetrofitClient.createService()
) {
    private val gson = Gson()

    /**
     * Authenticates a user against the PHP backend.
     * Passwords are never logged or exposed.
     */
    suspend fun login(identifier: String, password: String): AuthResult {
        return try {
            val response = authApiService.login(
                LoginRequest(
                    identifier = identifier.trim(),
                    password = password
                )
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.user != null) {
                    AuthResult.Success(body)
                } else {
                    val errorMsg = body?.message ?: "Login failed. Please check your credentials."
                    AuthResult.Error(errorMsg)
                }
            } else {
                // Parse error body JSON if present
                val errorBodyStr = response.errorBody()?.string()
                val parsedMessage = parseErrorMessage(errorBodyStr)

                val message = when (response.code()) {
                    401 -> parsedMessage ?: "Invalid email/username or password."
                    400 -> parsedMessage ?: "Please enter both identifier and password."
                    403 -> parsedMessage ?: "Account is inactive. Contact the administrator."
                    404 -> parsedMessage ?: "Endpoint not found on server."
                    in 500..599 -> parsedMessage ?: "Server error occurred. Please try again later."
                    else -> parsedMessage ?: "Request failed with HTTP status ${response.code()}."
                }
                AuthResult.Error(message)
            }
        } catch (e: UnknownHostException) {
            AuthResult.Error("Cannot connect to server. Please check your internet connection.")
        } catch (e: SocketTimeoutException) {
            AuthResult.Error("Connection timed out. The server or ngrok tunnel took too long to respond.")
        } catch (e: IOException) {
            AuthResult.Error("Network error: ${e.localizedMessage ?: "Failed to connect to the academy server."}")
        } catch (e: Exception) {
            AuthResult.Error("An unexpected error occurred: ${e.localizedMessage ?: "Please try again."}")
        }
    }

    private fun parseErrorMessage(errorBody: String?): String? {
        if (errorBody.isNullOrBlank()) return null
        return try {
            val errorResponse = gson.fromJson(errorBody, LoginResponse::class.java)
            errorResponse?.message?.takeIf { it.isNotBlank() }
        } catch (_: Exception) {
            null
        }
    }
}

