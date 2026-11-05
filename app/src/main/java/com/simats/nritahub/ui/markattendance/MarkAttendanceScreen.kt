package com.simats.nritahub.ui.markattendance

import android.app.DatePickerDialog
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
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
import java.util.Calendar
import java.util.Locale

@Composable
fun MarkAttendanceScreen(
    viewModel: MarkAttendanceViewModel = remember { MarkAttendanceViewModel() },
    onBackToPrevious: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigationEvent) {
        when (uiState.navigationEvent) {
            MarkAttendanceNavigationEvent.NavigateBack -> {
                onBackToPrevious()
                viewModel.onNavigationHandled()
            }
            MarkAttendanceNavigationEvent.SaveAttendanceSuccess -> {
                onBackToPrevious()
                viewModel.onNavigationHandled()
            }
            MarkAttendanceNavigationEvent.None -> {}
        }
    }

    MarkAttendanceContent(
        uiState = uiState,
        onBatchSelect = viewModel::onBatchSelected,
        onDateSelect = viewModel::onDateSelected,
        onSetStatus = viewModel::setStudentStatus,
        onSaveAttendanceClick = viewModel::onSaveAttendanceClicked,
        onRefreshClick = viewModel::refresh,
        onDismissMessages = viewModel::dismissMessages,
        onBackClick = viewModel::onBackClicked
    )
}

@Composable
fun MarkAttendanceContent(
    uiState: MarkAttendanceUiState,
    onBatchSelect: (String) -> Unit = {},
    onDateSelect: (String) -> Unit = {},
    onSetStatus: (id: String, status: AttendanceStatus) -> Unit = { _, _ -> },
    onSaveAttendanceClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {},
    onDismissMessages: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // Prepare DatePickerDialog for selecting attendance date
    val calendar = Calendar.getInstance()
    try {
        val parts = uiState.selectedDate.split("-")
        if (parts.size == 3) {
            calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
        }
    } catch (_: Exception) {}

    val datePickerDialog = remember(context, uiState.selectedDate) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formatted = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onDateSelect(formatted)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

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

                // Refresh Button in header
                IconButton(
                    onClick = onRefreshClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Canvas(modifier = Modifier.size(18.dp)) {
                        val stroke = 2.dp.toPx()
                        drawArc(
                            color = DeepPlum,
                            startAngle = 45f,
                            sweepAngle = 270f,
                            useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                        // Arrow head
                        drawLine(
                            color = DeepPlum,
                            start = Offset(size.width * 0.8f, size.height * 0.35f),
                            end = Offset(size.width * 0.95f, size.height * 0.5f),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = DeepPlum,
                            start = Offset(size.width * 0.95f, size.height * 0.5f),
                            end = Offset(size.width * 0.75f, size.height * 0.65f),
                            strokeWidth = stroke,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Title & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.title,
                    fontFamily = FontFamily.Serif,
                    fontSize = 32.sp,
                    color = DarkMaroonText
                )

                Box(
                    modifier = Modifier
                        .background(
                            if (uiState.isSaved) Color(0xFFE8F5E9) else SoftLavenderBadge,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = uiState.draftBadge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.isSaved) Color(0xFF2E7D32) else DeepPlum
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Error banner if any
            if (uiState.errorMessage != null) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    border = BorderStroke(1.dp, Color(0xFFEF9A9A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = Color(0xFFC62828),
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Retry",
                            color = DeepPlum,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { onRefreshClick() }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Success banner if any
            if (uiState.successMessage != null) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = BorderStroke(1.dp, Color(0xFFA5D6A7)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = uiState.successMessage,
                            color = Color(0xFF2E7D32),
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Dismiss",
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.clickable { onDismissMessages() }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Attendance Date Selector Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SoftLavenderBadge),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(16.dp)) {
                                val w = size.width
                                val h = size.height
                                val stroke = 1.8.dp.toPx()
                                drawRect(
                                    color = DeepPlum,
                                    topLeft = Offset(0f, h * 0.25f),
                                    size = androidx.compose.ui.geometry.Size(w, h * 0.75f),
                                    style = Stroke(width = stroke)
                                )
                                drawLine(
                                    color = DeepPlum,
                                    start = Offset(0f, h * 0.5f),
                                    end = Offset(w, h * 0.5f),
                                    strokeWidth = stroke
                                )
                                drawLine(
                                    color = DeepPlum,
                                    start = Offset(w * 0.28f, 0f),
                                    end = Offset(w * 0.28f, h * 0.3f),
                                    strokeWidth = stroke,
                                    cap = StrokeCap.Round
                                )
                                drawLine(
                                    color = DeepPlum,
                                    start = Offset(w * 0.72f, 0f),
                                    end = Offset(w * 0.72f, h * 0.3f),
                                    strokeWidth = stroke,
                                    cap = StrokeCap.Round
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Attendance Date",
                                fontSize = 11.sp,
                                color = MutedBodyText,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = uiState.formattedDateText.ifEmpty { uiState.selectedDate },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(SoftLavenderBadge, shape = RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Change Date",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepPlum
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Batches Selection Section
            Text(
                text = "Select batch",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DarkMaroonText
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.batches.isEmpty() && !uiState.isLoading) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No batches available for attendance.",
                            fontSize = 13.sp,
                            color = MutedBodyText
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    uiState.batches.forEach { batch ->
                        val isSelected = uiState.selectedBatchId == batch.id
                        val cardBg = if (isSelected) SoftLavenderBadge else CardBackground
                        val cardBorder = if (isSelected) BorderStroke(1.5.dp, DeepPlum) else BorderStroke(1.dp, CardBorder)

                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            border = cardBorder,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onBatchSelect(batch.id) }
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
                                        text = batch.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkMaroonText
                                    )

                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .background(DeepPlum, shape = RoundedCornerShape(10.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "Active",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = batch.scheduleAndStudio,
                                    fontSize = 12.sp,
                                    color = MutedBodyText
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Summary Stats Grid Row (3 Cards: Present, Absent, Excused)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "${uiState.presentCount}",
                            fontFamily = FontFamily.Serif,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMaroonText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Present",
                            fontSize = 11.sp,
                            color = MutedBodyText
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "${uiState.absentCount}",
                            fontFamily = FontFamily.Serif,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMaroonText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Absent",
                            fontSize = 11.sp,
                            color = MutedBodyText
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "${uiState.excusedCount}",
                            fontFamily = FontFamily.Serif,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMaroonText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Excused",
                            fontSize = 11.sp,
                            color = MutedBodyText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active Batch Students List Header
            val activeBatch = uiState.batches.find { it.id == uiState.selectedBatchId } ?: uiState.batches.firstOrNull()

            Text(
                text = "${activeBatch?.title ?: "Batch"} Roster",
                fontFamily = FontFamily.Serif,
                fontSize = 18.sp,
                color = DarkMaroonText
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Loading indicator for students
            if (uiState.isLoading) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = DeepPlum,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Loading attendance records...",
                            fontSize = 13.sp,
                            color = MutedBodyText
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else if (uiState.currentStudents.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No enrolled students found in this batch.",
                            fontSize = 13.sp,
                            color = MutedBodyText
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                // Students List Cards
                uiState.currentStudents.forEach { student ->
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(SoftLavenderBadge),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = student.initials,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepPlum
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = student.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkMaroonText
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 3 Attendance Toggle Buttons Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Present Button
                                val isPresent = student.status == AttendanceStatus.PRESENT
                                val presentBg = if (isPresent) Color(0xFF2E5B44) else Color(0xFFF3ECE5)
                                val presentTextColor = if (isPresent) Color.White else DarkMaroonText

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(presentBg)
                                        .clickable { onSetStatus(student.id, AttendanceStatus.PRESENT) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Present",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = presentTextColor
                                    )
                                }

                                // Absent Button
                                val isAbsent = student.status == AttendanceStatus.ABSENT
                                val absentBg = if (isAbsent) Color(0xFF944242) else Color(0xFFF3ECE5)
                                val absentTextColor = if (isAbsent) Color.White else DarkMaroonText

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(absentBg)
                                        .clickable { onSetStatus(student.id, AttendanceStatus.ABSENT) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Absent",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = absentTextColor
                                    )
                                }

                                // Excused Button
                                val isExcused = student.status == AttendanceStatus.EXCUSED
                                val excusedBg = if (isExcused) Color(0xFF8B5E2B) else Color(0xFFF3ECE5)
                                val excusedTextColor = if (isExcused) Color.White else DarkMaroonText

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(excusedBg)
                                        .clickable { onSetStatus(student.id, AttendanceStatus.EXCUSED) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Excused",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = excusedTextColor
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Review Guidance Notice Box
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
                        Canvas(modifier = Modifier.size(14.dp)) {
                            val w = size.width
                            val h = size.height
                            drawPath(
                                path = Path().apply {
                                    addRoundRect(
                                        RoundRect(
                                            rect = Rect(0f, h * 0.2f, w, h),
                                            cornerRadius = CornerRadius(4f, 4f)
                                        )
                                    )
                                },
                                color = DeepPlum,
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = uiState.noticeText,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = DeepPlum
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Primary Action Button ("Save attendance")
            Button(
                onClick = onSaveAttendanceClick,
                enabled = !uiState.isSaving && uiState.currentStudents.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepPlum,
                    contentColor = Color.White,
                    disabledContainerColor = DeepPlum.copy(alpha = 0.5f),
                    disabledContentColor = Color.White.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Saving attendance...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.isSaved) "Saved ✓" else "Save attendance",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Canvas(modifier = Modifier.size(16.dp)) {
                            val w = size.width
                            val h = size.height
                            val stroke = 2.dp.toPx()
                            drawLine(color = Color.White, start = Offset(0f, h * 0.55f), end = Offset(w * 0.38f, h * 0.9f), strokeWidth = stroke, cap = StrokeCap.Round)
                            drawLine(color = Color.White, start = Offset(w * 0.38f, h * 0.9f), end = Offset(w, h * 0.15f), strokeWidth = stroke, cap = StrokeCap.Round)
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
fun MarkAttendanceScreenPreview() {
    NritahubTheme {
        MarkAttendanceContent(
            uiState = MarkAttendanceUiState()
        )
    }
}
