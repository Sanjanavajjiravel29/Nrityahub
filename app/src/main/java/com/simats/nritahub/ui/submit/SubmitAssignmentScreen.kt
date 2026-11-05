package com.simats.nritahub.ui.submit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.nritahub.ui.theme.CardBackground
import com.simats.nritahub.ui.theme.CardBorder
import com.simats.nritahub.ui.theme.CreamBackground
import com.simats.nritahub.ui.theme.DarkMaroonText
import com.simats.nritahub.ui.theme.DeepPlum
import com.simats.nritahub.ui.theme.MediumMaroonText
import com.simats.nritahub.ui.theme.MutedBodyText
import com.simats.nritahub.ui.theme.NritahubTheme
import com.simats.nritahub.ui.theme.SoftLavenderBadge

@Composable
fun SubmitAssignmentScreen(
    viewModel: SubmitAssignmentViewModel = remember { SubmitAssignmentViewModel() },
    onBackToPrevious: () -> Unit,
    onSubmitSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onFilePicked(context, uri)
        }
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (uiState.navigationEvent) {
            SubmitAssignmentNavigationEvent.NavigateBack -> {
                onBackToPrevious()
                viewModel.onNavigationHandled()
            }
            SubmitAssignmentNavigationEvent.ChooseFileClicked -> {
                filePickerLauncher.launch("*/*")
                viewModel.onNavigationHandled()
            }
            SubmitAssignmentNavigationEvent.RemoveFileClicked -> {
                viewModel.onNavigationHandled()
            }
            SubmitAssignmentNavigationEvent.SubmittedToInstructor -> {
                // Keep the confirmation visible or navigate if desired
                viewModel.onNavigationHandled()
            }
            SubmitAssignmentNavigationEvent.None -> {}
        }
    }

    SubmitAssignmentContent(
        uiState = uiState,
        onInstructorNoteChange = viewModel::onInstructorNoteChanged,
        onToggleConsentClick = viewModel::toggleConsent,
        onChooseFileClick = { filePickerLauncher.launch("*/*") },
        onRemoveFileClick = viewModel::onRemoveFileClicked,
        onSubmitClick = viewModel::onSubmitClicked,
        onBackClick = viewModel::onBackClicked,
        onSelectHomework = viewModel::onSelectHomework,
        onRetryClick = viewModel::loadAssignedHomework
    )
}

@Composable
fun SubmitAssignmentContent(
    uiState: SubmitAssignmentUiState,
    onInstructorNoteChange: (String) -> Unit,
    onToggleConsentClick: () -> Unit,
    onChooseFileClick: () -> Unit,
    onRemoveFileClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onBackClick: () -> Unit,
    onSelectHomework: (Int) -> Unit = {},
    onRetryClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = CreamBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Canvas(modifier = Modifier.size(16.dp)) {
                        val w = size.width
                        val h = size.height
                        val stroke = 2.dp.toPx()
                        drawLine(
                            color = DeepPlum,
                            start = Offset(w, h / 2),
                            end = Offset(0f, h / 2),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = DeepPlum,
                            start = Offset(w * 0.4f, h * 0.2f),
                            end = Offset(0f, h / 2),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = DeepPlum,
                            start = Offset(w * 0.4f, h * 0.8f),
                            end = Offset(0f, h / 2),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round
                        )
                    }
                }

                // Three dots trailing icon
                Canvas(modifier = Modifier.size(16.dp)) {
                    val r = 1.8.dp.toPx()
                    val centerY = size.height / 2
                    val w = size.width
                    drawCircle(color = MediumMaroonText, radius = r, center = Offset(w * 0.2f, centerY))
                    drawCircle(color = MediumMaroonText, radius = r, center = Offset(w * 0.5f, centerY))
                    drawCircle(color = MediumMaroonText, radius = r, center = Offset(w * 0.8f, centerY))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Loading state banner
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DeepPlum, strokeWidth = 2.dp)
                }
            }

            // Error banner
            if (uiState.errorMessage != null) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = Color(0xFFC62828),
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Retry",
                            color = Color(0xFFC62828),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clickable { onRetryClick() }
                                .padding(start = 8.dp)
                        )
                    }
                }
            }

            // Success banner
            if (uiState.successMessage != null) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            drawCircle(color = Color(0xFF2E7D32), radius = size.width / 2)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.successMessage,
                            color = Color(0xFF2E7D32),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Homework Selector if multiple homework items exist
            if (uiState.homeworkList.size > 1) {
                Text(
                    text = "SELECT ASSIGNMENT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = MediumMaroonText
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.homeworkList.forEach { hw ->
                        val isSelected = hw.id == uiState.selectedHomeworkId
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) DeepPlum else SoftLavenderBadge,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onSelectHomework(hw.id) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = hw.title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else DeepPlum
                            )
                        }
                    }
                }
            }

            // Main Title
            Text(
                text = uiState.title,
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                color = DarkMaroonText
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Assignment Badge & Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .background(SoftLavenderBadge, shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = uiState.badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = DeepPlum
                    )
                }

                if (uiState.submissionStatus != null) {
                    val isReviewed = uiState.submissionStatus.equals("Reviewed", ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .background(
                                if (isReviewed) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isReviewed) "Reviewed" else "Submitted · Pending Review",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isReviewed) Color(0xFF2E7D32) else Color(0xFFE65100)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle / Description
            Text(
                text = uiState.subtitle,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MutedBodyText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Upload Box Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SoftLavenderBadge),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Cloud Upload Icon
                    Canvas(modifier = Modifier.size(24.dp)) {
                        val w = size.width
                        val h = size.height
                        drawPath(
                            path = Path().apply {
                                moveTo(w * 0.5f, h * 0.6f)
                                lineTo(w * 0.5f, h * 0.15f)
                                moveTo(w * 0.3f, h * 0.35f)
                                lineTo(w * 0.5f, h * 0.15f)
                                lineTo(w * 0.7f, h * 0.35f)
                            },
                            color = DeepPlum,
                            style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawPath(
                            path = Path().apply {
                                moveTo(w * 0.15f, h * 0.7f)
                                cubicTo(0f, h * 0.7f, 0f, h, w * 0.5f, h)
                                cubicTo(w, h, w, h * 0.7f, w * 0.85f, h * 0.7f)
                            },
                            color = DeepPlum,
                            style = Stroke(width = 1.8.dp.toPx())
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Add a practice recording",
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        color = DarkMaroonText
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Choose a video or document from your device.",
                        fontSize = 13.sp,
                        color = MutedBodyText
                    )

                    Text(
                        text = "MP4, MOV or PDF · up to 100 MB",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MediumMaroonText
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onChooseFileClick,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = CardBackground,
                            contentColor = DarkMaroonText
                        ),
                        border = BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = if (uiState.isFileAttached) "Choose another file" else "Select file from device",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMaroonText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Uploaded File Card (if attached)
            if (uiState.isFileAttached) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = uiState.attachedFileName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkMaroonText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = uiState.attachedFileDetails,
                                    fontSize = 12.sp,
                                    color = MutedBodyText
                                )
                            }

                            // Checkmark Icon
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.size(12.dp)) {
                                    val w = size.width
                                    val h = size.height
                                    drawPath(
                                        path = Path().apply {
                                            moveTo(0f, h * 0.5f)
                                            lineTo(w * 0.35f, h * 0.85f)
                                            lineTo(w, h * 0.2f)
                                        },
                                        color = Color(0xFF2E7D32),
                                        style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Remove file",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828),
                            modifier = Modifier.clickable { onRemoveFileClick() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Instructor Review & Evaluation Card (if assignment was already reviewed)
            if (uiState.hasReview) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF7F2)),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "INSTRUCTOR FEEDBACK",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                color = DeepPlum
                            )
                            if (!uiState.reviewerName.isNullOrBlank()) {
                                Text(
                                    text = "by ${uiState.reviewerName}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MediumMaroonText
                                )
                            }
                        }

                        if (!uiState.reviewedFeedback.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.reviewedFeedback,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = DarkMaroonText
                            )
                        }

                        if (!uiState.reviewedNextStep.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Actionable Next Step:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepPlum
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = uiState.reviewedNextStep,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = DarkMaroonText
                            )
                        }

                        // Checklist items summary
                        if (uiState.reviewedChecklist.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = CardBorder, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Evaluation Checklist:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepPlum
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            uiState.reviewedChecklist.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (item.isChecked) "✓" else "○",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (item.isChecked) Color(0xFF2E7D32) else Color(0xFF9E9E9E)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = item.title,
                                        fontSize = 12.sp,
                                        color = DarkMaroonText,
                                        fontWeight = if (item.isChecked) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // "A note for your instructor" Field
            Text(
                text = "A note for your instructor",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DarkMaroonText
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = uiState.instructorNote,
                onValueChange = onInstructorNoteChange,
                minLines = 3,
                placeholder = {
                    Text(
                        text = "e.g. My count feels steadier now. I'd love help with posture in the third speed set.",
                        fontSize = 12.sp,
                        color = MutedBodyText
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    focusedBorderColor = DeepPlum,
                    unfocusedBorderColor = CardBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Consent Checkbox Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleConsentClick() }
            ) {
                Canvas(modifier = Modifier.size(20.dp)) {
                    val r = size.width / 2
                    if (uiState.isConsentChecked) {
                        drawCircle(color = Color(0xFF2E7D32), radius = r)
                        drawPath(
                            path = Path().apply {
                                moveTo(r * 0.5f, r)
                                lineTo(r * 0.85f, r * 1.35f)
                                lineTo(r * 1.5f, r * 0.65f)
                            },
                            color = Color.White,
                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                        )
                    } else {
                        drawCircle(color = CardBorder, radius = r, style = Stroke(width = 1.5.dp.toPx()))
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "I consent to sharing this recording with my instructor for learning feedback.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = DarkMaroonText,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Privacy Notice Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoftLavenderBadge),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(CardBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        // Shield Icon
                        Canvas(modifier = Modifier.size(14.dp)) {
                            val w = size.width
                            val h = size.height
                            drawPath(
                                path = Path().apply {
                                    moveTo(w * 0.5f, 0f)
                                    lineTo(w, h * 0.25f)
                                    lineTo(w, h * 0.65f)
                                    cubicTo(w, h, w * 0.5f, h, w * 0.5f, h)
                                    cubicTo(w * 0.5f, h, 0f, h, 0f, h * 0.65f)
                                    lineTo(0f, h * 0.25f)
                                    close()
                                },
                                color = DeepPlum,
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = uiState.privacyNotice,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = DeepPlum
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Primary Action Button ("Submit to Meera" / "Submit to Instructor")
            Button(
                onClick = onSubmitClick,
                enabled = !uiState.isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkMaroonText,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Submitting Practice...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.submissionStatus != null) "Resubmit Practice" else uiState.submitButtonText,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Send Paperplane Icon
                        Canvas(modifier = Modifier.size(16.dp)) {
                            val w = size.width
                            val h = size.height
                            drawPath(
                                path = Path().apply {
                                    moveTo(0f, 0f)
                                    lineTo(w, h * 0.5f)
                                    lineTo(0f, h)
                                    lineTo(w * 0.3f, h * 0.5f)
                                    close()
                                },
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SubmitAssignmentScreenPreview() {
    NritahubTheme {
        SubmitAssignmentContent(
            uiState = SubmitAssignmentUiState(),
            onInstructorNoteChange = {},
            onToggleConsentClick = {},
            onChooseFileClick = {},
            onRemoveFileClick = {},
            onSubmitClick = {},
            onBackClick = {}
        )
    }
}
