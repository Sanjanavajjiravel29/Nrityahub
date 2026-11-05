package com.simats.nritahub

import com.google.gson.Gson
import com.simats.nritahub.data.remote.dto.BatchAttendanceResponse
import com.simats.nritahub.data.remote.dto.BatchAttendanceStatsDto
import com.simats.nritahub.data.remote.dto.BatchAttendanceStudentDto
import com.simats.nritahub.data.remote.dto.MarkAttendanceRequest
import com.simats.nritahub.data.remote.dto.MarkAttendanceResponse
import com.simats.nritahub.data.remote.dto.MarkStudentAttendanceItem
import com.simats.nritahub.data.remote.dto.StudentAttendanceResponse
import com.simats.nritahub.ui.markattendance.AttendanceBatchInfo
import com.simats.nritahub.ui.markattendance.AttendanceStatus
import com.simats.nritahub.ui.markattendance.AttendanceStudentItem
import com.simats.nritahub.ui.markattendance.MarkAttendanceUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AttendanceManagementUnitTest {

    private val gson = Gson()

    @Test
    fun testBatchAttendanceResponseParsing() {
        val json = """
            {
                "success": true,
                "message": "Batch attendance records fetched successfully.",
                "batch_id": 1,
                "batch_name": "Bharatanatyam - Foundation F1",
                "date": "2026-11-05",
                "is_saved": true,
                "stats": {
                    "present": 4,
                    "absent": 1,
                    "excused": 1,
                    "total": 6
                },
                "students": [
                    {
                        "student_id": 20,
                        "name": "Ananya Rao",
                        "initials": "AR",
                        "avatar_url": "",
                        "status": "Present",
                        "notes": "On time",
                        "attendance_id": 16
                    },
                    {
                        "student_id": 21,
                        "name": "Arjun Varma",
                        "initials": "AV",
                        "avatar_url": "",
                        "status": "Absent",
                        "notes": "Informed leave",
                        "attendance_id": 17
                    }
                ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, BatchAttendanceResponse::class.java)

        assertTrue(response.success)
        assertEquals("Bharatanatyam - Foundation F1", response.batchName)
        assertEquals("2026-11-05", response.date)
        assertTrue(response.isSaved)
        assertNotNull(response.stats)
        assertEquals(4, response.stats?.present)
        assertEquals(1, response.stats?.absent)
        assertEquals(1, response.stats?.excused)
        assertEquals(6, response.stats?.total)

        assertEquals(2, response.students.size)
        val s1 = response.students[0]
        assertEquals(20, s1.studentId)
        assertEquals("Ananya Rao", s1.name)
        assertEquals("Present", s1.status)
        assertEquals(16, s1.attendanceId)
    }

    @Test
    fun testMarkAttendanceRequestSerialization() {
        val request = MarkAttendanceRequest(
            batchId = 1,
            attendanceDate = "2026-11-05",
            students = listOf(
                MarkStudentAttendanceItem(studentId = 20, status = "Present", notes = "Attended"),
                MarkStudentAttendanceItem(studentId = 21, status = "Absent", notes = "Leave")
            )
        )

        val json = gson.toJson(request)
        assertTrue(json.contains("\"batch_id\":1"))
        assertTrue(json.contains("\"attendance_date\":\"2026-11-05\""))
        assertTrue(json.contains("\"student_id\":20"))
        assertTrue(json.contains("\"status\":\"Present\""))
    }

    @Test
    fun testMarkAttendanceResponseParsing() {
        val json = """
            {
                "success": true,
                "message": "Attendance successfully recorded for 3 student(s).",
                "batch_id": 1,
                "date": "2026-11-05",
                "marked_count": 3
            }
        """.trimIndent()

        val response = gson.fromJson(json, MarkAttendanceResponse::class.java)
        assertTrue(response.success)
        assertEquals(1, response.batchId)
        assertEquals(3, response.markedCount)
        assertEquals("2026-11-05", response.date)
    }

    @Test
    fun testStudentAttendanceResponseParsing() {
        val json = """
            {
                "success": true,
                "message": "Student attendance records retrieved successfully.",
                "student_name": "Arjun Varma",
                "batch_name": "Bharatanatyam - Foundation F1",
                "studio": "Studio 2",
                "timing": "6:00 PM - 7:00 PM",
                "instructor": "Meera Iyer",
                "percentage": 92,
                "percentage_text": "92%",
                "present_classes": 12,
                "absent_classes": 1,
                "excused_classes": 0,
                "total_classes": 13,
                "records": [
                    {
                        "id": 1,
                        "attendance_date": "2026-11-02",
                        "status": "Present",
                        "notes": "Good posture",
                        "batch_name": "Bharatanatyam - Foundation F1",
                        "studio": "Studio 2",
                        "timing": "6:00 PM - 7:00 PM",
                        "instructor": "Meera Iyer"
                    }
                ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, StudentAttendanceResponse::class.java)
        assertTrue(response.success)
        assertEquals("Arjun Varma", response.studentName)
        assertEquals(92, response.percentage)
        assertEquals("92%", response.percentageText)
        assertEquals(12, response.presentClasses)
        assertEquals(1, response.absentClasses)
        assertEquals(13, response.totalClasses)
        assertEquals(1, response.records.size)
        assertEquals("Present", response.records[0].status)
    }

    @Test
    fun testMarkAttendanceUiStateDynamicCalculations() {
        val students = listOf(
            AttendanceStudentItem("20", "AR", "Ananya Rao", AttendanceStatus.PRESENT),
            AttendanceStudentItem("21", "AV", "Arjun Varma", AttendanceStatus.PRESENT),
            AttendanceStudentItem("22", "DM", "Diya Menon", AttendanceStatus.ABSENT),
            AttendanceStudentItem("27", "AS", "Aarav Sharma", AttendanceStatus.EXCUSED)
        )

        val state = MarkAttendanceUiState(
            selectedBatchId = "1",
            currentStudents = students
        )

        assertEquals(2, state.presentCount)
        assertEquals(1, state.absentCount)
        assertEquals(1, state.excusedCount)
    }

    @Test
    fun testAttendanceStatusToggling() {
        val initialStudents = listOf(
            AttendanceStudentItem("20", "AR", "Ananya Rao", AttendanceStatus.PRESENT),
            AttendanceStudentItem("21", "AV", "Arjun Varma", AttendanceStatus.PRESENT)
        )

        // Toggle student 21 to ABSENT
        val updatedStudents = initialStudents.map {
            if (it.id == "21") it.copy(status = AttendanceStatus.ABSENT) else it
        }

        val state = MarkAttendanceUiState(currentStudents = updatedStudents)
        assertEquals(1, state.presentCount)
        assertEquals(1, state.absentCount)
        assertEquals(0, state.excusedCount)
        assertEquals(AttendanceStatus.ABSENT, state.currentStudents.first { it.id == "21" }.status)
    }
}

