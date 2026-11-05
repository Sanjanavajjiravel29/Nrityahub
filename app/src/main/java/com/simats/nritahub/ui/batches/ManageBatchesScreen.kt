package com.simats.nritahub.ui.batches

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.simats.nritahub.ui.theme.NritahubTheme
import com.simats.nritahub.ui.theme.SoftLavenderBadge

@Composable
fun ManageBatchesScreen(
    viewModel: ManageBatchesViewModel = remember { ManageBatchesViewModel() },
    onNavigateToOverview: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigationEvent) {
        when (uiState.navigationEvent) {
            ManageBatchesNavigationEvent.NavigateToOverview -> {
                onNavigateToOverview()
                viewModel.onNavigationHandled()
            }
            ManageBatchesNavigationEvent.NavigateBack -> {
                onNavigateToOverview()
                viewModel.onNavigationHandled()
            }
            ManageBatchesNavigationEvent.SaveBatchSuccess -> {
                onNavigateToOverview()
                viewModel.onNavigationHandled()
            }
            ManageBatchesNavigationEvent.CreateProfileSuccess -> {
                viewModel.onNavigationHandled()
            }
            ManageBatchesNavigationEvent.None -> {}
        }
    }

    ManageBatchesContent(
        uiState = uiState,
        onModeSelect = viewModel::onBatchModeSelected,
        onSelectedBatchSelect = viewModel::onSelectedBatchChanged,
        onBatchNameChange = viewModel::onBatchNameChanged,
        onDanceStyleSelect = viewModel::onDanceStyleChanged,
        onLevelSelect = viewModel::onLevelChanged,
        onInstructorSelect = viewModel::onInstructorChanged,
        onStudioSelect = viewModel::onStudioChanged,
        onStartTimeSelect = viewModel::onStartTimeChanged,
        onEndTimeSelect = viewModel::onEndTimeChanged,
        onTuitionChange = viewModel::onTuitionChanged,
        onToggleNotify = viewModel::toggleNotifyStudents,
        onToggleDaySelect = viewModel::toggleDay,
        onSaveBatchClick = viewModel::onSaveBatchClicked,
        onToggleCreateStudentForm = viewModel::toggleCreateStudentForm,
        onNewStudentNameChange = viewModel::onNewStudentNameChanged,
        onNewStudentBatchChange = viewModel::onNewStudentBatchChanged,
        onNewStudentUsernameChange = viewModel::onNewStudentUsernameChanged,
        onNewStudentPasswordChange = viewModel::onNewStudentPasswordChanged,
        onCreateStudentSubmit = viewModel::onCreateStudentSubmit,
        onToggleCreateStaffForm = viewModel::toggleCreateStaffForm,
        onNewStaffNameChange = viewModel::onNewStaffNameChanged,
        onNewStaffRoleChange = viewModel::onNewStaffRoleChanged,
        onNewStaffBatchesChange = viewModel::onNewStaffBatchesChanged,
        onNewStaffSalaryChange = viewModel::onNewStaffSalaryChanged,
        onNewStaffUsernameChange = viewModel::onNewStaffUsernameChanged,
        onNewStaffPasswordChange = viewModel::onNewStaffPasswordChanged,
        onCreateStaffSubmit = viewModel::onCreateStaffSubmit,
        onToggleExpandStudent = viewModel::toggleExpandStudent,
        onToggleExpandStaff = viewModel::toggleExpandStaff,
        onTabSelect = viewModel::onTabSelected,
        onBackClick = viewModel::onBackClicked,
        onRefreshUsers = viewModel::fetchUsers,
        onOpenResetPasswordDialog = viewModel::openResetPasswordDialog,
        onResetPasswordInputChange = viewModel::onResetPasswordInputChanged,
        onConfirmResetPassword = viewModel::confirmResetPassword,
        onDismissResetPasswordDialog = viewModel::closeResetPasswordDialog,
        onDismissMessageBanner = viewModel::clearUserManagementMessages,
        onRefreshBatches = viewModel::fetchBatches,
        onToggleCreateBatchMode = viewModel::toggleCreateBatchMode,
        onOpenAssignStudentDialog = viewModel::openAssignStudentDialog,
        onSelectStudentToAssign = viewModel::onSelectStudentToAssign,
        onCloseAssignStudentDialog = viewModel::closeAssignStudentDialog,
        onConfirmAssignStudent = viewModel::confirmAssignStudent,
        onOpenRemoveStudentConfirmDialog = viewModel::openRemoveStudentConfirmDialog,
        onCloseRemoveStudentConfirmDialog = viewModel::closeRemoveStudentConfirmDialog,
        onConfirmRemoveStudent = viewModel::confirmRemoveStudent
    )
}

@Composable
fun ManageBatchesContent(
    uiState: ManageBatchesUiState,
    onModeSelect: (BatchMode) -> Unit = {},
    onSelectedBatchSelect: (String) -> Unit = {},
    onBatchNameChange: (String) -> Unit = {},
    onDanceStyleSelect: (String) -> Unit = {},
    onLevelSelect: (String) -> Unit = {},
    onInstructorSelect: (String) -> Unit = {},
    onStudioSelect: (String) -> Unit = {},
    onStartTimeSelect: (String) -> Unit = {},
    onEndTimeSelect: (String) -> Unit = {},
    onTuitionChange: (String) -> Unit = {},
    onToggleNotify: () -> Unit = {},
    onToggleDaySelect: (String) -> Unit = {},
    onSaveBatchClick: () -> Unit = {},
    onToggleCreateStudentForm: () -> Unit = {},
    onNewStudentNameChange: (String) -> Unit = {},
    onNewStudentBatchChange: (String) -> Unit = {},
    onNewStudentUsernameChange: (String) -> Unit = {},
    onNewStudentPasswordChange: (String) -> Unit = {},
    onCreateStudentSubmit: () -> Unit = {},
    onToggleCreateStaffForm: () -> Unit = {},
    onNewStaffNameChange: (String) -> Unit = {},
    onNewStaffRoleChange: (String) -> Unit = {},
    onNewStaffBatchesChange: (String) -> Unit = {},
    onNewStaffSalaryChange: (String) -> Unit = {},
    onNewStaffUsernameChange: (String) -> Unit = {},
    onNewStaffPasswordChange: (String) -> Unit = {},
    onCreateStaffSubmit: () -> Unit = {},
    onToggleExpandStudent: (String) -> Unit = {},
    onToggleExpandStaff: (String) -> Unit = {},
    onTabSelect: (AdminTab) -> Unit = {},
    onBackClick: () -> Unit = {},
    onRefreshUsers: () -> Unit = {},
    onOpenResetPasswordDialog: (userId: Int?, userName: String, role: String) -> Unit = { _, _, _ -> },
    onResetPasswordInputChange: (String) -> Unit = {},
    onConfirmResetPassword: () -> Unit = {},
    onDismissResetPasswordDialog: () -> Unit = {},
    onDismissMessageBanner: () -> Unit = {},
    onRefreshBatches: () -> Unit = {},
    onToggleCreateBatchMode: () -> Unit = {},
    onOpenAssignStudentDialog: () -> Unit = {},
    onSelectStudentToAssign: (Int) -> Unit = {},
    onCloseAssignStudentDialog: () -> Unit = {},
    onConfirmAssignStudent: () -> Unit = {},
    onOpenRemoveStudentConfirmDialog: (Int?, String) -> Unit = { _, _ -> },
    onCloseRemoveStudentConfirmDialog: () -> Unit = {},
    onConfirmRemoveStudent: () -> Unit = {}
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
            // Mode Toggle Segmented Control Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BatchMode.entries.forEach { mode ->
                    val isSelected = uiState.activeMode == mode
                    val chipBg = if (isSelected) DeepPlum else SoftLavenderBadge
                    val chipTextColor = if (isSelected) Color.White else DarkMaroonText
                    val label = when (mode) {
                        BatchMode.BATCHES -> "Batches & Schedule"
                        BatchMode.STUDENTS -> "Students List"
                        BatchMode.STAFF -> "Staff List"
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(chipBg)
                            .clickable { onModeSelect(mode) }
                            .padding(vertical = 12.dp),
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

            if (uiState.userManagementErrorMessage != null) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.userManagementErrorMessage ?: "",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFC62828),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "✕",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828),
                            modifier = Modifier.clickable { onDismissMessageBanner() }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (uiState.userManagementSuccessMessage != null) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = uiState.userManagementSuccessMessage ?: "",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "✕",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.clickable { onDismissMessageBanner() }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            when (uiState.activeMode) {
                BatchMode.STUDENTS -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Enrolled Students Roster",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Students can only login using admin-created profiles.",
                                fontSize = 12.sp,
                                color = MutedBodyText
                            )
                        }
                        IconButton(onClick = onRefreshUsers) {
                            Text(text = "↻", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DeepPlum)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.isUsersLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = DeepPlum,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Loading student roster from server...",
                                fontSize = 12.sp,
                                color = MutedBodyText
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (uiState.createdStudentSuccessName != null) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✓ Student profile & credentials created for ${uiState.createdStudentSuccessName}! Login status set to Active.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (uiState.showCreateStudentForm) {
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
                                    text = "Create Student Profile & Issue Credentials",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkMaroonText
                                )

                                Spacer(modifier = Modifier.height(12.dp))

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
                                            value = uiState.newStudentNameInput,
                                            onValueChange = onNewStudentNameChange,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(text = "Assign batch", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                Spacer(modifier = Modifier.height(4.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(modifier = Modifier.padding(12.dp)) {
                                        BasicTextField(
                                            value = uiState.newStudentBatchInput,
                                            onValueChange = onNewStudentBatchChange,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(text = "Student login username / ID", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                Spacer(modifier = Modifier.height(4.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(modifier = Modifier.padding(12.dp)) {
                                        BasicTextField(
                                            value = uiState.newStudentUsernameInput,
                                            onValueChange = onNewStudentUsernameChange,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(text = "Initial login password", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                Spacer(modifier = Modifier.height(4.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(modifier = Modifier.padding(12.dp)) {
                                        BasicTextField(
                                            value = uiState.newStudentPasswordInput,
                                            onValueChange = onNewStudentPasswordChange,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = onCreateStudentSubmit,
                                    enabled = !uiState.isCreatingUser,
                                    colors = ButtonDefaults.buttonColors(containerColor = SoftLavenderBadge, contentColor = DeepPlum),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                ) {
                                    Text(
                                        text = if (uiState.isCreatingUser) "Creating Account..." else "Create Profile & Issue Credentials",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepPlum
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            uiState.studentsList.forEachIndexed { index, student ->
                                val isExpanded = uiState.expandedStudentId == student.id
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onToggleExpandStudent(student.id) }
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
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
                                            Column {
                                                Text(
                                                    text = student.name,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DarkMaroonText
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = student.batchName,
                                                    fontSize = 12.sp,
                                                    color = MutedBodyText
                                                )
                                            }
                                        }

                                        Box(
                                            modifier = Modifier
                                                .background(if (isExpanded) SoftLavenderBadge else Color(0xFFE8F5E9), shape = RoundedCornerShape(10.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = if (isExpanded) "Hide Credentials" else "View Credentials",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isExpanded) DeepPlum else Color(0xFF2E7D32)
                                            )
                                        }
                                    }

                                    if (isExpanded) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(SoftLavenderBadge, shape = RoundedCornerShape(12.dp))
                                                .padding(12.dp)
                                        ) {
                                            Column {
                                                Text(
                                                    text = "STUDENT LOGIN CREDENTIALS (ADMIN ISSUED)",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepPlum
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Username / ID: ${student.username}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = DarkMaroonText
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "Password: ${student.passwordMasked}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = DarkMaroonText
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                OutlinedButton(
                                                    onClick = { onOpenResetPasswordDialog(student.numericId, student.name, "student") },
                                                    colors = ButtonDefaults.outlinedButtonColors(
                                                        containerColor = Color.White,
                                                        contentColor = DeepPlum
                                                    ),
                                                    border = BorderStroke(1.dp, DeepPlum),
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier.height(34.dp)
                                                ) {
                                                    Text(
                                                        text = "Admin Reset Password",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = DeepPlum
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                if (index < uiState.studentsList.size - 1) {
                                    HorizontalDivider(color = CardBorder, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onToggleCreateStudentForm,
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = SoftLavenderBadge, contentColor = DeepPlum),
                        border = BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text(
                            text = if (uiState.showCreateStudentForm) "Close Profile Form" else "+ Create New Student Profile & Credentials (Admin Only)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepPlum
                        )
                    }
                }

                BatchMode.STAFF -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Academy Staff & Instructors",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Staff roles, assigned batches, and salary information.",
                                fontSize = 12.sp,
                                color = MutedBodyText
                            )
                        }
                        IconButton(onClick = onRefreshUsers) {
                            Text(text = "↻", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DeepPlum)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.isUsersLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = DeepPlum,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Loading staff roster from server...",
                                fontSize = 12.sp,
                                color = MutedBodyText
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (uiState.createdStaffSuccessName != null) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✓ Staff profile & credentials created for ${uiState.createdStaffSuccessName}! Status set to Active.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (uiState.showCreateStaffForm) {
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
                                    text = "Create Staff Profile & Issue Credentials",
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
                                            value = uiState.newStaffNameInput,
                                            onValueChange = onNewStaffNameChange,
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
                                            value = uiState.newStaffRoleInput,
                                            onValueChange = onNewStaffRoleChange,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(text = "Assigned batches", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                Spacer(modifier = Modifier.height(4.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(modifier = Modifier.padding(12.dp)) {
                                        BasicTextField(
                                            value = uiState.newStaffBatchesInput,
                                            onValueChange = onNewStaffBatchesChange,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(text = "Monthly salary payout", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                Spacer(modifier = Modifier.height(4.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(modifier = Modifier.padding(12.dp)) {
                                        BasicTextField(
                                            value = uiState.newStaffSalaryInput,
                                            onValueChange = onNewStaffSalaryChange,
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
                                            value = uiState.newStaffUsernameInput,
                                            onValueChange = onNewStaffUsernameChange,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(text = "Initial login password", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                Spacer(modifier = Modifier.height(4.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(modifier = Modifier.padding(12.dp)) {
                                        BasicTextField(
                                            value = uiState.newStaffPasswordInput,
                                            onValueChange = onNewStaffPasswordChange,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = onCreateStaffSubmit,
                                    enabled = !uiState.isCreatingUser,
                                    colors = ButtonDefaults.buttonColors(containerColor = SoftLavenderBadge, contentColor = DeepPlum),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                ) {
                                    Text(
                                        text = if (uiState.isCreatingUser) "Creating Account..." else "Create Staff Profile & Issue Credentials",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepPlum
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            uiState.staffList.forEachIndexed { index, staff ->
                                val isExpanded = uiState.expandedStaffId == staff.id
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onToggleExpandStaff(staff.id) }
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = staff.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkMaroonText
                                        )

                                        Box(
                                            modifier = Modifier
                                                .background(SoftLavenderBadge, shape = RoundedCornerShape(10.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = if (isExpanded) "Hide Credentials" else "${staff.monthlySalary} · Credentials",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = DeepPlum
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "${staff.role} · Batches: ${staff.assignedBatches}",
                                        fontSize = 12.sp,
                                        color = MutedBodyText
                                    )

                                    if (isExpanded) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(SoftLavenderBadge, shape = RoundedCornerShape(12.dp))
                                                .padding(12.dp)
                                        ) {
                                            Column {
                                                Text(
                                                    text = "STAFF LOGIN CREDENTIALS",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepPlum
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Username / ID: ${staff.username}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = DarkMaroonText
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "Password: ${staff.passwordMasked}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = DarkMaroonText
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                OutlinedButton(
                                                    onClick = { onOpenResetPasswordDialog(staff.numericId, staff.name, "instructor") },
                                                    colors = ButtonDefaults.outlinedButtonColors(
                                                        containerColor = Color.White,
                                                        contentColor = DeepPlum
                                                    ),
                                                    border = BorderStroke(1.dp, DeepPlum),
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier.height(34.dp)
                                                ) {
                                                    Text(
                                                        text = "Admin Reset Password",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = DeepPlum
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                if (index < uiState.staffList.size - 1) {
                                    HorizontalDivider(color = CardBorder, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onToggleCreateStaffForm,
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = SoftLavenderBadge, contentColor = DeepPlum),
                        border = BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text(
                            text = if (uiState.showCreateStaffForm) "Close Staff Form" else "+ Create New Staff Profile & Credentials (Admin Only)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepPlum
                        )
                    }
                }

                BatchMode.BATCHES -> {
                    var batchDropdownExpanded by remember { mutableStateOf(false) }
                    var danceStyleExpanded by remember { mutableStateOf(false) }
                    var levelExpanded by remember { mutableStateOf(false) }
                    var instructorExpanded by remember { mutableStateOf(false) }
                    var studioExpanded by remember { mutableStateOf(false) }
                    var startTimeExpanded by remember { mutableStateOf(false) }
                    var endTimeExpanded by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.isCreateBatchMode) "Create New Batch" else "Selected batch",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMaroonText
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onRefreshBatches, modifier = Modifier.size(30.dp)) {
                                Text(text = "↻", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DeepPlum)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (uiState.isCreateBatchMode) DeepPlum else SoftLavenderBadge)
                                    .clickable { onToggleCreateBatchMode() }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (uiState.isCreateBatchMode) "Cancel / View Batches" else "+ New Batch",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (uiState.isCreateBatchMode) Color.White else DeepPlum
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (uiState.isBatchesLoading) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = DeepPlum, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Loading academy batches...", fontSize = 11.sp, color = MutedBodyText)
                        }
                    }

                    if (!uiState.isCreateBatchMode) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                            border = BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { batchDropdownExpanded = true }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = uiState.selectedBatchName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkMaroonText
                                )

                                Canvas(modifier = Modifier.size(12.dp)) {
                                    val w = size.width
                                    val h = size.height
                                    val stroke = 1.8.dp.toPx()
                                    drawLine(color = DeepPlum, start = Offset(0f, h * 0.3f), end = Offset(w * 0.5f, h * 0.8f), strokeWidth = stroke, cap = StrokeCap.Round)
                                    drawLine(color = DeepPlum, start = Offset(w * 0.5f, h * 0.8f), end = Offset(w, h * 0.3f), strokeWidth = stroke, cap = StrokeCap.Round)
                                }
                            }
                        }

                        DropdownMenu(
                            expanded = batchDropdownExpanded,
                            onDismissRequest = { batchDropdownExpanded = false },
                            containerColor = Color.White
                        ) {
                            uiState.batchOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(text = option, fontSize = 13.sp, color = DarkMaroonText) },
                                    colors = MenuDefaults.itemColors(
                                        textColor = DarkMaroonText
                                    ),
                                    onClick = {
                                        onSelectedBatchSelect(option)
                                        batchDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = uiState.statusBadge,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(SoftLavenderBadge, shape = RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = uiState.enrollmentBadge,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepPlum
                            )
                        }
                    }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Batch name",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkMaroonText
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(14.dp)) {
                            BasicTextField(
                                value = uiState.batchNameInput,
                                onValueChange = onBatchNameChange,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Dance style",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Box(modifier = Modifier.fillMaxWidth()) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { danceStyleExpanded = true }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = uiState.danceStyle, fontSize = 13.sp, color = DarkMaroonText)
                                        Canvas(modifier = Modifier.size(10.dp)) {
                                            val stroke = 1.5.dp.toPx()
                                            drawLine(color = DeepPlum, start = Offset(0f, size.height * 0.3f), end = Offset(size.width * 0.5f, size.height * 0.8f), strokeWidth = stroke)
                                            drawLine(color = DeepPlum, start = Offset(size.width * 0.5f, size.height * 0.8f), end = Offset(size.width, size.height * 0.3f), strokeWidth = stroke)
                                        }
                                    }
                                }

                                DropdownMenu(
                                    expanded = danceStyleExpanded,
                                    onDismissRequest = { danceStyleExpanded = false },
                                    containerColor = Color.White
                                ) {
                                    uiState.danceStyleOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(text = option, fontSize = 13.sp, color = DarkMaroonText) },
                                            colors = MenuDefaults.itemColors(
                                                textColor = DarkMaroonText
                                            ),
                                            onClick = {
                                                onDanceStyleSelect(option)
                                                danceStyleExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Level",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Box(modifier = Modifier.fillMaxWidth()) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { levelExpanded = true }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = uiState.level, fontSize = 13.sp, color = DarkMaroonText)
                                        Canvas(modifier = Modifier.size(10.dp)) {
                                            val stroke = 1.5.dp.toPx()
                                            drawLine(color = DeepPlum, start = Offset(0f, size.height * 0.3f), end = Offset(size.width * 0.5f, size.height * 0.8f), strokeWidth = stroke)
                                            drawLine(color = DeepPlum, start = Offset(size.width * 0.5f, size.height * 0.8f), end = Offset(size.width, size.height * 0.3f), strokeWidth = stroke)
                                        }
                                    }
                                }

                                DropdownMenu(
                                    expanded = levelExpanded,
                                    onDismissRequest = { levelExpanded = false },
                                    containerColor = Color.White
                                ) {
                                    uiState.levelOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(text = option, fontSize = 13.sp, color = DarkMaroonText) },
                                            colors = MenuDefaults.itemColors(
                                                textColor = DarkMaroonText
                                            ),
                                            onClick = {
                                                onLevelSelect(option)
                                                levelExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Assigned instructor",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkMaroonText
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                            border = BorderStroke(1.dp, CardBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { instructorExpanded = true }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = uiState.instructor, fontSize = 13.sp, color = DarkMaroonText)
                                Canvas(modifier = Modifier.size(10.dp)) {
                                    val stroke = 1.5.dp.toPx()
                                    drawLine(color = DeepPlum, start = Offset(0f, size.height * 0.3f), end = Offset(size.width * 0.5f, size.height * 0.8f), strokeWidth = stroke)
                                    drawLine(color = DeepPlum, start = Offset(size.width * 0.5f, size.height * 0.8f), end = Offset(size.width, size.height * 0.3f), strokeWidth = stroke)
                                }
                            }
                        }

                        DropdownMenu(
                            expanded = instructorExpanded,
                            onDismissRequest = { instructorExpanded = false },
                            containerColor = Color.White
                        ) {
                            uiState.instructorOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(text = option, fontSize = 13.sp, color = DarkMaroonText) },
                                    colors = MenuDefaults.itemColors(
                                        textColor = DarkMaroonText
                                    ),
                                    onClick = {
                                        onInstructorSelect(option)
                                        instructorExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Capacity", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                            Spacer(modifier = Modifier.height(6.dp))
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = CardBackground),
                                border = BorderStroke(1.dp, CardBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(modifier = Modifier.padding(14.dp)) {
                                    Text(text = uiState.capacity, fontSize = 13.sp, color = DarkMaroonText)
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Studio", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                            Spacer(modifier = Modifier.height(6.dp))

                            Box(modifier = Modifier.fillMaxWidth()) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { studioExpanded = true }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = uiState.studio, fontSize = 13.sp, color = DarkMaroonText)
                                        Canvas(modifier = Modifier.size(10.dp)) {
                                            val stroke = 1.5.dp.toPx()
                                            drawLine(color = DeepPlum, start = Offset(0f, size.height * 0.3f), end = Offset(size.width * 0.5f, size.height * 0.8f), strokeWidth = stroke)
                                            drawLine(color = DeepPlum, start = Offset(size.width * 0.5f, size.height * 0.8f), end = Offset(size.width, size.height * 0.3f), strokeWidth = stroke)
                                        }
                                    }
                                }

                                DropdownMenu(
                                    expanded = studioExpanded,
                                    onDismissRequest = { studioExpanded = false },
                                    containerColor = Color.White
                                ) {
                                    uiState.studioOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(text = option, fontSize = 13.sp, color = DarkMaroonText) },
                                            colors = MenuDefaults.itemColors(
                                                textColor = DarkMaroonText
                                            ),
                                            onClick = {
                                                onStudioSelect(option)
                                                studioExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Weekly meeting schedule",
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        color = DarkMaroonText
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        uiState.allWeekDays.forEach { day ->
                            val isSelected = uiState.selectedDays.contains(day)
                            val chipBg = if (isSelected) DeepPlum else SoftLavenderBadge
                            val chipTextColor = if (isSelected) Color.White else DarkMaroonText

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(chipBg)
                                    .clickable { onToggleDaySelect(day) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isSelected) "$day ✓" else day,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = chipTextColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Text(
                                text = "Same time on all selected days",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Start time", fontSize = 11.sp, color = MutedBodyText)
                                    Spacer(modifier = Modifier.height(4.dp))

                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { startTimeExpanded = true }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = uiState.startTime, fontSize = 13.sp, color = DarkMaroonText)
                                                Canvas(modifier = Modifier.size(14.dp)) {
                                                    drawCircle(color = DeepPlum, radius = size.width / 2, style = Stroke(width = 1.5.dp.toPx()))
                                                    drawLine(color = DeepPlum, start = Offset(size.width / 2, size.height / 2), end = Offset(size.width / 2, size.height * 0.2f), strokeWidth = 1.5.dp.toPx())
                                                    drawLine(color = DeepPlum, start = Offset(size.width / 2, size.height / 2), end = Offset(size.width * 0.75f, size.height / 2), strokeWidth = 1.5.dp.toPx())
                                                }
                                            }
                                        }

                                        DropdownMenu(
                                            expanded = startTimeExpanded,
                                            onDismissRequest = { startTimeExpanded = false },
                                            containerColor = Color.White
                                        ) {
                                            uiState.timeOptions.forEach { option ->
                                                DropdownMenuItem(
                                                    text = { Text(text = option, fontSize = 13.sp, color = DarkMaroonText) },
                                                    colors = MenuDefaults.itemColors(
                                                        textColor = DarkMaroonText
                                                    ),
                                                    onClick = {
                                                        onStartTimeSelect(option)
                                                        startTimeExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "End time", fontSize = 11.sp, color = MutedBodyText)
                                    Spacer(modifier = Modifier.height(4.dp))

                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = CardBackground),
                                            border = BorderStroke(1.dp, CardBorder),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { endTimeExpanded = true }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = uiState.endTime, fontSize = 13.sp, color = DarkMaroonText)
                                                Canvas(modifier = Modifier.size(14.dp)) {
                                                    drawCircle(color = DeepPlum, radius = size.width / 2, style = Stroke(width = 1.5.dp.toPx()))
                                                    drawLine(color = DeepPlum, start = Offset(size.width / 2, size.height / 2), end = Offset(size.width / 2, size.height * 0.2f), strokeWidth = 1.5.dp.toPx())
                                                    drawLine(color = DeepPlum, start = Offset(size.width / 2, size.height / 2), end = Offset(size.width * 0.75f, size.height / 2), strokeWidth = 1.5.dp.toPx())
                                                }
                                            }
                                        }

                                        DropdownMenu(
                                            expanded = endTimeExpanded,
                                            onDismissRequest = { endTimeExpanded = false },
                                            containerColor = Color.White
                                        ) {
                                            uiState.timeOptions.forEach { option ->
                                                DropdownMenuItem(
                                                    text = { Text(text = option, fontSize = 13.sp, color = DarkMaroonText) },
                                                    colors = MenuDefaults.itemColors(
                                                        textColor = DarkMaroonText
                                                    ),
                                                    onClick = {
                                                        onEndTimeSelect(option)
                                                        endTimeExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = uiState.timezoneNote,
                                fontSize = 11.sp,
                                color = MutedBodyText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Monthly tuition",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkMaroonText
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(14.dp)) {
                            BasicTextField(
                                value = uiState.monthlyTuition,
                                onValueChange = onTuitionChange,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleNotify() }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(if (uiState.notifyStudentsChecked) Color(0xFF2E7D32) else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.notifyStudentsChecked) {
                                Canvas(modifier = Modifier.size(12.dp)) {
                                    val w = size.width
                                    val h = size.height
                                    drawLine(color = Color.White, start = Offset(0f, h * 0.55f), end = Offset(w * 0.38f, h * 0.9f), strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round)
                                    drawLine(color = Color.White, start = Offset(w * 0.38f, h * 0.9f), end = Offset(w, h * 0.15f), strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Notify students and linked parents of changes",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkMaroonText
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
                                                    cornerRadius = CornerRadius(3f, 3f)
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
                                text = if (uiState.isSaved) "Batch and schedule saved successfully!" else uiState.conflictNotice,
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = DeepPlum
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedButton(
                        onClick = onSaveBatchClick,
                        enabled = !uiState.isSavingBatch,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SoftLavenderBadge,
                            contentColor = DeepPlum
                        ),
                        border = BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (uiState.isSavingBatch) "Saving..." else (if (uiState.isCreateBatchMode) "Create Batch & Schedule" else (if (uiState.isSaved) "Saved ✓" else "Save batch & schedule")),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepPlum
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Canvas(modifier = Modifier.size(16.dp)) {
                                val w = size.width
                                val h = size.height
                                val stroke = 2.dp.toPx()
                                drawLine(color = DeepPlum, start = Offset(0f, h * 0.55f), end = Offset(w * 0.38f, h * 0.9f), strokeWidth = stroke, cap = StrokeCap.Round)
                                drawLine(color = DeepPlum, start = Offset(w * 0.38f, h * 0.9f), end = Offset(w, h * 0.15f), strokeWidth = stroke, cap = StrokeCap.Round)
                            }
                        }
                    }

                    if (!uiState.isCreateBatchMode) {
                        Spacer(modifier = Modifier.height(22.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Batch Enrolled Students",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkMaroonText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${uiState.batchRosterStudents.size} students currently assigned",
                                    fontSize = 11.sp,
                                    color = MutedBodyText
                                )
                            }

                            OutlinedButton(
                                onClick = onOpenAssignStudentDialog,
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = SoftLavenderBadge, contentColor = DeepPlum),
                                border = BorderStroke(1.dp, CardBorder),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(
                                    text = "+ Assign Student",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepPlum
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (uiState.isRosterLoading) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = DeepPlum, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Loading batch students...", fontSize = 12.sp, color = MutedBodyText)
                            }
                        } else if (uiState.batchRosterStudents.isEmpty()) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = CardBackground),
                                border = BorderStroke(1.dp, CardBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(modifier = Modifier.padding(18.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "No students enrolled in this batch yet. Tap '+ Assign Student' to enroll a student.",
                                        fontSize = 12.sp,
                                        color = MutedBodyText
                                    )
                                }
                            }
                        } else {
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = CardBackground),
                                border = BorderStroke(1.dp, CardBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    uiState.batchRosterStudents.forEachIndexed { index, student ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .clip(CircleShape)
                                                        .background(SoftLavenderBadge),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = student.initials,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = DeepPlum
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(
                                                        text = student.name,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = DarkMaroonText
                                                    )
                                                    Text(
                                                        text = student.attendanceSummary ?: student.username,
                                                        fontSize = 11.sp,
                                                        color = MutedBodyText
                                                    )
                                                }
                                            }

                                            OutlinedButton(
                                                onClick = { onOpenRemoveStudentConfirmDialog(student.numericId, student.name) },
                                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = Color(0xFFC62828)),
                                                border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier.height(30.dp)
                                            ) {
                                                Text(text = "Remove", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                                            }
                                        }

                                        if (index < uiState.batchRosterStudents.size - 1) {
                                            HorizontalDivider(color = CardBorder, thickness = 1.dp, modifier = Modifier.padding(horizontal = 14.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (uiState.showAssignStudentDialog) {
        AlertDialog(
            onDismissRequest = onCloseAssignStudentDialog,
            containerColor = CreamBackground,
            title = {
                Text(
                    text = "Assign Student to ${uiState.selectedBatchName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkMaroonText
                )
            },
            text = {
                Column {
                    Text(
                        text = "Select an academy student to enroll into this batch roster:",
                        fontSize = 12.sp,
                        color = MutedBodyText
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val allStudents = uiState.studentsList
                    if (allStudents.isEmpty()) {
                        Text("No students available. Please create a student first.", fontSize = 12.sp, color = MutedBodyText)
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            allStudents.forEach { student ->
                                val isSelected = uiState.selectedStudentIdToAssign == student.numericId
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = if (isSelected) SoftLavenderBadge else CardBackground),
                                    border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, if (isSelected) DeepPlum else CardBorder),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { student.numericId?.let { onSelectStudentToAssign(it) } }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = student.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DarkMaroonText)
                                            Text(text = "Current: ${student.batchName}", fontSize = 10.sp, color = MutedBodyText)
                                        }
                                        if (isSelected) {
                                            Text(text = "✓ Selected", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepPlum)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (uiState.assignStudentError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.assignStudentError ?: "",
                            color = Color(0xFFC62828),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirmAssignStudent,
                    enabled = !uiState.isAssigningStudent && uiState.selectedStudentIdToAssign != null,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepPlum, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (uiState.isAssigningStudent) "Enrolling..." else "Confirm Enrollment",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onCloseAssignStudentDialog,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkMaroonText),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", fontSize = 12.sp)
                }
            }
        )
    }

    if (uiState.showRemoveStudentConfirmDialog) {
        AlertDialog(
            onDismissRequest = onCloseRemoveStudentConfirmDialog,
            containerColor = CreamBackground,
            title = {
                Text(
                    text = "Remove Student from Batch?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkMaroonText
                )
            },
            text = {
                Column {
                    Text(
                        text = "Are you sure you want to remove ${uiState.studentToRemoveName} from ${uiState.selectedBatchName}?",
                        fontSize = 13.sp,
                        color = DarkMaroonText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "The student will be unenrolled from this batch. Their user account and attendance history will remain safe.",
                        fontSize = 11.sp,
                        color = MutedBodyText
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirmRemoveStudent,
                    enabled = !uiState.isRemovingStudent,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828), contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (uiState.isRemovingStudent) "Removing..." else "Remove Student",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onCloseRemoveStudentConfirmDialog,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkMaroonText),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", fontSize = 12.sp)
                }
            }
        )
    }

    if (uiState.showResetPasswordDialog) {
        AlertDialog(
            onDismissRequest = onDismissResetPasswordDialog,
            containerColor = CreamBackground,
            title = {
                Text(
                    text = "Reset User Password",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DarkMaroonText
                )
            },
            text = {
                Column {
                    Text(
                        text = "Reset password for ${uiState.resetTargetUserName} (${uiState.resetTargetUserRole.replaceFirstChar { it.uppercase() }}).",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkMaroonText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "All active authentication tokens for this user will be revoked immediately.",
                        fontSize = 11.sp,
                        color = MutedBodyText
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "New Password (min 8 characters)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkMaroonText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CardBackground),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(12.dp)) {
                            BasicTextField(
                                value = uiState.newPasswordInput,
                                onValueChange = onResetPasswordInputChange,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    if (uiState.resetPasswordError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.resetPasswordError ?: "",
                            color = Color(0xFFC62828),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirmResetPassword,
                    enabled = !uiState.isResettingPassword && uiState.newPasswordInput.length >= 8,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepPlum, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (uiState.isResettingPassword) "Resetting..." else "Confirm Reset",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismissResetPasswordDialog,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkMaroonText),
                    border = BorderStroke(1.dp, CardBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", fontSize = 13.sp)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ManageBatchesScreenPreview() {
    NritahubTheme {
        ManageBatchesContent(
            uiState = ManageBatchesUiState(),
            onModeSelect = {},
            onSelectedBatchSelect = {},
            onBatchNameChange = {},
            onDanceStyleSelect = {},
            onLevelSelect = {},
            onInstructorSelect = {},
            onStudioSelect = {},
            onStartTimeSelect = {},
            onEndTimeSelect = {},
            onTuitionChange = {},
            onToggleNotify = {},
            onToggleDaySelect = {},
            onSaveBatchClick = {},
            onTabSelect = {},
            onBackClick = {}
        )
    }
}
