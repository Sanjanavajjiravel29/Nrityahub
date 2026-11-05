package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateBatchRequest(
    @SerializedName("batch_name")
    val batchName: String,

    @SerializedName("dance_style")
    val danceStyle: String = "Bharatanatyam",

    @SerializedName("level")
    val level: String = "Foundation",

    @SerializedName("instructor_id")
    val instructorId: Int? = null,

    @SerializedName("instructor_name")
    val instructorName: String? = null,

    @SerializedName("capacity")
    val capacity: Int = 24,

    @SerializedName("studio")
    val studio: String = "Studio 1",

    @SerializedName("days")
    val days: String = "Tue, Thu, Sat",

    @SerializedName("start_time")
    val startTime: String = "6:00 PM",

    @SerializedName("end_time")
    val endTime: String = "7:00 PM",

    @SerializedName("monthly_tuition")
    val monthlyTuition: String = "₹2,400",

    @SerializedName("status")
    val status: String = "Active"
)

