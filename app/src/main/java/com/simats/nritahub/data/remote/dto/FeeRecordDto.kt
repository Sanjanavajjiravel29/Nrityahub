package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FeeRecordDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("record_type")
    val recordType: String,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("user_name")
    val userName: String? = null,

    @SerializedName("user_role")
    val userRole: String? = null,

    @SerializedName("staff_role")
    val staffRole: String? = null,

    @SerializedName("batch_id")
    val batchId: Int? = null,

    @SerializedName("batch_name")
    val batchName: String? = null,

    @SerializedName("title")
    val title: String,

    @SerializedName("amount")
    val amount: String,

    @SerializedName("due_date")
    val dueDate: String? = null,

    @SerializedName("status")
    val status: String,

    @SerializedName("is_paid")
    val isPaid: Boolean = false,

    @SerializedName("created_at")
    val createdAt: String? = null
)

