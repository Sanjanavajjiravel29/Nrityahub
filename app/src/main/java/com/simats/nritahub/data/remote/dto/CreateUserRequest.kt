package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Request payload for Admin user creation (student or instructor).
 */
data class CreateUserRequest(
    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("mobile")
    val mobile: String? = null,

    @SerializedName("password")
    val password: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("batch_id")
    val batchId: Int? = null,

    @SerializedName("batch_name")
    val batchName: String? = null,

    @SerializedName("staff_role")
    val staffRole: String? = null,

    @SerializedName("monthly_salary")
    val monthlySalary: String? = null,

    @SerializedName("bio")
    val bio: String? = null
)

