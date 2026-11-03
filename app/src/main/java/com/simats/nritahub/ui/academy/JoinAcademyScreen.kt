package com.simats.nritahub.ui.academy

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.nritahub.R
import com.simats.nritahub.data.session.UserSessionManager
import com.simats.nritahub.ui.theme.CardBackground
import com.simats.nritahub.ui.theme.CardBorder
import com.simats.nritahub.ui.theme.CreamBackground
import com.simats.nritahub.ui.theme.DarkMaroonText
import com.simats.nritahub.ui.theme.DeepPlum
import com.simats.nritahub.ui.theme.NritahubTheme

@Composable
fun JoinAcademyScreen(
    viewModel: JoinAcademyViewModel = remember { JoinAcademyViewModel() },
    onBackToSplash: () -> Unit,
    onSignInSuccess: (role: String) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.setSessionManager(UserSessionManager.getInstance(context))
    }

    LaunchedEffect(uiState.navigationEvent) {
        when (val event = uiState.navigationEvent) {
            JoinAcademyNavigationEvent.NavigateBack -> {
                onBackToSplash()
                viewModel.onNavigationHandled()
            }
            is JoinAcademyNavigationEvent.SignInSuccess -> {
                onSignInSuccess(event.role)
                viewModel.onNavigationHandled()
            }
            JoinAcademyNavigationEvent.NavigateToSignUp -> {
                onNavigateToSignUp()
                viewModel.onNavigationHandled()
            }
            JoinAcademyNavigationEvent.ForgotPassword -> {
                viewModel.onNavigationHandled()
            }
            JoinAcademyNavigationEvent.None -> {}
        }
    }

    JoinAcademyContent(
        uiState = uiState,
        onUsernameOrEmailChange = viewModel::onUsernameOrEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        onSignInClick = viewModel::onSignInClicked,
        onForgotPasswordClick = viewModel::onForgotPasswordClicked,
        onSignUpClick = viewModel::onSignUpClicked,
        onBackClick = viewModel::onBackClicked
    )
}

@Composable
fun JoinAcademyContent(
    uiState: JoinAcademyUiState,
    onUsernameOrEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = CreamBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Full Screen Backdrop Image
            Image(
                painter = painterResource(id = R.drawable.splash),
                contentDescription = "Log In Backdrop",
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize()
            )

            // Dark Gradient Scrim Overlay for Contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.35f),
                                Color.Black.copy(alpha = 0.6f),
                                Color.Black.copy(alpha = 0.88f)
                            )
                        )
                    )
            )

            // Content Column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header Row with Back Button only
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            val w = size.width
                            val h = size.height
                            val stroke = 2.dp.toPx()
                            drawLine(
                                color = Color.White,
                                start = Offset(w, h / 2),
                                end = Offset(0f, h / 2),
                                strokeWidth = stroke,
                                cap = StrokeCap.Round
                            )
                            drawLine(
                                color = Color.White,
                                start = Offset(w * 0.4f, h * 0.2f),
                                end = Offset(0f, h / 2),
                                strokeWidth = stroke,
                                cap = StrokeCap.Round
                            )
                            drawLine(
                                color = Color.White,
                                start = Offset(w * 0.4f, h * 0.8f),
                                end = Offset(0f, h / 2),
                                strokeWidth = stroke,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Credentials Form Section
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Title: "Welcome back"
                    Text(
                        text = uiState.title,
                        fontFamily = FontFamily.Serif,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Academy Badge Pill
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = uiState.academyName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = uiState.subtitle,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Email ID or Username Field
                    Text(
                        text = "Email ID or Username",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = uiState.usernameOrEmail,
                        onValueChange = onUsernameOrEmailChange,
                        enabled = !uiState.isLoading,
                        placeholder = { Text("e.g. ananya@example.com", fontSize = 13.sp, color = Color.Gray) },
                        singleLine = true,
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

                    // Password Field
                    Text(
                        text = "Password",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = onPasswordChange,
                        enabled = !uiState.isLoading,
                        placeholder = { Text("Enter your password", fontSize = 13.sp, color = Color.Gray) },
                        singleLine = true,
                        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardBackground,
                            unfocusedContainerColor = CardBackground,
                            focusedBorderColor = DeepPlum,
                            unfocusedBorderColor = CardBorder
                        ),
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable(enabled = !uiState.isLoading) { onTogglePasswordVisibility() },
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.size(18.dp)) {
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
                                    drawCircle(color = DeepPlum, radius = 3.dp.toPx(), center = Offset(w * 0.5f, h * 0.5f))
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Forgot Password Link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Forgot password?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.clickable(enabled = !uiState.isLoading) { onForgotPasswordClick() }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Error Message Banner
                    if (uiState.errorMessage != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFB00020).copy(alpha = 0.85f))
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = uiState.errorMessage,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    } else {
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Primary Action Button ("Log in" in Dark Maroon with Bold White Text)
                    Button(
                        onClick = onSignInClick,
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkMaroonText,
                            contentColor = Color.White,
                            disabledContainerColor = DarkMaroonText.copy(alpha = 0.6f),
                            disabledContentColor = Color.White.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(26.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Log in",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Canvas(modifier = Modifier.size(16.dp)) {
                                    val w = size.width
                                    val h = size.height
                                    val stroke = 2.dp.toPx()
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(0f, h / 2),
                                        end = Offset(w, h / 2),
                                        strokeWidth = stroke,
                                        cap = StrokeCap.Round
                                    )
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(w * 0.6f, h * 0.2f),
                                        end = Offset(w, h / 2),
                                        strokeWidth = stroke,
                                        cap = StrokeCap.Round
                                    )
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(w * 0.6f, h * 0.8f),
                                        end = Offset(w, h / 2),
                                        strokeWidth = stroke,
                                        cap = StrokeCap.Round
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JoinAcademyScreenPreview() {
    NritahubTheme {
        JoinAcademyContent(
            uiState = JoinAcademyUiState(),
            onUsernameOrEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onSignInClick = {},
            onForgotPasswordClick = {},
            onSignUpClick = {},
            onBackClick = {}
        )
    }
}
