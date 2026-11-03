package com.simats.nritahub.ui.academy

sealed interface JoinAcademyNavigationEvent {
    data object None : JoinAcademyNavigationEvent
    data object NavigateBack : JoinAcademyNavigationEvent
    data object NavigateToSignUp : JoinAcademyNavigationEvent
    data object ForgotPassword : JoinAcademyNavigationEvent
    data class SignInSuccess(val role: String) : JoinAcademyNavigationEvent
}

data class JoinAcademyUiState(
    val headerNumber: String = "02",
    val title: String = "Welcome back",
    val subtitle: String = "Enter your credentials to access your Sri Kanchi Shethira Kala Mandir account.",
    val usernameOrEmail: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val academyName: String = "SRI KANCHI SHETHIRA KALA MANDIR",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigationEvent: JoinAcademyNavigationEvent = JoinAcademyNavigationEvent.None
)
