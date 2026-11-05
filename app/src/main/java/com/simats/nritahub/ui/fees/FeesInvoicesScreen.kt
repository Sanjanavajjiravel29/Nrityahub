package com.simats.nritahub.ui.fees

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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun FeesInvoicesScreen(
    viewModel: FeesInvoicesViewModel = remember { FeesInvoicesViewModel() },
    onBackToPrevious: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigationEvent) {
        when (uiState.navigationEvent) {
            FeesInvoicesNavigationEvent.NavigateBack -> {
                onBackToPrevious()
                viewModel.onNavigationHandled()
            }
            FeesInvoicesNavigationEvent.None -> {}
        }
    }

    FeesInvoicesContent(
        uiState = uiState,
        onGenerateInvoiceClick = viewModel::onGenerateInvoiceClicked,
        onBackClick = viewModel::onBackClicked,
        onRefreshClick = viewModel::fetchStudentFees
    )
}

@Composable
fun FeesInvoicesContent(
    uiState: FeesInvoicesUiState,
    onGenerateInvoiceClick: () -> Unit,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit = {}
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

            // Main Card Container
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
                    Text(
                        text = uiState.title,
                        fontFamily = FontFamily.Serif,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkMaroonText
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = uiState.subtitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = MutedBodyText
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Top Status Card (Paid Status Banner)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                        modifier = Modifier.fillMaxWidth()
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
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF2E7D32)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.size(16.dp)) {
                                        val w = size.width
                                        val h = size.height
                                        drawPath(
                                            path = Path().apply {
                                                moveTo(0f, h * 0.5f)
                                                lineTo(w * 0.35f, h * 0.85f)
                                                lineTo(w, h * 0.2f)
                                            },
                                            color = Color.White,
                                            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "Fee Status",
                                        fontSize = 11.sp,
                                        color = Color(0xFF2E7D32)
                                    )
                                    Text(
                                        text = "Paid",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    Text(
                                        text = "Your fee has been successfully paid.",
                                        fontSize = 12.sp,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Invoice Available",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Student Details Summary Card
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(SoftLavenderBadge),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "AS",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepPlum
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = uiState.studentName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkMaroonText
                                    )
                                    Text(
                                        text = "Student ID: ${uiState.studentId}",
                                        fontSize = 12.sp,
                                        color = MutedBodyText
                                    )
                                    Text(
                                        text = "Class: ${uiState.studentClass}",
                                        fontSize = 12.sp,
                                        color = MutedBodyText
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = CardBorder, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "Fee Amount", fontSize = 11.sp, color = MutedBodyText)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = uiState.paidFeeAmount, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                }

                                Column {
                                    Text(text = "Fee Period", fontSize = 11.sp, color = MutedBodyText)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = uiState.feePeriod, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                }

                                Column {
                                    Text(text = "Payment Date", fontSize = 11.sp, color = MutedBodyText)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = uiState.paymentDate, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onGenerateInvoiceClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00A651),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Canvas(modifier = Modifier.size(16.dp)) {
                                        val w = size.width
                                        val h = size.height
                                        drawPath(
                                            path = Path().apply {
                                                moveTo(0f, 0f)
                                                lineTo(w * 0.7f, 0f)
                                                lineTo(w, h * 0.3f)
                                                lineTo(w, h)
                                                lineTo(0f, h)
                                                close()
                                            },
                                            color = Color.White,
                                            style = Stroke(width = 1.8.dp.toPx())
                                        )
                                        drawLine(color = Color.White, start = Offset(w * 0.25f, h * 0.4f), end = Offset(w * 0.75f, h * 0.4f), strokeWidth = 1.5.dp.toPx())
                                        drawLine(color = Color.White, start = Offset(w * 0.25f, h * 0.65f), end = Offset(w * 0.6f, h * 0.65f), strokeWidth = 1.5.dp.toPx())
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "Generate Fee Invoice",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    // Official Generated FEE INVOICE Document Card (Renders when clicked)
                    if (uiState.isInvoiceGenerated) {
                        Spacer(modifier = Modifier.height(20.dp))

                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, CardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                // Invoice Top Header
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column {
                                        Text(
                                            text = "Nritya Hub",
                                            fontFamily = FontFamily.Serif,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepPlum
                                        )
                                        Text(
                                            text = "Dance Academy",
                                            fontSize = 11.sp,
                                            color = MutedBodyText
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "FEE INVOICE",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.8.sp,
                                            color = DeepPlum
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Invoice No: ${uiState.invoiceNo}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkMaroonText
                                        )
                                        Text(
                                            text = "Date: ${uiState.paymentDate}",
                                            fontSize = 11.sp,
                                            color = MutedBodyText
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                HorizontalDivider(color = CardBorder, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(14.dp))

                                // Student Details Grid
                                Text(
                                    text = "Student Details",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkMaroonText
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row {
                                        Text(text = "Student Name  : ", fontSize = 12.sp, color = MutedBodyText, modifier = Modifier.width(110.dp))
                                        Text(text = uiState.studentName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                    }
                                    Row {
                                        Text(text = "Student ID          : ", fontSize = 12.sp, color = MutedBodyText, modifier = Modifier.width(110.dp))
                                        Text(text = uiState.studentId, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                    }
                                    Row {
                                        Text(text = "Class                   : ", fontSize = 12.sp, color = MutedBodyText, modifier = Modifier.width(110.dp))
                                        Text(text = uiState.studentClass, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                    }
                                    Row {
                                        Text(text = "Fee Period         : ", fontSize = 12.sp, color = MutedBodyText, modifier = Modifier.width(110.dp))
                                        Text(text = uiState.feePeriod, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Itemized Table
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF6F2)),
                                    border = BorderStroke(1.dp, CardBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        // Header Row
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFFF3ECE5))
                                                .padding(horizontal = 14.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = "Description", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                            Text(text = "Amount (₹)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        }

                                        // Item Row
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = uiState.feePeriod, fontSize = 13.sp, color = DarkMaroonText)
                                            Text(text = uiState.paidFeeAmount, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DarkMaroonText)
                                        }

                                        HorizontalDivider(color = CardBorder, thickness = 1.dp)

                                        // Total Row
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFFF3ECE5))
                                                .padding(horizontal = 14.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = "Total Amount", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                            Text(text = uiState.paidFeeAmount, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkMaroonText)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Green Stamped PAID Box
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Canvas(modifier = Modifier.size(24.dp)) {
                                            val w = size.width
                                            val h = size.height
                                            drawLine(color = Color(0xFF00A651), start = Offset(0f, h / 2), end = Offset(w, h / 2), strokeWidth = 2.dp.toPx())
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFE8F5E9))
                                                .padding(horizontal = 20.dp, vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "PAID",
                                                fontFamily = FontFamily.Serif,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 2.sp,
                                                color = Color(0xFF00A651)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Canvas(modifier = Modifier.size(24.dp)) {
                                            val w = size.width
                                            val h = size.height
                                            drawLine(color = Color(0xFF00A651), start = Offset(0f, h / 2), end = Offset(w, h / 2), strokeWidth = 2.dp.toPx())
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Footer Thank You Note
                                Text(
                                    text = "Thank you for being a part of Nritya Hub!\nKeep dancing, keep growing!",
                                    fontSize = 11.sp,
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 16.sp,
                                    color = MutedBodyText,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card 2: Unpaid / Outstanding Fee Status Card
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
                                Text(
                                    text = "Fee Status",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkMaroonText
                                )

                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFFFEBEE), shape = RoundedCornerShape(12.dp))
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Not Paid",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFC62828)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = uiState.outstandingFeeAmount,
                                fontFamily = FontFamily.Serif,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkMaroonText
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            // Month shown directly under the amount
                            Text(
                                text = "${uiState.feePeriod} · Outstanding fee amount",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MutedBodyText
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Warning Dues Notice Box
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                Canvas(modifier = Modifier.size(20.dp)) {
                                    val r = size.width / 2
                                    drawCircle(color = Color(0xFFC62828), radius = r, style = Stroke(width = 1.8.dp.toPx()))
                                    drawLine(color = Color(0xFFC62828), start = Offset(r, r * 0.45f), end = Offset(r, r * 1.15f), strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round)
                                    drawCircle(color = Color(0xFFC62828), radius = 1.5.dp.toPx(), center = Offset(r, r * 1.5f))
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = uiState.clearDuesTitle,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkMaroonText
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = uiState.clearDuesNotice,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp,
                                        color = MutedBodyText
                                    )
                                }
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
fun FeesInvoicesScreenPreview() {
    NritahubTheme {
        FeesInvoicesContent(
            uiState = FeesInvoicesUiState(),
            onGenerateInvoiceClick = {},
            onBackClick = {}
        )
    }
}
