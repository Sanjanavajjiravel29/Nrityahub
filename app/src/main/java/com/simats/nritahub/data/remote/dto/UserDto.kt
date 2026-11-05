package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data transfer object representing the authenticated user profile
 * and optional enrolled batch information returned by the backend.
 */
data class UserDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("mobile")
    val mobile: String? = null,

    @SerializedName("email")
    val email: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("staff_role")
    val staffRole: String? = null,

    @SerializedName("monthly_salary")
    val monthlySalary: String? = null,

    @SerializedName("batch_id")
    val batchId: Int? = null,

    @SerializedName("bio")
    val bio: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("profile_image")
    val profileImage: String? = null,

    @SerializedName("profile_image_url")
    val profileImageUrl: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("batch_name")
    val batchName: String? = null,

    @SerializedName("dance_style")
    val danceStyle: String? = null,

    @SerializedName("level")
    val level: String? = null,

    @SerializedName("studio")
    val studio: String? = null,

    @SerializedName("days")
    val days: String? = null,

    @SerializedName("start_time")
    val startTime: String? = null,

    @SerializedName("end_time")
    val endTime: String? = null,

    @SerializedName("initials")
    val initials: String? = null,

    @SerializedName("attendance_percentage")
    val attendancePercentage: String? = null,

    @SerializedName("level_and_attendance")
    val levelAndAttendance: String? = null
)
