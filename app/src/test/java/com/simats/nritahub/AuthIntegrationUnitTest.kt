package com.simats.nritahub

import com.google.gson.Gson
import com.simats.nritahub.data.remote.dto.AdminDashboardStatsResponse
import com.simats.nritahub.data.remote.dto.LoginResponse
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
    fun testLoginResponseParsing_adminWithToken() {
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
                "role": "admin",
                "token": "a1b2c3d4e5f60718293a4b5c6d7e8f90a1b2c3d4e5f60718293a4b5c6d7e8f90",
                "expires_at": "2026-12-03 14:00:00"
            }
        """.trimIndent()

        val response = gson.fromJson(json, LoginResponse::class.java)
        assertTrue(response.success)
        assertEquals("admin", response.role)
        assertEquals(10, response.user?.id)
        assertEquals("Dr. Rukmini Devi", response.user?.fullName)
        assertEquals("Founder & Chief Administrator", response.user?.staffRole)
        assertEquals(64, response.token?.length)
        assertNotNull(response.expiresAt)
    }

    @Test
    fun testAdminDashboardStatsResponseParsing() {
        val json = """
            {
                "success": true,
                "message": "Admin dashboard statistics retrieved successfully.",
                "overview_badge": "ADMIN CONTROL PANEL",
                "enrolled_students_count": "156",
                "active_staff_count": "6",
                "active_batches_count": "8",
                "calculated_salary_total": "₹1,80,000",
                "fee_collection": {
                    "title": "November fee collection",
                    "fee_percentage_text": "71% collected",
                    "total_collected_amount": "₹1,32,000",
                    "total_billed_text": "of ₹1,87,200 billed · 78 invoices",
                    "collection_progress": 0.71,
                    "pending_amount_text": "Pending ₹43,200",
                    "overdue_amount_text": "Overdue ₹12,000"
                },
                "quick_controls": [
                    {
                        "title": "Manage Batches & Name Lists",
                        "subtitle": "Student & staff rosters",
                        "action": "batches"
                    }
                ],
                "recital_notice": "Program Announcement: Autumn Recital 2026"
            }
        """.trimIndent()

        val response = gson.fromJson(json, AdminDashboardStatsResponse::class.java)
        assertTrue(response.success)
        assertEquals("ADMIN CONTROL PANEL", response.overviewBadge)
        assertEquals("156", response.enrolledStudentsCount)
        assertEquals("6", response.activeStaffCount)
        assertEquals("8", response.activeBatchesCount)
        assertEquals("₹1,80,000", response.calculatedSalaryTotal)
        assertEquals("November fee collection", response.feeCollection?.title)
        assertEquals(0.71f, response.feeCollection?.collectionProgress ?: 0f, 0.001f)
        assertEquals(1, response.quickControls?.size)
    }

    @Test
    fun testLiveApi_adminLoginIssuesToken() = runBlocking {
        val repository = AuthRepository()
        val result = repository.login("rukmini.admin", "Password123!")

        assertTrue("Expected AuthResult.Success for admin", result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals("admin", success.response.role?.lowercase())
        assertEquals("Dr. Rukmini Devi", success.response.user?.fullName)
        assertNotNull("Admin login must return a token", success.response.token)
        assertEquals("Token should be 64-char hex", 64, success.response.token?.length)
    }

    @Test
    fun testLiveApi_instructorLoginIssuesToken() = runBlocking {
        val repository = AuthRepository()
        val result = repository.login("meera.iyer", "Password123!")

        assertTrue("Expected AuthResult.Success for instructor", result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals("instructor", success.response.role?.lowercase())
        assertEquals("Meera Iyer", success.response.user?.fullName)
        assertNotNull("Instructor login must return a token", success.response.token)
        assertEquals("Token should be 64-char hex", 64, success.response.token?.length)
    }

    @Test
    fun testLiveApi_studentLoginIssuesToken() = runBlocking {
        val repository = AuthRepository()
        val result = repository.login("arjun.varma", "Password123!")

        assertTrue("Expected AuthResult.Success for student", result is AuthResult.Success)
        val success = result as AuthResult.Success
        assertEquals("student", success.response.role?.lowercase())
        assertEquals("Arjun Varma", success.response.user?.fullName)
        assertNotNull("Student login must return a token", success.response.token)
        assertEquals("Token should be 64-char hex", 64, success.response.token?.length)
        assertNotNull(success.response.user?.batchName)
    }

    @Test
    fun testLiveApi_invalidCredentialsRejected() = runBlocking {
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
