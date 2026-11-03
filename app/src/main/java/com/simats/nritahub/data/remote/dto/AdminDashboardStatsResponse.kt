package com.simats.nritahub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FeeCollectionDto(
    @SerializedName("title")
    val title: String? = null,

    @SerializedName("fee_percentage_text")
    val feePercentageText: String? = null,

    @SerializedName("total_collected_amount")
    val totalCollectedAmount: String? = null,

    @SerializedName("total_billed_text")
    val totalBilledText: String? = null,

    @SerializedName("collection_progress")
    val collectionProgress: Float? = null,

    @SerializedName("pending_amount_text")
    val pendingAmountText: String? = null,

    @SerializedName("overdue_amount_text")
    val overdueAmountText: String? = null
)

data class QuickControlDto(
    @SerializedName("title")
    val title: String,

    @SerializedName("subtitle")
    val subtitle: String,

    @SerializedName("action")
    val action: String
)

data class AdminDashboardStatsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("overview_badge")
    val overviewBadge: String? = null,

    @SerializedName("enrolled_students_count")
    val enrolledStudentsCount: String? = null,

    @SerializedName("active_staff_count")
    val activeStaffCount: String? = null,

    @SerializedName("active_batches_count")
    val activeBatchesCount: String? = null,

    @SerializedName("calculated_salary_total")
    val calculatedSalaryTotal: String? = null,

    @SerializedName("fee_collection")
    val feeCollection: FeeCollectionDto? = null,

    @SerializedName("quick_controls")
    val quickControls: List<QuickControlDto>? = null,

    @SerializedName("recital_notice")
    val recitalNotice: String? = null
)

