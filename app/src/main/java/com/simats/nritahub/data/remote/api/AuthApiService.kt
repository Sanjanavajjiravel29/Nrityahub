package com.simats.nritahub.data.remote.api

import com.simats.nritahub.data.remote.dto.LoginRequest
import com.simats.nritahub.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit interface for authentication operations.
 */
interface AuthApiService {

    /**
     * Authenticates an Admin, Instructor, or Student.
     * Expects JSON body with "identifier" and "password".
     */
    @POST("auth/login.php")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}

