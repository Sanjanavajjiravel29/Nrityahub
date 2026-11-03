package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data transfer object for user login credentials.
 * Identifier accepts either email address or username.
 */
data class LoginRequest(
    @SerializedName("identifier")
    val identifier: String,

    @SerializedName("password")
    val password: String
)

