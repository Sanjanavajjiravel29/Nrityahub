package com.simats.nritahub.ui.feemanagement

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.nritahub.ui.admin.AdminTab
import com.simats.nritahub.ui.admin.AdminTabIcon
import com.simats.nritahub.ui.theme.CardBackground
import com.simats.nritahub.ui.theme.CardBorder
import com.simats.nritahub.ui.theme.CreamBackground
import com.simats.nritahub.ui.theme.DarkMaroonText
import com.simats.nritahub.ui.theme.DeepPlum
import com.simats.nritahub.ui.theme.MediumMaroonText
import com.simats.nritahub.ui.theme.MutedBodyText
import com.simats.nritahub.ui.theme.SoftLavenderBadge
import com.simats.nritahub.ui.theme.NritahubTheme

@Composable
fun FeeManagementScreen(
    viewModel: FeeManagementViewModel = remember { FeeManagementViewModel() },
    onNavigateToOverview: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigationEvent) {
        when (uiState.navigationEvent) {
            FeeManagementNavigationEvent.NavigateToOverview -> {
                onNavigateToOverview()
                viewModel.onNavigationHandled()
            }
            FeeManagementNavigationEvent.NavigateBack -> {
                onNavigateToOverview()
                viewModel.onNavigationHandled()
            }
            FeeManagementNavigationEvent.None -> {}
        }
    }

    FeeManagementContent(
        uiState = uiState,
        onFeeViewModeSelect = viewModel::onFeeViewModeSelected,
        onTogglePaymentStatus = viewModel::togglePaymentStatus,
        onToggleAddFeeStructureForm = viewModel::toggleAddFeeStructureForm,
        onNewFeeProgramNameChange = viewModel::onNewFeeProgramNameChanged,
        onNewFeeAmountChange = viewModel::onNewFeeAmountChanged,
        onNewFeeStudentNameChange = viewModel::onNewFeeStudentNameChanged,
        onNewFeeStudentIdChange = viewModel::onNewFeeStudentIdChanged,
        onNewFeeDueDateChange = viewModel::onNewFeeDueDateChanged,
        onAddFeeStructureSubmit = viewModel::onAddFeeStructureSubmit,
        onToggleStaffPayoutForm = viewModel::toggleStaffPayoutForm,
        onPayoutStaffNameChange = viewModel::onPayoutStaffNameChanged,
        onPayoutStaffRoleChange = viewModel::onPayoutStaffRoleChanged,
        onPayoutStaffSalaryChange = viewModel::onPayoutStaffSalaryChanged,
        onPayoutStaffUsernameChange = viewModel::onPayoutStaffUsernameChanged,
        onConfirmStaffPayoutSubmit = viewModel::onConfirmStaffPayoutSubmit,
        onTabSelect = viewModel::onTabSelected,
        onBackClick = viewModel::onBackClicked,
        onRefreshFees = viewModel::fetchFees
    )
}

@Composable
fun FeeManagementContent(
    uiState: FeeManagementUiState,
    onFeeViewModeSelect: (FeeViewMode) -> Unit = {},
    onTogglePaymentStatus: (String) -> Unit = {},
    onToggleAddFeeStructureForm: () -> Unit = {},
    onNewFeeProgramNameChange: (String) -> Unit = {},
    onNewFeeAmountChange: (String) -> Unit = {},
    onNewFeeStudentNameChange: (String) -> Unit = {},
    onNewFeeStudentIdChange: (String) -> Unit = {},
    onNewFeeDueDateChange: (String) -> Unit = {},
    onAddFeeStructureSubmit: () -> Unit = {},
    onToggleStaffPayoutForm: () -> Unit = {},
    onPayoutStaffNameChange: (String) -> Unit = {},
    onPayoutStaffRoleChange: (String) -> Unit = {},
    onPayoutStaffSalaryChange: (String) -> Unit = {},
    onPayoutStaffUsernameChange: (String) -> Unit = {},
    onConfirmStaffPayoutSubmit: () -> Unit = {},
    onTabSelect: (AdminTab) -> Unit = {},
    onBackClick: () -> Unit = {},
    onRefreshFees: () -> Unit = {}
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
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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

            // Main Card Container matching the uploaded design sample
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                border = BorderStroke(1.dp, CardBorder),
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
                            text = uiState.title,
                            fontFamily = FontFamily.Serif,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMaroonText
                        )
                        IconButton(onClick = onRefreshFees) {
                            Text(text = "↻", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DeepPlum)
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = uiState.subtitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = MutedBodyText
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (uiState.isLoading) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = DeepPlum, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Loading live fee records...", fontSize = 12.sp, color = MutedBodyText)
                        }
                    }
                    if (uiState.errorMessage != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = uiState.errorMessage ?: "",
                                color = Color(0xFFC62828),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    // Mode Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FeeViewMode.entries.forEach { mode ->
                            val isSelected = uiState.activeViewMode == mode
                            val chipBg = if (isSelected) DeepPlum else Color(0xFFF3ECE5)
                            val chipTextColor = if (isSelected) Color.White else DarkMaroonText
                            val label = if (mode == FeeViewMode.STUDENT_FEES) "Student Fees" else "Staff Salary"

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(chipBg)
                                    .clickable { onFeeViewModeSelect(mode) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = chipTextColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    when (uiState.activeViewMode) {
                        FeeViewMode.STUDENT_FEES -> {
                            if (uiState.createdFeeSuccessName != null) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                                    border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "✓ Program fee structure issued for ${uiState.createdFeeSuccessName}! Credentials and fee notification sent.",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32),
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            if (uiState.showAddFeeStructureForm) {
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.5.dp, DeepPlum),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(18.dp)
                                    ) {
                                        Text(
                                            text = "Add Program Fee Structure & Credentials",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkMaroonText
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(text = "Program / Fee name", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(modifier = Modifier.padding(12.dp)) {
                                                BasicTextField(
                                                    value = uiState.newFeeProgramNameInput,
                                                    onValueChange = onNewFeeProgramNameChange,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "Fee amount", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(modifier = Modifier.padding(12.dp)) {
                                                BasicTextField(
                                                    value = uiState.newFeeAmountInput,
                                                    onValueChange = onNewFeeAmountChange,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "Student full name", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(modifier = Modifier.padding(12.dp)) {
                                                BasicTextField(
                                                    value = uiState.newFeeStudentNameInput,
                                                    onValueChange = onNewFeeStudentNameChange,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "Student ID / Username", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(modifier = Modifier.padding(12.dp)) {
                                                BasicTextField(
                                                    value = uiState.newFeeStudentIdInput,
                                                    onValueChange = onNewFeeStudentIdChange,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "Due date", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(modifier = Modifier.padding(12.dp)) {
                                                BasicTextField(
                                                    value = uiState.newFeeDueDateInput,
                                                    onValueChange = onNewFeeDueDateChange,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Button(
                                            onClick = onAddFeeStructureSubmit,
                                            colors = ButtonDefaults.buttonColors(containerColor = SoftLavenderBadge, contentColor = DeepPlum),
                                            shape = RoundedCornerShape(16.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(46.dp)
                                        ) {
                                            Text(
                                                text = "Issue Fee Structure & Notify Student",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DeepPlum
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                            }

                            // Student Fee Cards List
                            uiState.studentFeeRecords.forEachIndexed { index, record ->
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF6F2)),
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
                                            Column {
                                                Text(
                                                    text = record.studentName,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DarkMaroonText
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "${record.batchName} · ID: ${record.studentId}",
                                                    fontSize = 12.sp,
                                                    color = MutedBodyText
                                                )
                                            }

                                            // Status Badge Pill
                                            val badgeBg = if (record.isPaid) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                                            val badgeText = if (record.isPaid) "Paid" else "Unpaid"
                                            val badgeColor = if (record.isPaid) Color(0xFF2E7D32) else Color(0xFFC62828)

                                            Box(
                                                modifier = Modifier
                                                    .background(badgeBg, shape = RoundedCornerShape(12.dp))
                                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = badgeText,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = badgeColor
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Fee amount",
                                                    fontSize = 11.sp,
                                                    color = MutedBodyText
                                                )
                                                Text(
                                                    text = record.feeAmount,
                                                    fontFamily = FontFamily.Serif,
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DarkMaroonText
                                                )
                                            }

                                            Switch(
                                                checked = record.isPaid,
                                                onCheckedChange = { onTogglePaymentStatus(record.studentId) },
                                                colors = SwitchDefaults.colors(
                                                    checkedThumbColor = Color.White,
                                                    checkedTrackColor = Color(0xFF2E7D32),
                                                    uncheckedThumbColor = Color.White,
                                                    uncheckedTrackColor = Color(0xFFB0BEC5)
                                                )
                                            )
                                        }
                                    }
                                }

                                if (index < uiState.studentFeeRecords.size - 1) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = onToggleAddFeeStructureForm,
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = SoftLavenderBadge, contentColor = DeepPlum),
                                border = BorderStroke(1.dp, CardBorder),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            ) {
                                Text(
                                    text = if (uiState.showAddFeeStructureForm) "Close Fee Form" else "+ Add Program Fee Structure & Credentials (Admin Only)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepPlum
                                )
                            }
                        }

                        FeeViewMode.STAFF_SALARY -> {
                            if (uiState.processedPayoutSuccessName != null) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                                    border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "✓ Salary payout processed & verified with staff credentials for ${uiState.processedPayoutSuccessName}! Status set to Paid / Processed.",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32),
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            if (uiState.showStaffPayoutForm) {
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.5.dp, DeepPlum),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(18.dp)
                                    ) {
                                        Text(
                                            text = "Process Salary Payout & Verify Credentials",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkMaroonText
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(text = "Staff full name", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(modifier = Modifier.padding(12.dp)) {
                                                BasicTextField(
                                                    value = uiState.payoutStaffNameInput,
                                                    onValueChange = onPayoutStaffNameChange,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "Staff role / designation", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(modifier = Modifier.padding(12.dp)) {
                                                BasicTextField(
                                                    value = uiState.payoutStaffRoleInput,
                                                    onValueChange = onPayoutStaffRoleChange,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "Calculated monthly salary", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(modifier = Modifier.padding(12.dp)) {
                                                BasicTextField(
                                                    value = uiState.payoutStaffSalaryInput,
                                                    onValueChange = onPayoutStaffSalaryChange,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(text = "Staff login username / ID", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(modifier = Modifier.padding(12.dp)) {
                                                BasicTextField(
                                                    value = uiState.payoutStaffUsernameInput,
                                                    onValueChange = onPayoutStaffUsernameChange,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Button(
                                            onClick = onConfirmStaffPayoutSubmit,
                                            colors = ButtonDefaults.buttonColors(containerColor = SoftLavenderBadge, contentColor = DeepPlum),
                                            shape = RoundedCornerShape(16.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(46.dp)
                                        ) {
                                            Text(
                                                text = "Confirm Payout & Issue Credentials Notification",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DeepPlum
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                            }

                            // Staff Salary Cards List
                            uiState.staffSalaryRecords.forEachIndexed { index, record ->
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF6F2)),
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
                                            Column {
                                                Text(
                                                    text = record.staffName,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DarkMaroonText
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "${record.role} · ${record.activeBatches}",
                                                    fontSize = 12.sp,
                                                    color = MutedBodyText
                                                )
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .background(SoftLavenderBadge, shape = RoundedCornerShape(12.dp))
                                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = record.payoutStatus,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepPlum
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = "Calculated monthly salary",
                                            fontSize = 11.sp,
                                            color = MutedBodyText
                                        )

                                        Text(
                                            text = record.calculatedSalary,
                                            fontFamily = FontFamily.Serif,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkMaroonText
                                        )
                                    }
                                }

                                if (index < uiState.staffSalaryRecords.size - 1) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = onToggleStaffPayoutForm,
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = SoftLavenderBadge, contentColor = DeepPlum),
                                border = BorderStroke(1.dp, CardBorder),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            ) {
                                Text(
                                    text = if (uiState.showStaffPayoutForm) "Close Payout Form" else "Process Salary Calculation & Payouts (Admin Only)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepPlum
                                )
                            }
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
fun FeeManagementScreenPreview() {
    NritahubTheme {
        FeeManagementContent(
            uiState = FeeManagementUiState(),
            onTogglePaymentStatus = {},
            onTabSelect = {},
            onBackClick = {}
        )
    }
}
