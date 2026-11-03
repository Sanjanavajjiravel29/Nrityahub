package com.simats.nritahub.data.remote.api

import com.simats.nritahub.data.remote.dto.LoginRequest
import com.simats.nritahub.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service definition for authentication endpoints.
 */
interface AuthApiService {

    @POST("auth/login.php")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("auth/logout.php")
    suspend fun logout(): Response<Map<String, Any>>
}
