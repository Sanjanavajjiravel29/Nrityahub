package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Encapsulates the response from POST auth/login.php.
 * Returns the authenticated user's profile, role, and the cryptographic Bearer token.
 */
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("user")
    val user: UserDto? = null,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("expires_at")
    val expiresAt: String? = null
)
