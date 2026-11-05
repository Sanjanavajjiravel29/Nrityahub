package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BatchesListResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("count")
    val count: Int = 0,

    @SerializedName("batches")
    val batches: List<BatchDto> = emptyList()
)

