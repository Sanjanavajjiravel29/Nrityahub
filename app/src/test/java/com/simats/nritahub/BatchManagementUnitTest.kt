package com.simats.nritahub

import com.google.gson.Gson
import com.simats.nritahub.data.remote.dto.AssignStudentRequest
import com.simats.nritahub.data.remote.dto.BatchDetailsResponse
import com.simats.nritahub.data.remote.dto.BatchDto
import com.simats.nritahub.data.remote.dto.BatchesListResponse
import com.simats.nritahub.data.remote.dto.CreateBatchRequest
import com.simats.nritahub.data.remote.dto.UpdateBatchRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BatchManagementUnitTest {

    private val gson = Gson()

    @Test
    fun testBatchesListResponseParsing() {
        val json = """
            {
                "success": true,
                "message": "Batches fetched successfully.",
                "count": 2,
                "batches": [
                    {
                        "id": 1,
                        "batch_name": "Bharatanatyam - Foundation F1",
                        "dance_style": "Bharatanatyam",
                        "level": "Foundation",
                        "instructor_id": 11,
                        "instructor_name": "Meera Iyer",
                        "capacity": 24,
                        "studio": "Studio 2",
                        "days": "Tue, Thu, Sat",
                        "start_time": "6:00 PM",
                        "end_time": "7:00 PM",
                        "monthly_tuition": "₹2,400",
                        "status": "Active",
                        "enrolled_count": 3,
                        "spaces_left": 21,
                        "enrollment_badge": "3 enrolled · 21 spaces",
                        "schedule": "Tue, Thu, Sat · 6:00 PM–7:00 PM"
                    },
                    {
                        "id": 2,
                        "batch_name": "Bharatanatyam - Foundation F2",
                        "dance_style": "Bharatanatyam",
                        "level": "Foundation",
                        "instructor_id": 4,
                        "instructor_name": "Lakshmi Narayanan",
                        "capacity": 20,
                        "studio": "Studio 1",
                        "days": "Tue, Thu, Sat",
                        "start_time": "4:30 PM",
                        "end_time": "5:30 PM",
                        "monthly_tuition": "₹2,400",
                        "status": "Active",
                        "enrolled_count": 5,
                        "spaces_left": 15,
                        "enrollment_badge": "5 enrolled · 15 spaces",
                        "schedule": "Tue, Thu, Sat · 4:30 PM–5:30 PM"
                    }
                ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, BatchesListResponse::class.java)
        assertTrue(response.success)
        assertEquals(2, response.count)
        assertEquals(2, response.batches.size)

        val batch1 = response.batches[0]
        assertEquals(1, batch1.id)
        assertEquals("Bharatanatyam - Foundation F1", batch1.batchName)
        assertEquals("Meera Iyer", batch1.instructorName)
        assertEquals(3, batch1.enrolledCount)
        assertEquals(21, batch1.spacesLeft)
        assertEquals("Studio 2", batch1.studio)

        val batch2 = response.batches[1]
        assertEquals(2, batch2.id)
        assertEquals("Bharatanatyam - Foundation F2", batch2.batchName)
        assertEquals("Lakshmi Narayanan", batch2.instructorName)
        assertEquals(5, batch2.enrolledCount)
    }

    @Test
    fun testBatchDetailsResponseParsing() {
        val json = """
            {
                "success": true,
                "message": "Batch details retrieved successfully.",
                "batch": {
                    "id": 1,
                    "batch_name": "Bharatanatyam - Foundation F1",
                    "dance_style": "Bharatanatyam",
                    "level": "Foundation",
                    "instructor_id": 11,
                    "capacity": 24,
                    "studio": "Studio 2",
                    "days": "Tue, Thu, Sat",
                    "start_time": "6:00 PM",
                    "end_time": "7:00 PM",
                    "status": "Active"
                },
                "enrolled_count": 1,
                "students": [
                    {
                        "id": 27,
                        "full_name": "Aarav Sharma",
                        "username": "aaravsharma861",
                        "email": "aarav.sharma@nritahub.com",
                        "role": "student",
                        "initials": "AS",
                        "status": "active"
                    }
                ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, BatchDetailsResponse::class.java)
        assertTrue(response.success)
        assertNotNull(response.batch)
        assertEquals(1, response.batch?.id)
        assertEquals("Bharatanatyam - Foundation F1", response.batch?.batchName)
        assertEquals(1, response.enrolledCount)
        assertEquals(1, response.students.size)
        assertEquals("Aarav Sharma", response.students[0].fullName)
        assertEquals("AS", response.students[0].initials)
    }

    @Test
    fun testCreateAndUpdateBatchRequestsSerialization() {
        val createReq = CreateBatchRequest(
            batchName = "Varnam Mastery Batch",
            danceStyle = "Bharatanatyam",
            level = "Advanced",
            instructorName = "Meera Iyer",
            capacity = 15,
            studio = "Studio 3",
            days = "Mon, Wed, Fri",
            startTime = "5:00 PM",
            endTime = "6:30 PM",
            monthlyTuition = "₹3,200",
            status = "Active"
        )
        val createJson = gson.toJson(createReq)
        assertTrue(createJson.contains("Varnam Mastery Batch"))
        assertTrue(createJson.contains("Studio 3"))
        assertTrue(createJson.contains("15"))

        val updateReq = UpdateBatchRequest(
            id = 5,
            batchName = "Varnam Mastery Batch Updated",
            capacity = 18,
            studio = "Studio 2"
        )
        val updateJson = gson.toJson(updateReq)
        assertTrue(updateJson.contains("\"id\":5"))
        assertTrue(updateJson.contains("Varnam Mastery Batch Updated"))
        assertTrue(updateJson.contains("18"))

        val assignReq = AssignStudentRequest(batchId = 1, studentId = 20)
        val assignJson = gson.toJson(assignReq)
        assertTrue(assignJson.contains("\"batch_id\":1"))
        assertTrue(assignJson.contains("\"student_id\":20"))
    }
}

