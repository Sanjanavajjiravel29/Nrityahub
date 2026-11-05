package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateFeeStatusRequest(
    @SerializedName("id")
    val id: Int,

    @SerializedName("status")
    val status: String
)

