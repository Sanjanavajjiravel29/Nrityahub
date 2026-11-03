package com.simats.nritahub.data.session

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages local persistence of the logged-in user's profile, role, and Bearer authentication token.
 *
 * Architecture Note on Authentication:
 * Tokens issued by the backend are cryptographically random 64-character tokens.
 * Passwords are never saved or stored.
 */
class UserSessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "nrityahub_user_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_FULL_NAME = "full_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_USERNAME = "username"
        private const val KEY_ROLE = "role"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_EXPIRES_AT = "expires_at"
        private const val KEY_STAFF_ROLE = "staff_role"
        private const val KEY_MONTHLY_SALARY = "monthly_salary"
        private const val KEY_BATCH_ID = "batch_id"
        private const val KEY_BATCH_NAME = "batch_name"
        private const val KEY_BIO = "bio"
        private const val KEY_PROFILE_IMAGE_URL = "profile_image_url"

        @Volatile
        private var instance: UserSessionManager? = null

        fun getInstance(context: Context): UserSessionManager {
            return instance ?: synchronized(this) {
                instance ?: UserSessionManager(context.applicationContext).also { instance = it }
            }
        }

        /**
         * Access token provider for OkHttp Authenticator/Interceptor without needing a Context reference.
         */
        fun getActiveToken(): String? {
            return instance?.getAuthToken()
        }
    }

    /**
     * Saves authenticated user session details upon successful login.
     * Does not store passwords.
     */
    fun saveSession(
        id: Int,
        fullName: String,
        email: String,
        username: String? = null,
        role: String,
        authToken: String? = null,
        expiresAt: String? = null,
        staffRole: String? = null,
        monthlySalary: String? = null,
        batchId: Int? = null,
        batchName: String? = null,
        bio: String? = null,
        profileImageUrl: String? = null
    ) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putInt(KEY_USER_ID, id)
            putString(KEY_FULL_NAME, fullName)
            putString(KEY_EMAIL, email)
            putString(KEY_USERNAME, username)
            putString(KEY_ROLE, role.lowercase().trim())
            putString(KEY_AUTH_TOKEN, authToken)
            putString(KEY_EXPIRES_AT, expiresAt)
            putString(KEY_STAFF_ROLE, staffRole)
            putString(KEY_MONTHLY_SALARY, monthlySalary)
            if (batchId != null) putInt(KEY_BATCH_ID, batchId) else remove(KEY_BATCH_ID)
            putString(KEY_BATCH_NAME, batchName)
            putString(KEY_BIO, bio)
            putString(KEY_PROFILE_IMAGE_URL, profileImageUrl)
            apply()
        }
    }

    /**
     * Checks whether an active user session exists.
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && getUserId() > 0
    }

    /**
     * Returns the active Bearer authentication token.
     */
    fun getAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    /**
     * Returns the token expiration datetime string.
     */
    fun getExpiresAt(): String? {
        return prefs.getString(KEY_EXPIRES_AT, null)
    }

    /**
     * Returns the current logged-in user's role ("admin", "instructor", "student").
     */
    fun getUserRole(): String? {
        return prefs.getString(KEY_ROLE, null)
    }

    fun isAdmin(): Boolean = getUserRole().equals("admin", ignoreCase = true)
    fun isInstructor(): Boolean = getUserRole().equals("instructor", ignoreCase = true)
    fun isStudent(): Boolean = getUserRole().equals("student", ignoreCase = true)

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)
    fun getFullName(): String? = prefs.getString(KEY_FULL_NAME, null)
    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)
    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    /**
     * Retrieves the complete stored session. Returns null if no user is logged in.
     */
    fun getSession(): UserSession? {
        if (!isLoggedIn()) return null
        return UserSession(
            id = getUserId(),
            fullName = getFullName().orEmpty(),
            email = getEmail().orEmpty(),
            username = getUsername(),
            role = getUserRole().orEmpty(),
            authToken = getAuthToken(),
            expiresAt = getExpiresAt(),
            staffRole = prefs.getString(KEY_STAFF_ROLE, null),
            monthlySalary = prefs.getString(KEY_MONTHLY_SALARY, null),
            batchId = if (prefs.contains(KEY_BATCH_ID)) prefs.getInt(KEY_BATCH_ID, -1) else null,
            batchName = prefs.getString(KEY_BATCH_NAME, null),
            bio = prefs.getString(KEY_BIO, null),
            profileImageUrl = prefs.getString(KEY_PROFILE_IMAGE_URL, null)
        )
    }

    /**
     * Clears all session and token data upon user logout.
     */
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
