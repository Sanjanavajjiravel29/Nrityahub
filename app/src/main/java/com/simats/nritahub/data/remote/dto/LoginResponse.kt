package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Standard backend response payload for the login endpoint.
 */
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("user")
    val user: UserDto? = null,

    @SerializedName("role")
    val role: String? = null
)

