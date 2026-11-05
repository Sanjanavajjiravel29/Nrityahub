package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BatchDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("batch_name")
    val batchName: String,

    @SerializedName("dance_style")
    val danceStyle: String? = null,

    @SerializedName("level")
    val level: String? = null,

    @SerializedName("instructor_id")
    val instructorId: Int? = null,

    @SerializedName("instructor_name")
    val instructorName: String? = null,

    @SerializedName("instructor_role")
    val instructorRole: String? = null,

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
    val status: String? = null,

    @SerializedName("enrolled_count")
    val enrolledCount: Int? = null,

    @SerializedName("spaces_left")
    val spacesLeft: Int? = null,

    @SerializedName("enrollment_badge")
    val enrollmentBadge: String? = null,

    @SerializedName("schedule")
    val schedule: String? = null,

    @SerializedName("studio_badge")
    val studioBadge: String? = null,

    @SerializedName("student_count_badge")
    val studentCountBadge: String? = null,

    @SerializedName("days_list")
    val daysList: List<String>? = null
)

