package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BatchDetailsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("batch")
    val batch: BatchDto? = null,

    @SerializedName("enrolled_count")
    val enrolledCount: Int = 0,

    @SerializedName("students")
    val students: List<UserDto> = emptyList()
)

