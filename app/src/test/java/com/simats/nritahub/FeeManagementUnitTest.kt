package com.simats.nritahub

import com.google.gson.Gson
import com.simats.nritahub.data.remote.dto.CreateFeeRequest
import com.simats.nritahub.data.remote.dto.CreateFeeResponse
import com.simats.nritahub.data.remote.dto.FeeRecordDto
import com.simats.nritahub.data.remote.dto.FeesListResponse
import com.simats.nritahub.data.remote.dto.GenericApiResponse
import com.simats.nritahub.data.remote.dto.UpdateFeeStatusRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FeeManagementUnitTest {

    private val gson = Gson()

    @Test
    fun testFeesListResponseParsing() {
        val json = """
            {
                "success": true,
                "message": "Fee records retrieved successfully.",
                "total_records": 2,
                "student_fees": [
                    {
                        "id": 1,
                        "record_type": "student_fee",
                        "user_id": 20,
                        "user_name": "Ananya Rao",
                        "user_role": "student",
                        "staff_role": null,
                        "batch_id": 1,
                        "batch_name": "Bharatanatyam - Foundation F1",
                        "title": "October Monthly Tuition",
                        "amount": "₹2,400",
                        "due_date": "10 Oct 2026",
                        "status": "Paid",
                        "is_paid": true,
                        "created_at": "2026-11-02 13:18:30"
                    }
                ],
                "staff_salaries": [
                    {
                        "id": 6,
                        "record_type": "staff_salary",
                        "user_id": 11,
                        "user_name": "Meera Iyer",
                        "user_role": "instructor",
                        "staff_role": "Senior Instructor",
                        "batch_id": 1,
                        "batch_name": "Foundation F1 & F2",
                        "title": "October Instructor Payout",
                        "amount": "₹45,000",
                        "due_date": "31 Oct 2026",
                        "status": "Calculated",
                        "is_paid": false,
                        "created_at": "2026-11-02 13:18:30"
                    }
                ],
                "records": []
            }
        """.trimIndent()

        val response = gson.fromJson(json, FeesListResponse::class.java)
        assertTrue(response.success)
        assertEquals(2, response.totalRecords)
        assertEquals(1, response.studentFees.size)
        assertEquals(1, response.staffSalaries.size)

        val studentFee = response.studentFees[0]
        assertEquals(1, studentFee.id)
        assertEquals("student_fee", studentFee.recordType)
        assertEquals("Ananya Rao", studentFee.userName)
        assertEquals("₹2,400", studentFee.amount)
        assertTrue(studentFee.isPaid)
        assertEquals("Paid", studentFee.status)

        val staffSalary = response.staffSalaries[0]
        assertEquals(6, staffSalary.id)
        assertEquals("staff_salary", staffSalary.recordType)
        assertEquals("Meera Iyer", staffSalary.userName)
        assertEquals("₹45,000", staffSalary.amount)
        assertFalse(staffSalary.isPaid)
        assertEquals("Calculated", staffSalary.status)
    }

    @Test
    fun testCreateFeeRequestAndResponseParsing() {
        val request = CreateFeeRequest(
            recordType = "student_fee",
            userId = 20,
            userName = "Ananya Rao",
            title = "Costume & Exam Fee",
            amount = "₹3,500",
            dueDate = "25 Oct 2026",
            status = "Pending"
        )

        val json = gson.toJson(request)
        assertTrue(json.contains("\"record_type\":\"student_fee\""))
        assertTrue(json.contains("\"user_id\":20"))
        assertTrue(json.contains("\"amount\":\"₹3,500\""))
        assertTrue(json.contains("\"status\":\"Pending\""))

        val responseJson = """
            {
                "success": true,
                "message": "Fee record created successfully.",
                "fee_record": {
                    "id": 15,
                    "record_type": "student_fee",
                    "user_id": 20,
                    "user_name": "Ananya Rao",
                    "batch_id": 1,
                    "title": "Costume & Exam Fee",
                    "amount": "₹3,500",
                    "due_date": "25 Oct 2026",
                    "status": "Pending",
                    "is_paid": false
                }
            }
        """.trimIndent()

        val response = gson.fromJson(responseJson, CreateFeeResponse::class.java)
        assertTrue(response.success)
        assertNotNull(response.feeRecord)
        assertEquals(15, response.feeRecord?.id)
        assertEquals("Costume & Exam Fee", response.feeRecord?.title)
        assertEquals("₹3,500", response.feeRecord?.amount)
        assertFalse(response.feeRecord?.isPaid == true)
    }

    @Test
    fun testUpdateFeeStatusRequestAndResponseParsing() {
        val request = UpdateFeeStatusRequest(
            id = 15,
            status = "Paid"
        )
        val json = gson.toJson(request)
        assertTrue(json.contains("\"id\":15"))
        assertTrue(json.contains("\"status\":\"Paid\""))

        val responseJson = """
            {
                "success": true,
                "message": "Fee status updated to Paid."
            }
        """.trimIndent()

        val response = gson.fromJson(responseJson, GenericApiResponse::class.java)
        assertTrue(response.success)
        assertEquals("Fee status updated to Paid.", response.message)
    }
}

