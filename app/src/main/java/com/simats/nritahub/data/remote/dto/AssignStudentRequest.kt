package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AssignStudentRequest(
    @SerializedName("batch_id")
    val batchId: Int,

    @SerializedName("student_id")
    val studentId: Int
)

