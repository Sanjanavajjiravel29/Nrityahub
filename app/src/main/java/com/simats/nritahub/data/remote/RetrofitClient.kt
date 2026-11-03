package com.simats.nritahub.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.simats.nritahub.BuildConfig
import com.simats.nritahub.data.session.UserSessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Central Retrofit and OkHttpClient factory for NrityaHub.
 *
 * Configures:
 * - Central base URL from [ApiConfig].
 * - Connection, read, and write timeouts.
 * - Authorization Interceptor: Automatically attaches `Authorization: Bearer <token>` when a token exists.
 * - ngrok Interceptor: Attaches `ngrok-skip-browser-warning: true` to bypass interstitial page.
 * - Logging enabled for development (debug) builds only via [BuildConfig.DEBUG].
 * - Redacts sensitive headers (Authorization, Cookie) to prevent logging private credentials/tokens.
 * - Lenient Gson parser for robust backend response handling.
 */
object RetrofitClient {

    private val gson: Gson by lazy {
        GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }

    /**
     * Interceptor to inject the Authorization Bearer header and ngrok bypass headers.
     */
    private val authInterceptor: Interceptor by lazy {
        Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("ngrok-skip-browser-warning", "true")

            val activeToken = UserSessionManager.getActiveToken()
            if (!activeToken.isNullOrBlank() && originalRequest.header("Authorization") == null) {
                requestBuilder.header("Authorization", "Bearer $activeToken")
            }

            chain.proceed(requestBuilder.build())
        }
    }

    private val loggingInterceptor: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            redactHeader("Authorization")
            redactHeader("Cookie")
        }
    }

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(ApiConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(ApiConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(ApiConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
        }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * Creates an implementation of the API endpoints defined by the [serviceClass] interface.
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }

    /**
     * Inline reified helper for cleaner service creation:
     * e.g. `val authApi = RetrofitClient.createService<AuthApiService>()`
     */
    inline fun <reified T> createService(): T {
        return createService(T::class.java)
    }
}
