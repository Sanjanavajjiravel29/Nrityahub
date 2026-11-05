package com.simats.nritahub.ui.admin

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
fun AdministratorDashboardScreen(
    viewModel: AdministratorDashboardViewModel = remember { AdministratorDashboardViewModel() },
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToFees: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToBatches: () -> Unit = {},
    onNavigateToAttendance: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigationEvent) {
        when (val event = uiState.navigationEvent) {
            AdministratorDashboardNavigationEvent.ManageCollections -> {
                onNavigateToFees()
                viewModel.onNavigationHandled()
            }
            AdministratorDashboardNavigationEvent.ManageSchedules -> {
                onNavigateToBatches()
                viewModel.onNavigationHandled()
            }
            AdministratorDashboardNavigationEvent.NotificationsClicked -> {
                onNavigateToNotifications()
                viewModel.onNavigationHandled()
            }
            is AdministratorDashboardNavigationEvent.AttentionItemClicked -> {
                when (event.actionType) {
                    "credentials", "nav_page_30", "create_student", "create_staff", "manage_rosters" -> onNavigateToBatches()
                    "nav_page_31", "calculate_salary", "overdue_fees" -> onNavigateToFees()
                    "nav_page_32", "admin_profile" -> onNavigateToProfile()
                    "nav_schedule" -> onNavigateToSchedule()
                    "attendance" -> onNavigateToAttendance()
                    "add_announcement" -> onNavigateToNotifications()
                    else -> {}
                }
                viewModel.onNavigationHandled()
            }
            AdministratorDashboardNavigationEvent.NavigateBack -> {
                viewModel.onNavigationHandled()
            }
            AdministratorDashboardNavigationEvent.None -> {}
        }
    }

    AdministratorDashboardContent(
        uiState = uiState,
        onTabSelect = { tab ->
            when (tab) {
                AdminTab.BATCHES -> onNavigateToBatches()
                AdminTab.FEES -> onNavigateToFees()
                AdminTab.PROFILE -> onNavigateToProfile()
                else -> viewModel.onTabSelected(tab)
            }
        },
        onManageCollectionsClick = viewModel::onManageCollectionsClicked,
        onManageSchedulesClick = viewModel::onManageSchedulesClicked,
        onAttentionItemClick = viewModel::onAttentionItemClicked,
        onNotificationsClick = viewModel::onNotificationsClicked,
        onRetryClick = viewModel::fetchDashboardStats
    )
}

@Composable
fun AdministratorDashboardContent(
    uiState: AdministratorDashboardUiState,
    onTabSelect: (AdminTab) -> Unit,
    onManageCollectionsClick: () -> Unit,
    onManageSchedulesClick: () -> Unit,
    onAttentionItemClick: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onRetryClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = CreamBackground,
        bottomBar = {
            // Bottom 4-Tab Navigation Bar (Admin)
            Card(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
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
                    AdminTab.entries.forEach { tab ->
                        val isSelected = uiState.selectedTab == tab
                        val contentColor = if (isSelected) DeepPlum else MutedBodyText

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onTabSelect(tab) }
                        ) {
                            AdminTabIcon(tab = tab, color = contentColor)
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Main Greeting Title
            Text(
                text = uiState.title,
                fontFamily = FontFamily.Serif,
                fontSize = 32.sp,
                color = DarkMaroonText
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Academy Name & Date Subhead Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.academyAndDateText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MediumMaroonText
                )

                Box(
                    modifier = Modifier
                        .background(SoftLavenderBadge, shape = RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = uiState.overviewBadge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = DeepPlum
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = DeepPlum,
                    trackColor = SoftLavenderBadge
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFBEBEB)),
                    border = BorderStroke(1.dp, Color(0xFFE57373))
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
                            color = Color(0xFFB71C1C),
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Retry",
                            color = DeepPlum,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { onRetryClick() }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Summary Stats Grid (2x2 Grid)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = uiState.enrolledStudentsCount,
                                fontFamily = FontFamily.Serif,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Enrolled students",
                                fontSize = 11.sp,
                                color = MutedBodyText
                            )
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = uiState.activeBatchesCount,
                                fontFamily = FontFamily.Serif,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Active batches",
                                fontSize = 11.sp,
                                color = MutedBodyText
                            )
                        }
                    }
                }

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = uiState.activeStaffCount,
                                fontFamily = FontFamily.Serif,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Active staff / instructors",
                                fontSize = 11.sp,
                                color = MutedBodyText
                            )
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = uiState.calculatedSalaryTotal,
                                fontFamily = FontFamily.Serif,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Staff salary calculation",
                                fontSize = 11.sp,
                                color = MutedBodyText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // "October fee collection" Card
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
                            text = uiState.feeCollectionTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMaroonText
                        )

                        Text(
                            text = uiState.feePercentageText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepPlum
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = uiState.totalCollectedAmount,
                        fontFamily = FontFamily.Serif,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkMaroonText
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = uiState.totalBilledText,
                        fontSize = 12.sp,
                        color = MutedBodyText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE8DDE3))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(uiState.collectionProgress)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(DeepPlum)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.pendingAmountText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkMaroonText
                        )

                        Text(
                            text = uiState.overdueAmountText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF944242)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onManageCollectionsClick() }
                    ) {
                        Text(
                            text = "Manage collections",
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

            // "Needs your attention" Section
            Text(
                text = uiState.attentionTitle,
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                color = DarkMaroonText
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    uiState.attentionItems.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAttentionItemClick(item.actionType) }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkMaroonText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.subtitle,
                                    fontSize = 12.sp,
                                    color = MutedBodyText
                                )
                            }

                            Canvas(modifier = Modifier.size(12.dp)) {
                                val w = size.width
                                val h = size.height
                                val stroke = 1.5.dp.toPx()
                                drawLine(color = DeepPlum, start = Offset(0f, 0f), end = Offset(w, h / 2), strokeWidth = stroke, cap = StrokeCap.Round)
                                drawLine(color = DeepPlum, start = Offset(w, h / 2), end = Offset(0f, h), strokeWidth = stroke, cap = StrokeCap.Round)
                            }
                        }

                        if (index < uiState.attentionItems.size - 1) {
                            HorizontalDivider(
                                color = CardBorder,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Manage batches & schedules button
            OutlinedButton(
                onClick = onManageSchedulesClick,
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
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Manage batches & schedules",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepPlum
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Canvas(modifier = Modifier.size(14.dp)) {
                        val w = size.width
                        val h = size.height
                        drawPath(
                            path = Path().apply {
                                addRoundRect(
                                    RoundRect(
                                        rect = Rect(0f, h * 0.2f, w, h),
                                        cornerRadius = CornerRadius(3f, 3f)
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recital Notice Box
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
                                    moveTo(0f, h * 0.35f)
                                    lineTo(w * 0.4f, h * 0.35f)
                                    lineTo(w * 0.85f, 0f)
                                    lineTo(w * 0.85f, h)
                                    lineTo(w * 0.4f, h * 0.65f)
                                    lineTo(0f, h * 0.65f)
                                    close()
                                },
                                color = DeepPlum
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = uiState.recitalNotice,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = DeepPlum
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun AdminTabIcon(tab: AdminTab, color: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height

        when (tab) {
            AdminTab.OVERVIEW -> {
                drawRect(color = color, topLeft = Offset(0f, 0f), size = Size(w * 0.42f, h * 0.42f), style = Stroke(width = 1.5.dp.toPx()))
                drawRect(color = color, topLeft = Offset(w * 0.58f, 0f), size = Size(w * 0.42f, h * 0.42f), style = Stroke(width = 1.5.dp.toPx()))
                drawRect(color = color, topLeft = Offset(0f, h * 0.58f), size = Size(w * 0.42f, h * 0.42f), style = Stroke(width = 1.5.dp.toPx()))
                drawRect(color = color, topLeft = Offset(w * 0.58f, h * 0.58f), size = Size(w * 0.42f, h * 0.42f), style = Stroke(width = 1.5.dp.toPx()))
            }
            AdminTab.BATCHES -> {
                drawCircle(color = color, radius = w * 0.2f, center = Offset(w * 0.35f, h * 0.3f), style = Stroke(width = 1.5.dp.toPx()))
                drawCircle(color = color, radius = w * 0.2f, center = Offset(w * 0.7f, h * 0.3f), style = Stroke(width = 1.5.dp.toPx()))
            }
            AdminTab.FEES -> {
                drawPath(
                    path = Path().apply {
                        addRoundRect(
                            androidx.compose.ui.geometry.RoundRect(
                                rect = androidx.compose.ui.geometry.Rect(0f, h * 0.15f, w, h * 0.85f),
                                cornerRadius = CornerRadius(4f, 4f)
                            )
                        )
                    },
                    color = color,
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
            AdminTab.PROFILE -> {
                drawCircle(color = color, radius = w * 0.25f, center = Offset(w * 0.5f, h * 0.3f), style = Stroke(width = 1.5.dp.toPx()))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdministratorDashboardScreenPreview() {
    NritahubTheme {
        AdministratorDashboardContent(
            uiState = AdministratorDashboardUiState(),
            onTabSelect = {},
            onManageCollectionsClick = {},
            onManageSchedulesClick = {},
            onAttentionItemClick = {},
            onNotificationsClick = {}
        )
    }
}
