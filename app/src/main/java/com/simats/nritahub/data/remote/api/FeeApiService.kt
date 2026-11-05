package com.simats.nritahub.data.remote.api

import com.simats.nritahub.data.remote.dto.CreateFeeRequest
import com.simats.nritahub.data.remote.dto.CreateFeeResponse
import com.simats.nritahub.data.remote.dto.FeesListResponse
import com.simats.nritahub.data.remote.dto.GenericApiResponse
import com.simats.nritahub.data.remote.dto.UpdateFeeStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FeeApiService {

    @GET("fees/list_fees.php")
    suspend fun listFees(
        @Query("type") type: String? = null,
        @Query("record_type") recordType: String? = null,
        @Query("user_id") userId: Int? = null,
        @Query("status") status: String? = null
    ): Response<FeesListResponse>

    @POST("fees/create_fee.php")
    suspend fun createFee(
        @Body request: CreateFeeRequest
    ): Response<CreateFeeResponse>

    @POST("fees/update_fee_status.php")
    suspend fun updateFeeStatus(
        @Body request: UpdateFeeStatusRequest
    ): Response<GenericApiResponse>
}

