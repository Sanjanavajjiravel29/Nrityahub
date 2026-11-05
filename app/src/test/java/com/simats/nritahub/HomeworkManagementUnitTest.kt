package com.simats.nritahub

import com.google.gson.Gson
import com.simats.nritahub.data.remote.dto.ChecklistItemDto
import com.simats.nritahub.data.remote.dto.CreateHomeworkRequest
import com.simats.nritahub.data.remote.dto.HomeworkItemDto
import com.simats.nritahub.data.remote.dto.HomeworkListResponse
import com.simats.nritahub.data.remote.dto.ReviewSubmissionRequest
import com.simats.nritahub.data.remote.dto.SubmissionDetailsResponse
import com.simats.nritahub.data.remote.dto.SubmissionsListResponse
import com.simats.nritahub.data.remote.dto.SubmitHomeworkJsonRequest
import com.simats.nritahub.ui.review.ChecklistItem
import com.simats.nritahub.ui.review.ReviewPracticeUiState
import com.simats.nritahub.ui.submit.SubmitAssignmentUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeworkManagementUnitTest {

    private val gson = Gson()

    @Test
    fun testHomeworkListResponseParsing() {
        val json = """
            {
                "success": true,
                "count": 2,
                "homework": [
                    {
                        "id": 1,
                        "batch_id": 1,
                        "instructor_id": 11,
                        "title": "Tatta Adavu Speed Sets 1-4",
                        "topic": "Tatta Adavu",
                        "description": "Record steady aramandi execution in 3 speeds.",
                        "due_date": "2026-11-15",
                        "reference_media_url": "media/tatta_adavu.mp4",
                        "batch_name": "Bharatanatyam - Foundation F1",
                        "instructor_name": "Meera Iyer",
                        "is_submitted": true,
                        "my_submission": {
                            "id": 1,
                            "status": "Reviewed",
                            "video_url": "uploads/submissions/ananya_tatta.mp4",
                            "submitted_at": "2026-11-04 18:30:00"
                        }
                    },
                    {
                        "id": 2,
                        "batch_id": 1,
                        "instructor_id": 11,
                        "title": "Natta Adavu Practice",
                        "topic": "Natta Adavu",
                        "description": "Focus on stretch and sitting posture.",
                        "due_date": "2026-11-20",
                        "is_submitted": false,
                        "my_submission": null
                    }
                ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, HomeworkListResponse::class.java)

        assertTrue(response.success)
        assertEquals(2, response.count)
        assertEquals(2, response.homework.size)

        val hw1 = response.homework[0]
        assertEquals(1, hw1.id)
        assertEquals("Tatta Adavu Speed Sets 1-4", hw1.title)
        assertEquals("Tatta Adavu", hw1.topic)
        assertEquals("Meera Iyer", hw1.instructorName)
        assertEquals(true, hw1.isSubmitted)
        assertNotNull(hw1.mySubmission)
        assertEquals(1, hw1.mySubmission?.id)
        assertEquals("Reviewed", hw1.mySubmission?.status)

        val hw2 = response.homework[1]
        assertEquals(2, hw2.id)
        assertEquals("Natta Adavu Practice", hw2.title)
        assertEquals(false, hw2.isSubmitted)
        assertNull(hw2.mySubmission)
    }

    @Test
    fun testCreateHomeworkRequestSerialization() {
        val request = CreateHomeworkRequest(
            batchId = 1,
            title = "Kuditta Mettu Step 1",
            topic = "Kuditta Mettu",
            description = "Maintain rhythm on tat-tai-ta-ha.",
            dueDate = "2026-11-25"
        )

        val json = gson.toJson(request)
        assertTrue(json.contains("\"batch_id\":1"))
        assertTrue(json.contains("\"title\":\"Kuditta Mettu Step 1\""))
        assertTrue(json.contains("\"topic\":\"Kuditta Mettu\""))
        assertTrue(json.contains("\"due_date\":\"2026-11-25\""))
    }

    @Test
    fun testSubmissionsListResponseParsing() {
        val json = """
            {
                "success": true,
                "count": 1,
                "submissions": [
                    {
                        "id": 5,
                        "homework_id": 1,
                        "batch_id": 1,
                        "student_id": 20,
                        "student_name": "Ananya Rao",
                        "student_initials": "AR",
                        "batch_name": "Bharatanatyam - Foundation F1",
                        "topic": "Tatta Adavu",
                        "file_name": "ananya_practice.mp4",
                        "file_details": "01:20 · 18 MB",
                        "status": "Pending",
                        "submitted_at": "2026-11-05 08:30:00",
                        "has_review": false
                    }
                ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, SubmissionsListResponse::class.java)

        assertTrue(response.success)
        assertEquals(1, response.count)
        assertEquals(1, response.submissions.size)

        val sub = response.submissions[0]
        assertEquals(5, sub.id)
        assertEquals("Ananya Rao", sub.studentName)
        assertEquals("AR", sub.studentInitials)
        assertEquals("Tatta Adavu", sub.topic)
        assertEquals("Pending", sub.status)
        assertEquals(false, sub.hasReview)
    }

    @Test
    fun testSubmissionDetailsWithChecklistParsing() {
        val json = """
            {
                "success": true,
                "submission": {
                    "id": 1,
                    "homework_id": 1,
                    "student_id": 20,
                    "student_name": "Ananya Rao",
                    "student_initials": "AR",
                    "batch_name": "Bharatanatyam - Foundation F1",
                    "topic": "Tatta Adavu",
                    "student_note": "Felt better in second speed.",
                    "status": "Reviewed",
                    "submitted_at": "2026-11-04 18:30:00",
                    "reviewer_name": "Meera Iyer",
                    "feedback": "Excellent posture. Keep shoulders relaxed.",
                    "next_step": "Practice araimandi 2 mins daily.",
                    "has_review": true,
                    "checklist_items": [
                        {
                            "id": "1",
                            "title": "Anga Shuddham",
                            "description": "Body posture and clarity of form",
                            "isChecked": true
                        },
                        {
                            "id": "2",
                            "title": "Aramandi",
                            "description": "Proper half-sitting stance maintenance",
                            "isChecked": true
                        },
                        {
                            "id": "4",
                            "title": "Bhavam",
                            "description": "Expressive facial and emotional engagement",
                            "isChecked": false
                        }
                    ]
                }
            }
        """.trimIndent()

        val response = gson.fromJson(json, SubmissionDetailsResponse::class.java)

        assertTrue(response.success)
        assertNotNull(response.submission)

        val sub = response.submission!!
        assertEquals(1, sub.id)
        assertEquals("Ananya Rao", sub.studentName)
        assertEquals("Meera Iyer", sub.reviewerName)
        assertEquals("Reviewed", sub.status)
        assertEquals(true, sub.hasReview)
        assertEquals("Excellent posture. Keep shoulders relaxed.", sub.feedback)

        val checklist = sub.checklistItems
        assertNotNull(checklist)
        assertEquals(3, checklist?.size)
        assertEquals("Anga Shuddham", checklist?.get(0)?.title)
        assertTrue(checklist?.get(0)?.isChecked ?: false)
        assertFalse(checklist?.get(2)?.isChecked ?: true)
    }

    @Test
    fun testReviewSubmissionRequestSerialization() {
        val checklist = listOf(
            ChecklistItemDto("1", "Anga Shuddham", "Posture", isChecked = true),
            ChecklistItemDto("2", "Aramandi", "Half-sitting", isChecked = false)
        )
        val request = ReviewSubmissionRequest(
            submissionId = 1,
            feedback = "Great execution!",
            nextStep = "Focus on speed 3.",
            checklist = checklist,
            status = "Published"
        )

        val json = gson.toJson(request)
        assertTrue(json.contains("\"submission_id\":1"))
        assertTrue(json.contains("\"feedback\":\"Great execution!\""))
        assertTrue(json.contains("\"status\":\"Published\""))
        assertTrue(json.contains("\"title\":\"Anga Shuddham\""))
        assertTrue(json.contains("\"isChecked\":true"))
    }

    @Test
    fun testSubmitHomeworkJsonRequestSerialization() {
        val request = SubmitHomeworkJsonRequest(
            homeworkId = 2,
            batchId = 1,
            studentId = 21,
            topic = "Natta Adavu",
            studentNote = "Here is my recording",
            fileName = "practice_video.mp4",
            fileDetails = "01:45 · 25 MB"
        )

        val json = gson.toJson(request)
        assertTrue(json.contains("\"homework_id\":2"))
        assertTrue(json.contains("\"batch_id\":1"))
        assertTrue(json.contains("\"topic\":\"Natta Adavu\""))
        assertTrue(json.contains("\"file_name\":\"practice_video.mp4\""))
    }

    @Test
    fun testSubmitAssignmentUiStateFileHandling() {
        var state = SubmitAssignmentUiState(
            isFileAttached = false,
            isConsentChecked = true
        )

        // Attach file
        state = state.copy(
            isFileAttached = true,
            attachedFileName = "practice.mp4",
            attachedFileDetails = "24 MB · Ready to submit"
        )
        assertTrue(state.isFileAttached)
        assertEquals("practice.mp4", state.attachedFileName)

        // Toggle consent
        state = state.copy(isConsentChecked = !state.isConsentChecked)
        assertFalse(state.isConsentChecked)

        // Remove file
        state = state.copy(
            isFileAttached = false,
            attachedFileName = "",
            attachedFileDetails = ""
        )
        assertFalse(state.isFileAttached)
        assertTrue(state.attachedFileName.isEmpty())
    }

    @Test
    fun testReviewPracticeUiStateChecklistToggle() {
        val initialItems = listOf(
            ChecklistItem("1", "Anga Shuddham", "Posture", isChecked = false),
            ChecklistItem("2", "Aramandi", "Half-sitting", isChecked = true)
        )

        var state = ReviewPracticeUiState(checklistItems = initialItems)
        assertFalse(state.checklistItems[0].isChecked)
        assertTrue(state.checklistItems[1].isChecked)

        // Toggle item 1
        val updated = state.checklistItems.map {
            if (it.id == "1") it.copy(isChecked = !it.isChecked) else it
        }
        state = state.copy(checklistItems = updated)

        assertTrue(state.checklistItems[0].isChecked)
        assertTrue(state.checklistItems[1].isChecked)
    }

    @Test
    fun testCreateHomeworkDialogState() {
        var state = ReviewPracticeUiState(showCreateHomeworkDialog = false)
        assertFalse(state.showCreateHomeworkDialog)

        // Open dialog
        state = state.copy(
            showCreateHomeworkDialog = true,
            newHomeworkBatchId = 1,
            newHomeworkTitle = "Aramandi Practice",
            newHomeworkTopic = "Aramandi",
            newHomeworkDescription = "Sit in aramandi for 2 minutes",
            newHomeworkDueDate = "2026-11-30"
        )
        assertTrue(state.showCreateHomeworkDialog)
        assertEquals(1, state.newHomeworkBatchId)
        assertEquals("Aramandi Practice", state.newHomeworkTitle)
        assertEquals("Aramandi", state.newHomeworkTopic)

        // Dismiss dialog
        state = state.copy(showCreateHomeworkDialog = false)
        assertFalse(state.showCreateHomeworkDialog)
    }
}

