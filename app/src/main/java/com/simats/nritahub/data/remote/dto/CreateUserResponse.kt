package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Response returned by users/create_user.php upon creating a student or instructor.
 */
data class CreateUserResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("user")
    val user: UserDto? = null
)

