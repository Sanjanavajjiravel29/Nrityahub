package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateFeeRequest(
    @SerializedName("record_type")
    val recordType: String = "student_fee",

    @SerializedName("user_id")
    val userId: Int? = null,

    @SerializedName("user_identifier")
    val userIdentifier: String? = null,

    @SerializedName("user_name")
    val userName: String? = null,

    @SerializedName("batch_id")
    val batchId: Int? = null,

    @SerializedName("title")
    val title: String,

    @SerializedName("amount")
    val amount: String,

    @SerializedName("due_date")
    val dueDate: String? = null,

    @SerializedName("status")
    val status: String = "Pending"
)

