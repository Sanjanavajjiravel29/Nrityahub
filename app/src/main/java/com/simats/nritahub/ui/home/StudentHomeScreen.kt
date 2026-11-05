package com.simats.nritahub.ui.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun StudentHomeScreen(
    viewModel: StudentHomeViewModel = remember { StudentHomeViewModel() },
    onNavigateToClassDetails: () -> Unit,
    onNavigateToSadhana: () -> Unit,
    onNavigateToEvents: () -> Unit,
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToFees: () -> Unit = {},
    onNavigateToAttendance: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {},
    onNavigateToPractice: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Log out",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    color = DarkMaroonText
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to log out?",
                    fontSize = 14.sp,
                    color = MutedBodyText
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkMaroonText,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Logout", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", color = DeepPlum)
                }
            },
            containerColor = CardBackground,
            shape = RoundedCornerShape(16.dp)
        )
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (uiState.navigationEvent) {
            StudentHomeNavigationEvent.ViewClassDetails -> {
                onNavigateToClassDetails()
                viewModel.onNavigationHandled()
            }
            StudentHomeNavigationEvent.OpenSadhana -> {
                onNavigateToSadhana()
                viewModel.onNavigationHandled()
            }
            StudentHomeNavigationEvent.ExploreEvents -> {
                onNavigateToEvents()
                viewModel.onNavigationHandled()
            }
            StudentHomeNavigationEvent.NotificationsClicked -> {
                onNavigateToNotifications()
                viewModel.onNavigationHandled()
            }
            StudentHomeNavigationEvent.None -> {}
        }
    }

    StudentHomeContent(
        uiState = uiState,
        onTabSelect = { tab ->
            when (tab) {
                HomeTab.HOME -> viewModel.onTabSelected(tab)
                HomeTab.SCHEDULE -> onNavigateToSchedule()
                HomeTab.PRACTICE -> onNavigateToPractice()
                HomeTab.PROGRESS -> onNavigateToProgress()
            }
        },
        onViewClassDetailsClick = viewModel::onViewClassDetailsClicked,
        onOpenSadhanaClick = viewModel::onOpenSadhanaClicked,
        onExploreEventsClick = viewModel::onExploreEventsClicked,
        onNotificationsClick = viewModel::onNotificationsClicked,
        onFeesClick = onNavigateToFees,
        onAttendanceClick = onNavigateToAttendance,
        onLogoutClick = { showLogoutDialog = true }
    )
}

@Composable
fun StudentHomeContent(
    uiState: StudentHomeUiState,
    onTabSelect: (HomeTab) -> Unit,
    onViewClassDetailsClick: () -> Unit,
    onOpenSadhanaClick: () -> Unit,
    onExploreEventsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onFeesClick: () -> Unit,
    onAttendanceClick: () -> Unit,
    onLogoutClick: () -> Unit = {}
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
            // Top Bar / Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(SoftLavenderBadge),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            val w = size.width
                            val h = size.height
                            drawPath(
                                path = Path().apply {
                                    moveTo(w * 0.5f, 0f)
                                    lineTo(w, h * 0.35f)
                                    lineTo(0f, h * 0.35f)
                                    close()
                                },
                                color = DeepPlum
                            )
                            drawLine(color = DeepPlum, start = Offset(w * 0.25f, h * 0.45f), end = Offset(w * 0.25f, h * 0.9f), strokeWidth = 2f)
                            drawLine(color = DeepPlum, start = Offset(w * 0.75f, h * 0.45f), end = Offset(w * 0.75f, h * 0.9f), strokeWidth = 2f)
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Bell / Notification Icon
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Canvas(modifier = Modifier.size(18.dp)) {
                            val w = size.width
                            val h = size.height
                            drawPath(
                                path = Path().apply {
                                    moveTo(w * 0.5f, 0f)
                                    cubicTo(w * 0.2f, h * 0.1f, w * 0.1f, h * 0.5f, w * 0.1f, h * 0.75f)
                                    lineTo(w * 0.9f, h * 0.75f)
                                    cubicTo(w * 0.9f, h * 0.5f, w * 0.8f, h * 0.1f, w * 0.5f, 0f)
                                },
                                color = DeepPlum,
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                            drawLine(color = DeepPlum, start = Offset(0f, h * 0.75f), end = Offset(w, h * 0.75f), strokeWidth = 1.5.dp.toPx())
                            drawArc(
                                color = DeepPlum,
                                startAngle = 0f,
                                sweepAngle = 180f,
                                useCenter = false,
                                topLeft = Offset(w * 0.35f, h * 0.75f),
                                size = Size(w * 0.3f, h * 0.25f),
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Quick Logout Icon
                    IconButton(
                        onClick = onLogoutClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Canvas(modifier = Modifier.size(18.dp)) {
                            val w = size.width
                            val h = size.height
                            val stroke = 1.6.dp.toPx()
                            drawPath(
                                path = Path().apply {
                                    moveTo(w * 0.55f, h * 0.15f)
                                    lineTo(w * 0.15f, h * 0.15f)
                                    lineTo(w * 0.15f, h * 0.85f)
                                    lineTo(w * 0.55f, h * 0.85f)
                                },
                                color = DeepPlum,
                                style = Stroke(width = stroke, cap = StrokeCap.Round)
                            )
                            drawLine(color = DeepPlum, start = Offset(w * 0.35f, h * 0.5f), end = Offset(w * 0.95f, h * 0.5f), strokeWidth = stroke, cap = StrokeCap.Round)
                            drawLine(color = DeepPlum, start = Offset(w * 0.7f, h * 0.25f), end = Offset(w * 0.95f, h * 0.5f), strokeWidth = stroke, cap = StrokeCap.Round)
                            drawLine(color = DeepPlum, start = Offset(w * 0.7f, h * 0.75f), end = Offset(w * 0.95f, h * 0.5f), strokeWidth = stroke, cap = StrokeCap.Round)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Greeting Title
            Text(
                text = uiState.title,
                fontFamily = FontFamily.Serif,
                fontSize = 32.sp,
                color = DarkMaroonText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Segmented 4-Tab Navigation Bar
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HomeTab.entries.forEach { tab ->
                        val isSelected = uiState.selectedTab == tab
                        val contentColor = if (isSelected) DeepPlum else MutedBodyText

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onTabSelect(tab) }
                        ) {
                            TabIcon(tab = tab, color = contentColor)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = contentColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Student Badge Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SoftLavenderBadge),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.studentInitials,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepPlum
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = uiState.studentTagline,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MediumMaroonText
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Next Class Banner Card ("Find your rhythm")
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SoftLavenderBadge),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.nextClassBadge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            color = DeepPlum
                        )
                        Text(
                            text = uiState.nextClassDate,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkMaroonText
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = uiState.nextClassTitle,
                        fontFamily = FontFamily.Serif,
                        fontSize = 24.sp,
                        color = DarkMaroonText
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = uiState.nextClassReminderText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkMaroonText
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = uiState.nextClassNoteText,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MutedBodyText
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onViewClassDetailsClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkMaroonText,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "View class details",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Canvas(modifier = Modifier.size(14.dp)) {
                                val w = size.width
                                val h = size.height
                                val stroke = 1.8.dp.toPx()
                                drawLine(color = Color.White, start = Offset(0f, h / 2), end = Offset(w, h / 2), strokeWidth = stroke, cap = StrokeCap.Round)
                                drawLine(color = Color.White, start = Offset(w * 0.6f, h * 0.2f), end = Offset(w, h / 2), strokeWidth = stroke, cap = StrokeCap.Round)
                                drawLine(color = Color.White, start = Offset(w * 0.6f, h * 0.8f), end = Offset(w, h / 2), strokeWidth = stroke, cap = StrokeCap.Round)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fee Details & Invoices Quick Action Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SoftLavenderBadge),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFeesClick() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CardBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            // Invoice / Receipt Icon
                            Canvas(modifier = Modifier.size(18.dp)) {
                                val w = size.width
                                val h = size.height
                                drawPath(
                                    path = Path().apply {
                                        addRoundRect(
                                            RoundRect(
                                                rect = Rect(0f, h * 0.1f, w, h * 0.9f),
                                                cornerRadius = CornerRadius(4f, 4f)
                                            )
                                        )
                                    },
                                    color = DeepPlum,
                                    style = Stroke(width = 1.5.dp.toPx())
                                )
                                drawLine(color = DeepPlum, start = Offset(w * 0.25f, h * 0.35f), end = Offset(w * 0.75f, h * 0.35f), strokeWidth = 1.5.dp.toPx())
                                drawLine(color = DeepPlum, start = Offset(w * 0.25f, h * 0.65f), end = Offset(w * 0.55f, h * 0.65f), strokeWidth = 1.5.dp.toPx())
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Fee Details & Invoices",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "October tuition · ₹2,400 due 10 Oct",
                                fontSize = 12.sp,
                                color = MutedBodyText
                            )
                        }
                    }

                    Canvas(modifier = Modifier.size(12.dp)) {
                        val w = size.width
                        val h = size.height
                        val stroke = 1.8.dp.toPx()
                        drawLine(color = DeepPlum, start = Offset(0f, 0f), end = Offset(w, h / 2), strokeWidth = stroke, cap = StrokeCap.Round)
                        drawLine(color = DeepPlum, start = Offset(w, h / 2), end = Offset(0f, h), strokeWidth = stroke, cap = StrokeCap.Round)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Attendance Record Quick Action Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SoftLavenderBadge),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAttendanceClick() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CardBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            // Calendar Check Icon
                            Canvas(modifier = Modifier.size(18.dp)) {
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
                                drawLine(color = DeepPlum, start = Offset(w * 0.3f, 0f), end = Offset(w * 0.3f, h * 0.3f), strokeWidth = 1.5.dp.toPx())
                                drawLine(color = DeepPlum, start = Offset(w * 0.7f, 0f), end = Offset(w * 0.7f, h * 0.3f), strokeWidth = 1.5.dp.toPx())
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Attendance Record",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "92% attendance · 12 of 13 classes attended",
                                fontSize = 12.sp,
                                color = MutedBodyText
                            )
                        }
                    }

                    Canvas(modifier = Modifier.size(12.dp)) {
                        val w = size.width
                        val h = size.height
                        val stroke = 1.8.dp.toPx()
                        drawLine(color = DeepPlum, start = Offset(0f, 0f), end = Offset(w, h / 2), strokeWidth = stroke, cap = StrokeCap.Round)
                        drawLine(color = DeepPlum, start = Offset(w, h / 2), end = Offset(0f, h), strokeWidth = stroke, cap = StrokeCap.Round)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Daily Sadhana Section
            Text(
                text = "Your daily sadhana",
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                color = DarkMaroonText
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Assignment Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = uiState.sadhanaTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkMaroonText
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = uiState.sadhanaSubtitle,
                        fontSize = 12.sp,
                        color = MutedBodyText
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onOpenSadhanaClick() }
                ) {
                    Text(
                        text = "Open",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepPlum
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Canvas(modifier = Modifier.size(12.dp)) {
                        val w = size.width
                        val h = size.height
                        val stroke = 1.5.dp.toPx()
                        drawLine(color = DeepPlum, start = Offset(0f, h * 0.2f), end = Offset(w * 0.7f, h * 0.5f), strokeWidth = stroke, cap = StrokeCap.Round)
                        drawLine(color = DeepPlum, start = Offset(w * 0.7f, h * 0.5f), end = Offset(0f, h * 0.8f), strokeWidth = stroke, cap = StrokeCap.Round)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Grid Row (Attendance & Weekly Practice)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = uiState.attendancePercentage,
                            fontFamily = FontFamily.Serif,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMaroonText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.attendanceRatio,
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
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = uiState.practiceWeeklyScore,
                            fontFamily = FontFamily.Serif,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMaroonText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.practiceWeeklyLabel,
                            fontSize = 11.sp,
                            color = MutedBodyText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instructor Note Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoftLavenderBadge),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Canvas(modifier = Modifier.size(18.dp)) {
                        val w = size.width
                        val h = size.height
                        drawPath(
                            path = Path().apply {
                                addRoundRect(
                                    RoundRect(
                                        rect = Rect(0f, 0f, w, h * 0.75f),
                                        cornerRadius = CornerRadius(8f, 8f)
                                    )
                                )
                                moveTo(w * 0.2f, h * 0.75f)
                                lineTo(w * 0.1f, h)
                                lineTo(w * 0.4f, h * 0.75f)
                            },
                            color = DeepPlum,
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = uiState.instructorFeedback,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = DeepPlum
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Academy Announcement Card
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
                    Box(
                        modifier = Modifier
                            .background(SoftLavenderBadge, shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "ACADEMY ANNOUNCEMENT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            color = DeepPlum
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = uiState.announcementTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkMaroonText
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = uiState.announcementBody,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MutedBodyText
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onExploreEventsClick() }
                    ) {
                        Text(
                            text = "Explore events",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepPlum
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Canvas(modifier = Modifier.size(12.dp)) {
                            val w = size.width
                            val h = size.height
                            val stroke = 1.5.dp.toPx()
                            drawLine(color = DeepPlum, start = Offset(0f, h / 2), end = Offset(w, h / 2), strokeWidth = stroke, cap = StrokeCap.Round)
                            drawLine(color = DeepPlum, start = Offset(w * 0.6f, h * 0.2f), end = Offset(w, h / 2), strokeWidth = stroke, cap = StrokeCap.Round)
                            drawLine(color = DeepPlum, start = Offset(w * 0.6f, h * 0.8f), end = Offset(w, h / 2), strokeWidth = stroke, cap = StrokeCap.Round)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Visible Logout Option
            OutlinedButton(
                onClick = onLogoutClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CardBackground,
                    contentColor = DeepPlum
                ),
                border = BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Log out",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepPlum
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TabIcon(tab: HomeTab, color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height

        when (tab) {
            HomeTab.HOME -> {
                drawPath(
                    path = Path().apply {
                        moveTo(w * 0.5f, 0f)
                        lineTo(w, h * 0.45f)
                        lineTo(w * 0.85f, h * 0.45f)
                        lineTo(w * 0.85f, h)
                        lineTo(w * 0.15f, h)
                        lineTo(w * 0.15f, h * 0.45f)
                        lineTo(0f, h * 0.45f)
                        close()
                    },
                    color = color,
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
            HomeTab.SCHEDULE -> {
                drawPath(
                    path = Path().apply {
                        addRoundRect(
                            RoundRect(
                                rect = Rect(0f, h * 0.2f, w, h),
                                cornerRadius = CornerRadius(6f, 6f)
                            )
                        )
                    },
                    color = color,
                    style = Stroke(width = 1.5.dp.toPx())
                )
                drawLine(color = color, start = Offset(w * 0.3f, 0f), end = Offset(w * 0.3f, h * 0.3f), strokeWidth = 1.5.dp.toPx())
                drawLine(color = color, start = Offset(w * 0.7f, 0f), end = Offset(w * 0.7f, h * 0.3f), strokeWidth = 1.5.dp.toPx())
            }
            HomeTab.PRACTICE -> {
                drawCircle(color = color, radius = size.width / 2, style = Stroke(width = 1.5.dp.toPx()))
                drawPath(
                    path = Path().apply {
                        moveTo(w * 0.4f, h * 0.3f)
                        lineTo(w * 0.7f, h * 0.5f)
                        lineTo(w * 0.4f, h * 0.7f)
                        close()
                    },
                    color = color
                )
            }
            HomeTab.PROGRESS -> {
                drawLine(color = color, start = Offset(0f, h), end = Offset(w, h), strokeWidth = 1.5.dp.toPx())
                drawLine(color = color, start = Offset(w * 0.2f, h), end = Offset(w * 0.2f, h * 0.6f), strokeWidth = 2.5.dp.toPx())
                drawLine(color = color, start = Offset(w * 0.5f, h), end = Offset(w * 0.5f, h * 0.3f), strokeWidth = 2.5.dp.toPx())
                drawLine(color = color, start = Offset(w * 0.8f, h), end = Offset(w * 0.8f, h * 0.1f), strokeWidth = 2.5.dp.toPx())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentHomeScreenPreview() {
    NritahubTheme {
        StudentHomeContent(
            uiState = StudentHomeUiState(),
            onTabSelect = {},
            onViewClassDetailsClick = {},
            onOpenSadhanaClick = {},
            onExploreEventsClick = {},
            onNotificationsClick = {},
            onFeesClick = {},
            onAttendanceClick = {},
            onLogoutClick = {}
        )
    }
}
