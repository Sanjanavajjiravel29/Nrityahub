package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FeesListResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("total_records")
    val totalRecords: Int = 0,

    @SerializedName("student_fees")
    val studentFees: List<FeeRecordDto> = emptyList(),

    @SerializedName("staff_salaries")
    val staffSalaries: List<FeeRecordDto> = emptyList(),

    @SerializedName("records")
    val records: List<FeeRecordDto> = emptyList()
)

