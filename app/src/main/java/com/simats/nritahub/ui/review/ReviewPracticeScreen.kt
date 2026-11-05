package com.simats.nritahub.ui.review

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.nritahub.R
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
fun ReviewPracticeScreen(
    viewModel: ReviewPracticeViewModel = remember { ReviewPracticeViewModel() },
    onBackToPrevious: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigationEvent) {
        when (uiState.navigationEvent) {
            ReviewPracticeNavigationEvent.NavigateBack -> {
                onBackToPrevious()
                viewModel.onNavigationHandled()
            }
            ReviewPracticeNavigationEvent.PublishFeedbackSuccess -> {
                viewModel.onNavigationHandled()
            }
            ReviewPracticeNavigationEvent.SaveDraftSuccess -> {
                viewModel.onNavigationHandled()
            }
            ReviewPracticeNavigationEvent.CreateHomeworkSuccess -> {
                viewModel.onNavigationHandled()
            }
            ReviewPracticeNavigationEvent.None -> {}
        }
    }

    ReviewPracticeContent(
        uiState = uiState,
        onFeedbackChange = viewModel::onFeedbackChanged,
        onNextStepChange = viewModel::onNextStepChanged,
        onChecklistItemToggle = viewModel::onChecklistItemToggled,
        onPublishFeedbackClick = viewModel::onPublishFeedbackClicked,
        onSaveDraftClick = viewModel::onSaveDraftClicked,
        onBackClick = viewModel::onBackClicked,
        onSelectSubmission = viewModel::selectSubmission,
        onFilterSelect = viewModel::setFilter,
        onOpenCreateHomework = viewModel::openCreateHomeworkDialog,
        onDismissCreateHomework = viewModel::dismissCreateHomeworkDialog,
        onCreateHomeworkClick = viewModel::createHomework,
        onNewBatchSelect = viewModel::onNewBatchSelected,
        onNewTitleChange = viewModel::onNewTitleChanged,
        onNewTopicChange = viewModel::onNewTopicChanged,
        onNewDescriptionChange = viewModel::onNewDescriptionChanged,
        onNewDueDateChange = viewModel::onNewDueDateChanged,
        onRetryClick = { viewModel.loadSubmissions() }
    )

    if (uiState.showCreateHomeworkDialog) {
        CreateHomeworkDialog(
            uiState = uiState,
            onDismiss = viewModel::dismissCreateHomeworkDialog,
            onConfirm = viewModel::createHomework,
            onBatchSelected = viewModel::onNewBatchSelected,
            onTitleChanged = viewModel::onNewTitleChanged,
            onTopicChanged = viewModel::onNewTopicChanged,
            onDescriptionChanged = viewModel::onNewDescriptionChanged,
            onDueDateChanged = viewModel::onNewDueDateChanged
        )
    }
}

@Composable
fun ReviewPracticeContent(
    uiState: ReviewPracticeUiState,
    onFeedbackChange: (String) -> Unit,
    onNextStepChange: (String) -> Unit,
    onChecklistItemToggle: (String) -> Unit = {},
    onPublishFeedbackClick: () -> Unit,
    onSaveDraftClick: () -> Unit,
    onBackClick: () -> Unit,
    onSelectSubmission: (Int) -> Unit = {},
    onFilterSelect: (String) -> Unit = {},
    onOpenCreateHomework: () -> Unit = {},
    onDismissCreateHomework: () -> Unit = {},
    onCreateHomeworkClick: () -> Unit = {},
    onNewBatchSelect: (Int) -> Unit = {},
    onNewTitleChange: (String) -> Unit = {},
    onNewTopicChange: (String) -> Unit = {},
    onNewDescriptionChange: (String) -> Unit = {},
    onNewDueDateChange: (String) -> Unit = {},
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

                // Header Action: "Assign Practice" button
                Box(
                    modifier = Modifier
                        .background(DeepPlum, shape = RoundedCornerShape(16.dp))
                        .clickable { onOpenCreateHomework() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "+ Assign Practice",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Title & Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = uiState.title,
                    fontFamily = FontFamily.Serif,
                    fontSize = 28.sp,
                    color = DarkMaroonText
                )

                Button(
                    onClick = onOpenCreateHomework,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepPlum,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text(
                        text = "+ Assign Practice",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

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

            // Filter Tabs (All / Pending / Reviewed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Pending", "Reviewed").forEach { filter ->
                    val isSelected = uiState.selectedFilter.equals(filter, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) DeepPlum else SoftLavenderBadge,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onFilterSelect(filter) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else DeepPlum
                        )
                    }
                }
            }

            // Empty Submissions State Card with prominent "+ Create & Assign Homework" action
            if (uiState.submissionsList.isEmpty() && !uiState.isLoading) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftLavenderBadge),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Student Submissions Yet",
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkMaroonText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Assign practice exercises or homework to your batches so students can submit their recordings for review.",
                            fontSize = 12.sp,
                            color = MutedBodyText,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onOpenCreateHomework,
                            colors = ButtonDefaults.buttonColors(containerColor = DeepPlum),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "+ Create & Assign Homework",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Submissions Selector Chips (Horizontal Scroll)
            if (uiState.submissionsList.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.submissionsList.forEach { sub ->
                        val isSelected = sub.id == uiState.selectedSubmissionId
                        val isSubReviewed = sub.status.equals("Reviewed", ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) CardBackground else Color.White,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .border(
                                    BorderStroke(
                                        if (isSelected) 2.dp else 1.dp,
                                        if (isSelected) DeepPlum else CardBorder
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { onSelectSubmission(sub.id) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = sub.studentName ?: "Student",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkMaroonText
                                )
                                Text(
                                    text = "${sub.topic} · ${if (isSubReviewed) "Reviewed" else "Pending"}",
                                    fontSize = 10.sp,
                                    color = if (isSubReviewed) Color(0xFF2E7D32) else Color(0xFFA05C1C)
                                )
                            }
                        }
                    }
                }
            }

            // Student Info Row
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
                            text = uiState.studentInitials,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepPlum
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = uiState.studentNameAndBatch,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkMaroonText
                    )
                }

                val isReviewed = uiState.statusBadge.equals("Reviewed", ignoreCase = true)
                Box(
                    modifier = Modifier
                        .background(
                            if (isReviewed) Color(0xFFE8F5E9) else Color(0xFFFDF3E5),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = uiState.statusBadge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isReviewed) Color(0xFF2E7D32) else Color(0xFFA05C1C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.submissionDetails,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MutedBodyText
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Video Player Card with Play Overlay
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.splash),
                        contentDescription = "Practice Recording",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Play Button Overlay
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(DeepPlum.copy(alpha = 0.85f))
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(20.dp)) {
                            val w = size.width
                            val h = size.height
                            drawPath(
                                path = Path().apply {
                                    moveTo(w * 0.3f, h * 0.15f)
                                    lineTo(w * 0.85f, h * 0.5f)
                                    lineTo(w * 0.3f, h * 0.85f)
                                    close()
                                },
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Student Note Box
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3ECE5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = uiState.studentNoteTitle,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = DeepPlum
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = uiState.studentNoteText,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = DarkMaroonText
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // "Instructor Evaluation Checklist" Section
            Column {
                Text(
                    text = uiState.checklistTitle,
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp,
                    color = DarkMaroonText
                )
                if (uiState.checklistSubtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = uiState.checklistSubtitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepPlum
                    )
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
                        .padding(16.dp)
                ) {
                    uiState.checklistItems.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onChecklistItemToggle(item.id) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = { onChecklistItemToggle(item.id) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = DeepPlum,
                                    uncheckedColor = MediumMaroonText,
                                    checkmarkColor = Color.White
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkMaroonText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.description,
                                    fontSize = 12.sp,
                                    color = MutedBodyText
                                )
                            }
                        }

                        if (index < uiState.checklistItems.size - 1) {
                            HorizontalDivider(
                                color = CardBorder,
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // "Instructor Comments" Section
            Column {
                Text(
                    text = uiState.commentBoxTitle,
                    fontFamily = FontFamily.Serif,
                    fontSize = 22.sp,
                    color = DarkMaroonText
                )
                if (uiState.commentBoxSubtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = uiState.commentBoxSubtitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepPlum
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Detailed Feedback Comment Box
            Text(
                text = "Detailed Feedback for Student",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DarkMaroonText
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = uiState.feedbackText,
                onValueChange = onFeedbackChange,
                minLines = 3,
                placeholder = {
                    Text(
                        text = "e.g. Your rhythm is settling beautifully! Practice relaxing your shoulders during the 3rd speed set.",
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

            Spacer(modifier = Modifier.height(14.dp))

            // Actionable Next Step Box
            Text(
                text = "Actionable Next Step for Student",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DarkMaroonText
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = uiState.nextStepText,
                onValueChange = onNextStepChange,
                minLines = 2,
                placeholder = {
                    Text(
                        text = "e.g. Practice a comfortable araimandi for 2 minutes daily before recording.",
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

            // Privacy / Visibility Notice Box
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
                                    moveTo(0f, h * 0.5f)
                                    cubicTo(w * 0.25f, h * 0.1f, w * 0.75f, h * 0.1f, w, h * 0.5f)
                                    cubicTo(w * 0.75f, h * 0.9f, w * 0.25f, h * 0.9f, 0f, h * 0.5f)
                                },
                                color = DeepPlum,
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                            drawCircle(color = DeepPlum, radius = 2.5f, center = Offset(w * 0.5f, h * 0.5f))
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = if (uiState.isPublished) "Feedback published successfully for student!" else uiState.privacyNotice,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = DeepPlum
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Primary Button ("Publish feedback")
            Button(
                onClick = onPublishFeedbackClick,
                enabled = !uiState.isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepPlum,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Saving...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.isPublished) "Published ✓" else "Publish feedback",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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

            Spacer(modifier = Modifier.height(12.dp))

            // Secondary Button ("Save as draft")
            OutlinedButton(
                onClick = onSaveDraftClick,
                enabled = !uiState.isSaving,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CardBackground,
                    contentColor = DeepPlum
                ),
                border = BorderStroke(1.dp, CardBorder),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = if (uiState.isDraftSaved) "Draft saved ✓" else "Save as draft",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepPlum
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHomeworkDialog(
    uiState: ReviewPracticeUiState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onBatchSelected: (Int) -> Unit,
    onTitleChanged: (String) -> Unit,
    onTopicChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDueDateChanged: (String) -> Unit
) {
    var isBatchDropdownExpanded by remember { mutableStateOf(false) }
    val selectedBatch = uiState.authorizedBatches.find { it.id == uiState.newHomeworkBatchId }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CreamBackground,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Assign Practice Homework",
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkMaroonText
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (uiState.createHomeworkError != null) {
                    Text(
                        text = uiState.createHomeworkError,
                        color = Color(0xFFC62828),
                        fontSize = 12.sp
                    )
                }

                // Batch Selector
                Text(
                    text = "Select Batch",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkMaroonText
                )
                ExposedDropdownMenuBox(
                    expanded = isBatchDropdownExpanded,
                    onExpandedChange = { isBatchDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedBatch?.batchName ?: "Select Batch",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isBatchDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardBackground,
                            unfocusedContainerColor = CardBackground,
                            focusedBorderColor = DeepPlum,
                            unfocusedBorderColor = CardBorder
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = isBatchDropdownExpanded,
                        onDismissRequest = { isBatchDropdownExpanded = false }
                    ) {
                        uiState.authorizedBatches.forEach { batch ->
                            DropdownMenuItem(
                                text = { Text("${batch.batchName} (${batch.level ?: "All"})") },
                                onClick = {
                                    onBatchSelected(batch.id)
                                    isBatchDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Title Field
                Text(
                    text = "Homework Title *",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkMaroonText
                )
                OutlinedTextField(
                    value = uiState.newHomeworkTitle,
                    onValueChange = onTitleChanged,
                    placeholder = { Text("e.g. Tatta Adavu Practice 2", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBackground,
                        unfocusedContainerColor = CardBackground,
                        focusedBorderColor = DeepPlum,
                        unfocusedBorderColor = CardBorder
                    )
                )

                // Topic Field
                Text(
                    text = "Dance Topic / Adavu",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkMaroonText
                )
                OutlinedTextField(
                    value = uiState.newHomeworkTopic,
                    onValueChange = onTopicChanged,
                    placeholder = { Text("e.g. Tatta Adavu Steps 1-4", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBackground,
                        unfocusedContainerColor = CardBackground,
                        focusedBorderColor = DeepPlum,
                        unfocusedBorderColor = CardBorder
                    )
                )

                // Description Field
                Text(
                    text = "Description / Instructions",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkMaroonText
                )
                OutlinedTextField(
                    value = uiState.newHomeworkDescription,
                    onValueChange = onDescriptionChanged,
                    placeholder = { Text("e.g. Record with steady aramandi in 3 speeds.", fontSize = 12.sp) },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBackground,
                        unfocusedContainerColor = CardBackground,
                        focusedBorderColor = DeepPlum,
                        unfocusedBorderColor = CardBorder
                    )
                )

                // Due Date Field
                Text(
                    text = "Due Date (YYYY-MM-DD)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkMaroonText
                )
                OutlinedTextField(
                    value = uiState.newHomeworkDueDate,
                    onValueChange = onDueDateChanged,
                    placeholder = { Text("2026-11-15", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardBackground,
                        unfocusedContainerColor = CardBackground,
                        focusedBorderColor = DeepPlum,
                        unfocusedBorderColor = CardBorder
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !uiState.isCreatingHomework,
                colors = ButtonDefaults.buttonColors(containerColor = DeepPlum),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (uiState.isCreatingHomework) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Assigning...")
                } else {
                    Text("Assign Homework", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkMaroonText)
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ReviewPracticeScreenPreview() {
    NritahubTheme {
        ReviewPracticeContent(
            uiState = ReviewPracticeUiState(),
            onFeedbackChange = {},
            onNextStepChange = {},
            onPublishFeedbackClick = {},
            onSaveDraftClick = {},
            onBackClick = {}
        )
    }
}
