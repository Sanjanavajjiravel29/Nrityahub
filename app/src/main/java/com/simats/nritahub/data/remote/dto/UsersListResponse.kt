package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Response returned by users/list_users.php containing user records.
 */
data class UsersListResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("count")
    val count: Int = 0,

    @SerializedName("users")
    val users: List<UserDto> = emptyList()
)

