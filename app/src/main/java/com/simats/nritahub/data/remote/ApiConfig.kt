package com.simats.nritahub.data.remote

/**
 * Central networking configuration for NrityaHub.
 * Keeps the API base URL and network timeout constants in a single place
 * to facilitate easy environment switching (e.g. ngrok tunnel vs local LAN vs production).
 */
object ApiConfig {

    /**
     * Current base URL for backend API requests.
     * Note: Retrofit requires the base URL string to end with a trailing slash '/'.
     */
    const val BASE_URL: String = "https://headcount-briskly-mockup.ngrok-free.dev/nrityahub_backend/"

    /**
     * Network timeout durations in seconds.
     * Configured with 30s to comfortably handle ngrok tunnel latency and media uploads.
     */
    const val CONNECT_TIMEOUT_SECONDS: Long = 30L
    const val READ_TIMEOUT_SECONDS: Long = 30L
    const val WRITE_TIMEOUT_SECONDS: Long = 30L
}

