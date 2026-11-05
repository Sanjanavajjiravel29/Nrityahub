package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Request payload for admin resetting another user's password.
 */
data class AdminResetPasswordRequest(
    @SerializedName("user_id")
    val userId: Int? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("new_password")
    val newPassword: String
)

