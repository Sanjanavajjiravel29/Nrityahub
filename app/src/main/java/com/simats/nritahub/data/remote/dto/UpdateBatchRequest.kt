package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateBatchRequest(
    @SerializedName("id")
    val id: Int,

    @SerializedName("batch_name")
    val batchName: String? = null,

    @SerializedName("dance_style")
    val danceStyle: String? = null,

    @SerializedName("level")
    val level: String? = null,

    @SerializedName("instructor_id")
    val instructorId: Int? = null,

    @SerializedName("instructor_name")
    val instructorName: String? = null,

    @SerializedName("capacity")
    val capacity: Int? = null,

    @SerializedName("studio")
    val studio: String? = null,

    @SerializedName("days")
    val days: String? = null,

    @SerializedName("start_time")
    val startTime: String? = null,

    @SerializedName("end_time")
    val endTime: String? = null,

    @SerializedName("monthly_tuition")
    val monthlyTuition: String? = null,

    @SerializedName("status")
    val status: String? = null
)

