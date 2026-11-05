package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateFeeResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("fee_record")
    val feeRecord: FeeRecordDto? = null
)

