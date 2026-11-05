package com.simats.nritahub

import com.google.gson.Gson
import com.simats.nritahub.data.remote.dto.AdminResetPasswordRequest
import com.simats.nritahub.data.remote.dto.CreateUserRequest
import com.simats.nritahub.data.remote.dto.CreateUserResponse
import com.simats.nritahub.data.remote.dto.GenericApiResponse
import com.simats.nritahub.data.remote.dto.UserDto
import com.simats.nritahub.data.remote.dto.UsersListResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AdminUserManagementUnitTest {

    private val gson = Gson()

    @Test
    fun testUsersListResponseParsing() {
        val json = """
            {
                "success": true,
                "message": "Users fetched successfully.",
                "count": 2,
                "users": [
                    {
                        "id": 20,
                        "full_name": "Ananya Rao",
                        "username": "ananya.rao",
                        "email": "ananya.rao@nritahub.com",
                        "role": "student",
                        "staff_role": null,
                        "monthly_salary": null,
                        "batch_id": 2,
                        "status": "active",
                        "batch_name": "Bharatanatyam - Foundation F2",
                        "initials": "AR"
                    },
                    {
                        "id": 11,
                        "full_name": "Meera Iyer",
                        "username": "meera.iyer",
                        "email": "meera.iyer@nritahub.com",
                        "role": "instructor",
                        "staff_role": "Senior Bharatanatyam Instructor",
                        "monthly_salary": "₹45,000 / mo",
                        "batch_id": 1,
                        "status": "active",
                        "batch_name": "Bharatanatyam - Foundation F1",
                        "initials": "MI"
                    }
                ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, UsersListResponse::class.java)
        assertTrue(response.success)
        assertEquals(2, response.count)
        assertEquals(2, response.users.size)

        val student = response.users[0]
        assertEquals("Ananya Rao", student.fullName)
        assertEquals("student", student.role)
        assertEquals("AR", student.initials)
        assertEquals("Bharatanatyam - Foundation F2", student.batchName)

        val instructor = response.users[1]
        assertEquals("Meera Iyer", instructor.fullName)
        assertEquals("instructor", instructor.role)
        assertEquals("Senior Bharatanatyam Instructor", instructor.staffRole)
        assertEquals("₹45,000 / mo", instructor.monthlySalary)
    }

    @Test
    fun testCreateUserRequestAndResponseParsing() {
        val request = CreateUserRequest(
            fullName = "Kavya Menon",
            username = "kavya.menon",
            email = "kavya.menon@nritahub.com",
            password = "SecurePassword123!",
            role = "student",
            batchName = "Bharatanatyam - Foundation F1"
        )

        val requestJson = gson.toJson(request)
        assertTrue(requestJson.contains("kavya.menon"))
        assertTrue(requestJson.contains("SecurePassword123!"))
        assertTrue(requestJson.contains("student"))

        val responseJson = """
            {
                "success": true,
                "message": "User account created successfully.",
                "user": {
                    "id": 35,
                    "full_name": "Kavya Menon",
                    "username": "kavya.menon",
                    "email": "kavya.menon@nritahub.com",
                    "role": "student",
                    "batch_name": "Bharatanatyam - Foundation F1",
                    "initials": "KM",
                    "status": "active"
                }
            }
        """.trimIndent()

        val response = gson.fromJson(responseJson, CreateUserResponse::class.java)
        assertTrue(response.success)
        assertNotNull(response.user)
        assertEquals(35, response.user?.id)
        assertEquals("Kavya Menon", response.user?.fullName)
        assertEquals("KM", response.user?.initials)
    }

    @Test
    fun testAdminResetPasswordRequestAndResponseParsing() {
        val request = AdminResetPasswordRequest(
            userId = 20,
            newPassword = "NewStudentPass123!"
        )
        val requestJson = gson.toJson(request)
        assertTrue(requestJson.contains("\"user_id\":20"))
        assertTrue(requestJson.contains("NewStudentPass123!"))

        val responseJson = """
            {
                "success": true,
                "message": "User password has been successfully reset by administrator and active sessions revoked."
            }
        """.trimIndent()

        val response = gson.fromJson(responseJson, GenericApiResponse::class.java)
        assertTrue(response.success)
        assertTrue(response.message.contains("successfully reset"))
    }
}

