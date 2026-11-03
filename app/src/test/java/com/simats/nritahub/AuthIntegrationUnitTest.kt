package com.simats.nritahub

import com.google.gson.Gson
import com.simats.nritahub.data.remote.dto.LoginRequest
import com.simats.nritahub.data.remote.dto.LoginResponse
import com.simats.nritahub.data.remote.dto.UserDto
import com.simats.nritahub.data.repository.AuthRepository
import com.simats.nritahub.data.repository.AuthResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthIntegrationUnitTest {

    private val gson = Gson()

    @Test
    fun testLoginResponseParsing_admin() {
        val json = """
            {
                "success": true,
                "message": "Login successful.",
                "user": {
                    "id": 10,
                    "full_name": "Dr. Rukmini Devi",
                    "username": "rukmini.admin",
                    "email": "rukmini.admin@nritahub.com",
                    "role": "admin",
                    "staff_role": "Founder & Chief Administrator",
                    "monthly_salary": "₹65,000 / mo"
                },
                "role": "admin"
            }
        """.trimIndent()

        val response = gson.fromJson(json, LoginResponse::class.java)
        assertTrue(response.success)
        assertEquals("admin", response.role)
        assertEquals(10, response.user?.id)
        assertEquals("Dr. Rukmini Devi", response.user?.fullName)
        assertEquals("Founder & Chief Administrator", response.user?.staffRole)
    }

    @Test
    fun testLoginResponseParsing_studentWithBatch() {
        val json = """
            {
                "success": true,
                "message": "Login successful.",
                "user": {
                    "id": 21,
                    "full_name": "Arjun Varma",
                    "username": "arjun.varma",
                    "email": "arjun.varma@nritahub.com",
                    "role": "student",
                    "batch_id": 1,
                    "batch_name": "Bharatanatyam - Foundation F1",
                    "dance_style": "Bharatanatyam",
                    "level": "Foundation",
                    "days": "Tue, Thu, Sat"
                },
                "role": "student"
            }
        """.trimIndent()

        val response = gson.fromJson(json, LoginResponse::class.java)
        assertTrue(response.success)
        assertEquals("student", response.role)
        assertEquals("Bharatanatyam - Foundation F1", response.user?.batchName)
        assertEquals(1, response.user?.batchId)
    }

    @Test
    fun testLiveApi_adminLogin() = runBlocking {
        val repository = AuthRepository()
        val result = repository.login("rukmini.admin", "Password123!")

        assertTrue("Expected AuthResult.Success for admin", result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals("admin", success.response.role?.lowercase())
        assertEquals("Dr. Rukmini Devi", success.response.user?.fullName)
    }

    @Test
    fun testLiveApi_instructorLogin() = runBlocking {
        val repository = AuthRepository()
        val result = repository.login("meera.iyer", "Password123!")

        assertTrue("Expected AuthResult.Success for instructor", result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals("instructor", success.response.role?.lowercase())
        assertEquals("Meera Iyer", success.response.user?.fullName)
    }

    @Test
    fun testLiveApi_studentLogin() = runBlocking {
        val repository = AuthRepository()
        val result = repository.login("arjun.varma", "Password123!")

        assertTrue("Expected AuthResult.Success for student", result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals("student", success.response.role?.lowercase())
        assertEquals("Arjun Varma", success.response.user?.fullName)
        assertNotNull(success.response.user?.batchName)
    }

    @Test
    fun testLiveApi_invalidCredentials() = runBlocking {
        val repository = AuthRepository()
        val result = repository.login("nonexistent.user", "WrongPassword123!")

        assertTrue("Expected AuthResult.Error for invalid credentials", result is AuthResult.Error)
        val error = result as AuthResult.Error
        assertTrue(
            "Expected invalid credentials message, got: ${error.message}",
            error.message.contains("Invalid", ignoreCase = true)
        )
    }
}

