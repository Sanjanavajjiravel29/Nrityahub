package com.simats.nritahub.data.session

/**
 * Immutable model representing an active authenticated user session.
 * Contains user identification, role information, and the cryptographic Bearer authentication token.
 * Passwords are strictly NOT stored.
 */
data class UserSession(
    val id: Int,
    val fullName: String,
    val email: String,
    val username: String?,
    val role: String,
    val authToken: String? = null,
    val expiresAt: String? = null,
    val staffRole: String? = null,
    val monthlySalary: String? = null,
    val batchId: Int? = null,
    val batchName: String? = null,
    val bio: String? = null,
    val profileImageUrl: String? = null
)
