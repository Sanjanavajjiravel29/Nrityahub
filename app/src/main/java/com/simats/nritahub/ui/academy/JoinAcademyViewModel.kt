package com.simats.nritahub.ui.academy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.nritahub.data.repository.AuthRepository
import com.simats.nritahub.data.repository.AuthResult
import com.simats.nritahub.data.session.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JoinAcademyViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private var sessionManager: UserSessionManager? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinAcademyUiState())
    val uiState: StateFlow<JoinAcademyUiState> = _uiState.asStateFlow()

    fun setSessionManager(manager: UserSessionManager) {
        this.sessionManager = manager
    }

    fun onUsernameOrEmailChanged(value: String) {
        _uiState.update {
            it.copy(
                usernameOrEmail = value,
                errorMessage = null
            )
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                errorMessage = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onSignInClicked() {
        val currentState = _uiState.value

        // Prevent repeated requests while a login call is active
        if (currentState.isLoading) return

        val identifier = currentState.usernameOrEmail.trim()
        val password = currentState.password

        // Client-side validation
        if (identifier.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your username or email address.") }
            return
        }

        if (password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your password.") }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            when (val result = authRepository.login(identifier, password)) {
                is AuthResult.Success -> {
                    val user = result.response.user
                    val role = (result.response.role ?: user?.role).orEmpty().trim().lowercase()

                    val validRoles = listOf("admin", "instructor", "student")
                    if (user != null && role in validRoles) {
                        // Save session securely (no password, no fake token)
                        sessionManager?.saveSession(
                            id = user.id,
                            fullName = user.fullName,
                            email = user.email,
                            username = user.username,
                            role = role,
                            staffRole = user.staffRole,
                            monthlySalary = user.monthlySalary,
                            batchId = user.batchId,
                            batchName = user.batchName,
                            bio = user.bio,
                            profileImageUrl = user.profileImageUrl
                        )

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                navigationEvent = JoinAcademyNavigationEvent.SignInSuccess(role)
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = if (role !in validRoles) {
                                    "Unrecognized account role: '$role'. Please contact your administrator."
                                } else {
                                    "Incomplete profile received from server."
                                }
                            )
                        }
                    }
                }
                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun onForgotPasswordClicked() {
        _uiState.update { it.copy(navigationEvent = JoinAcademyNavigationEvent.ForgotPassword) }
    }

    fun onSignUpClicked() {
        // Public signup is restricted per academy rules
        _uiState.update {
            it.copy(errorMessage = "Student and instructor accounts are created directly by Academy Administrators.")
        }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = JoinAcademyNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = JoinAcademyNavigationEvent.None) }
    }
}
