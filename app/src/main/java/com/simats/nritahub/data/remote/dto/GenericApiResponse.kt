package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Standard API response with success flag and message.
 */
data class GenericApiResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)

