package com.simats.nritahub.data.remote.api

import com.simats.nritahub.data.remote.dto.AdminDashboardStatsResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit service definition for Admin-exclusive endpoints.
 * All requests are automatically signed with the Admin Bearer token via RetrofitClient.
 */
interface AdminApiService {

    @GET("admin/dashboard_stats.php")
    suspend fun getDashboardStats(): Response<AdminDashboardStatsResponse>
}

