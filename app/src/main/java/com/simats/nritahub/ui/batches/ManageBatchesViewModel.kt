package com.simats.nritahub.ui.batches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.nritahub.data.remote.dto.BatchDto
import com.simats.nritahub.data.remote.dto.CreateBatchRequest
import com.simats.nritahub.data.remote.dto.CreateUserRequest
import com.simats.nritahub.data.remote.dto.UpdateBatchRequest
import com.simats.nritahub.data.remote.dto.UserDto
import com.simats.nritahub.data.repository.AdminRepository
import com.simats.nritahub.ui.admin.AdminTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageBatchesViewModel(
    private val adminRepository: AdminRepository = AdminRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageBatchesUiState())
    val uiState: StateFlow<ManageBatchesUiState> = _uiState.asStateFlow()

    init {
        fetchUsers()
        fetchBatches()
    }

    // --- Users Management ---

    fun fetchUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isUsersLoading = true, userManagementErrorMessage = null) }
            val result = adminRepository.listUsers()
            result.onSuccess { users ->
                val students = users
                    .filter { it.role.equals("student", ignoreCase = true) }
                    .map { it.toEnrolledStudentAdminItem() }

                val staff = users
                    .filter { it.role.equals("instructor", ignoreCase = true) || it.role.equals("admin", ignoreCase = true) }
                    .map { it.toStaffMemberItem() }

                val instructorNames = staff.map { it.name }.distinct()

                _uiState.update { current ->
                    current.copy(
                        studentsList = students,
                        staffList = staff,
                        instructorOptions = if (instructorNames.isNotEmpty()) instructorNames else current.instructorOptions,
                        isUsersLoading = false,
                        headerNumber = users.size.toString()
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isUsersLoading = false,
                        userManagementErrorMessage = error.localizedMessage ?: "Failed to load academy users."
                    )
                }
            }
        }
    }

    // --- Batches Management ---

    fun fetchBatches() {
        viewModelScope.launch {
            _uiState.update { it.copy(isBatchesLoading = true) }
            val result = adminRepository.listBatches()
            result.onSuccess { batches ->
                val options = batches.map { it.batchName }
                val currentSelectedName = _uiState.value.selectedBatchName

                // Match existing selection or fallback to first batch
                val selectedBatch = batches.find { it.batchName == currentSelectedName } ?: batches.firstOrNull()

                _uiState.update { current ->
                    if (selectedBatch != null && !current.isCreateBatchMode) {
                        current.copy(
                            batchesList = batches,
                            batchOptions = if (options.isNotEmpty()) options else current.batchOptions,
                            isBatchesLoading = false,
                            selectedBatchId = selectedBatch.id,
                            selectedBatchName = selectedBatch.batchName,
                            batchNameInput = selectedBatch.batchName,
                            danceStyle = selectedBatch.danceStyle ?: current.danceStyle,
                            level = selectedBatch.level ?: current.level,
                            instructor = selectedBatch.instructorName ?: current.instructor,
                            instructorId = selectedBatch.instructorId,
                            capacity = "${selectedBatch.capacity ?: 24} students",
                            studio = selectedBatch.studio ?: current.studio,
                            selectedDays = selectedBatch.daysList ?: current.selectedDays,
                            startTime = selectedBatch.startTime ?: current.startTime,
                            endTime = selectedBatch.endTime ?: current.endTime,
                            monthlyTuition = selectedBatch.monthlyTuition ?: current.monthlyTuition,
                            statusBadge = selectedBatch.status ?: "Active",
                            enrollmentBadge = selectedBatch.enrollmentBadge ?: "${selectedBatch.enrolledCount ?: 0} enrolled · ${selectedBatch.spacesLeft ?: 0} spaces"
                        )
                    } else {
                        current.copy(
                            batchesList = batches,
                            batchOptions = if (options.isNotEmpty()) options else current.batchOptions,
                            isBatchesLoading = false
                        )
                    }
                }

                // If a batch is selected, fetch its live roster
                selectedBatch?.let { fetchBatchRoster(it.id) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isBatchesLoading = false,
                        userManagementErrorMessage = error.localizedMessage ?: "Failed to load batches."
                    )
                }
            }
        }
    }

    fun onSelectedBatchChanged(batchName: String) {
        val matchedBatch = _uiState.value.batchesList.find { it.batchName == batchName }
        if (matchedBatch != null) {
            _uiState.update { current ->
                current.copy(
                    isCreateBatchMode = false,
                    isSaved = false,
                    selectedBatchId = matchedBatch.id,
                    selectedBatchName = matchedBatch.batchName,
                    batchNameInput = matchedBatch.batchName,
                    danceStyle = matchedBatch.danceStyle ?: current.danceStyle,
                    level = matchedBatch.level ?: current.level,
                    instructor = matchedBatch.instructorName ?: current.instructor,
                    instructorId = matchedBatch.instructorId,
                    capacity = "${matchedBatch.capacity ?: 24} students",
                    studio = matchedBatch.studio ?: current.studio,
                    selectedDays = matchedBatch.daysList ?: current.selectedDays,
                    startTime = matchedBatch.startTime ?: current.startTime,
                    endTime = matchedBatch.endTime ?: current.endTime,
                    monthlyTuition = matchedBatch.monthlyTuition ?: current.monthlyTuition,
                    statusBadge = matchedBatch.status ?: "Active",
                    enrollmentBadge = matchedBatch.enrollmentBadge ?: "${matchedBatch.enrolledCount ?: 0} enrolled · ${matchedBatch.spacesLeft ?: 0} spaces"
                )
            }
            fetchBatchRoster(matchedBatch.id)
        } else {
            _uiState.update { it.copy(selectedBatchName = batchName, batchNameInput = batchName) }
        }
    }

    fun fetchBatchRoster(batchId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRosterLoading = true) }
            val result = adminRepository.getBatch(batchId)
            result.onSuccess { details ->
                val students = details.students.map { it.toEnrolledStudentAdminItem() }
                _uiState.update {
                    it.copy(
                        batchRosterStudents = students,
                        isRosterLoading = false,
                        enrollmentBadge = "${details.enrolledCount} enrolled · ${maxOf(0, (details.batch?.capacity ?: 24) - details.enrolledCount)} spaces"
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isRosterLoading = false) }
            }
        }
    }

    fun toggleCreateBatchMode() {
        _uiState.update { current ->
            val nextMode = !current.isCreateBatchMode
            if (nextMode) {
                current.copy(
                    isCreateBatchMode = true,
                    isSaved = false,
                    batchNameInput = "New Bharatanatyam Batch",
                    danceStyle = "Bharatanatyam",
                    level = "Foundation",
                    capacity = "24 students",
                    studio = "Studio 1",
                    selectedDays = listOf("Tue", "Thu", "Sat"),
                    startTime = "6:00 PM",
                    endTime = "7:00 PM",
                    monthlyTuition = "₹2,400",
                    statusBadge = "Active"
                )
            } else {
                current.copy(isCreateBatchMode = false)
            }
        }
    }

    fun onSaveBatchClicked() {
        val currentState = _uiState.value
        val name = currentState.batchNameInput.trim()

        if (name.isBlank()) {
            _uiState.update { it.copy(userManagementErrorMessage = "Batch name is required.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingBatch = true, userManagementErrorMessage = null) }

            val cleanCapacity = currentState.capacity.filter { it.isDigit() }.toIntOrNull() ?: 24

            if (currentState.isCreateBatchMode) {
                // Create New Batch
                val createReq = CreateBatchRequest(
                    batchName = name,
                    danceStyle = currentState.danceStyle,
                    level = currentState.level,
                    instructorName = currentState.instructor,
                    capacity = cleanCapacity,
                    studio = currentState.studio,
                    days = currentState.selectedDays.joinToString(", "),
                    startTime = currentState.startTime,
                    endTime = currentState.endTime,
                    monthlyTuition = currentState.monthlyTuition,
                    status = currentState.statusBadge
                )

                val result = adminRepository.createBatch(createReq)
                result.onSuccess { created ->
                    _uiState.update {
                        it.copy(
                            isSavingBatch = false,
                            isSaved = true,
                            isCreateBatchMode = false,
                            selectedBatchId = created.id,
                            selectedBatchName = created.batchName,
                            userManagementSuccessMessage = "✓ Batch '${created.batchName}' created successfully!"
                        )
                    }
                    fetchBatches()
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSavingBatch = false,
                            userManagementErrorMessage = error.localizedMessage ?: "Failed to create batch."
                        )
                    }
                }
            } else {
                // Update Existing Batch
                val batchId = currentState.selectedBatchId
                if (batchId == null) {
                    _uiState.update { it.copy(isSavingBatch = false, userManagementErrorMessage = "No batch selected for editing.") }
                    return@launch
                }

                val updateReq = UpdateBatchRequest(
                    id = batchId,
                    batchName = name,
                    danceStyle = currentState.danceStyle,
                    level = currentState.level,
                    instructorName = currentState.instructor,
                    capacity = cleanCapacity,
                    studio = currentState.studio,
                    days = currentState.selectedDays.joinToString(", "),
                    startTime = currentState.startTime,
                    endTime = currentState.endTime,
                    monthlyTuition = currentState.monthlyTuition,
                    status = currentState.statusBadge
                )

                val result = adminRepository.updateBatch(updateReq)
                result.onSuccess { updated ->
                    _uiState.update {
                        it.copy(
                            isSavingBatch = false,
                            isSaved = true,
                            selectedBatchName = updated.batchName,
                            userManagementSuccessMessage = "✓ Batch '${updated.batchName}' updated successfully!"
                        )
                    }
                    fetchBatches()
                }.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSavingBatch = false,
                            userManagementErrorMessage = error.localizedMessage ?: "Failed to update batch."
                        )
                    }
                }
            }
        }
    }

    // --- Assign & Remove Students ---

    fun openAssignStudentDialog() {
        _uiState.update {
            it.copy(
                showAssignStudentDialog = true,
                selectedStudentIdToAssign = null,
                assignStudentError = null
            )
        }
    }

    fun onSelectStudentToAssign(studentId: Int) {
        _uiState.update { it.copy(selectedStudentIdToAssign = studentId, assignStudentError = null) }
    }

    fun closeAssignStudentDialog() {
        _uiState.update {
            it.copy(
                showAssignStudentDialog = false,
                selectedStudentIdToAssign = null,
                assignStudentError = null,
                isAssigningStudent = false
            )
        }
    }

    fun confirmAssignStudent() {
        val currentState = _uiState.value
        val batchId = currentState.selectedBatchId
        val studentId = currentState.selectedStudentIdToAssign

        if (batchId == null) {
            _uiState.update { it.copy(assignStudentError = "Please select a batch first.") }
            return
        }

        if (studentId == null) {
            _uiState.update { it.copy(assignStudentError = "Please select a student to enroll.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isAssigningStudent = true, assignStudentError = null) }
            val result = adminRepository.assignStudent(batchId = batchId, studentId = studentId)
            result.onSuccess { msg ->
                _uiState.update {
                    it.copy(
                        showAssignStudentDialog = false,
                        isAssigningStudent = false,
                        selectedStudentIdToAssign = null,
                        userManagementSuccessMessage = "✓ $msg"
                    )
                }
                fetchBatchRoster(batchId)
                fetchBatches()
                fetchUsers()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isAssigningStudent = false,
                        assignStudentError = error.localizedMessage ?: "Failed to assign student."
                    )
                }
            }
        }
    }

    fun openRemoveStudentConfirmDialog(studentId: Int?, studentName: String) {
        _uiState.update {
            it.copy(
                showRemoveStudentConfirmDialog = true,
                studentToRemoveId = studentId,
                studentToRemoveName = studentName
            )
        }
    }

    fun closeRemoveStudentConfirmDialog() {
        _uiState.update {
            it.copy(
                showRemoveStudentConfirmDialog = false,
                studentToRemoveId = null,
                studentToRemoveName = "",
                isRemovingStudent = false
            )
        }
    }

    fun confirmRemoveStudent() {
        val currentState = _uiState.value
        val batchId = currentState.selectedBatchId
        val studentId = currentState.studentToRemoveId
        val studentName = currentState.studentToRemoveName

        if (batchId == null || studentId == null) {
            closeRemoveStudentConfirmDialog()
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isRemovingStudent = true) }
            val result = adminRepository.removeStudent(batchId = batchId, studentId = studentId)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        showRemoveStudentConfirmDialog = false,
                        isRemovingStudent = false,
                        studentToRemoveId = null,
                        studentToRemoveName = "",
                        userManagementSuccessMessage = "✓ $studentName successfully unenrolled from batch."
                    )
                }
                fetchBatchRoster(batchId)
                fetchBatches()
                fetchUsers()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        showRemoveStudentConfirmDialog = false,
                        isRemovingStudent = false,
                        userManagementErrorMessage = error.localizedMessage ?: "Failed to remove student from batch."
                    )
                }
            }
        }
    }

    // --- Mode, Field Changes & Existing Operations ---

    fun onBatchModeSelected(mode: BatchMode) {
        _uiState.update { it.copy(activeMode = mode) }
    }

    fun onDanceStyleChanged(style: String) {
        _uiState.update { it.copy(danceStyle = style, isSaved = false) }
    }

    fun onLevelChanged(level: String) {
        _uiState.update { it.copy(level = level, isSaved = false) }
    }

    fun onInstructorChanged(instructor: String) {
        _uiState.update { it.copy(instructor = instructor, isSaved = false) }
    }

    fun onStudioChanged(studio: String) {
        _uiState.update { it.copy(studio = studio, isSaved = false) }
    }

    fun onStartTimeChanged(time: String) {
        _uiState.update { it.copy(startTime = time, isSaved = false) }
    }

    fun onEndTimeChanged(time: String) {
        _uiState.update { it.copy(endTime = time, isSaved = false) }
    }

    fun onBatchNameChanged(name: String) {
        _uiState.update { it.copy(batchNameInput = name, isSaved = false) }
    }

    fun onTuitionChanged(tuition: String) {
        _uiState.update { it.copy(monthlyTuition = tuition, isSaved = false) }
    }

    fun toggleCreateStudentForm() {
        _uiState.update { it.copy(showCreateStudentForm = !it.showCreateStudentForm, userManagementErrorMessage = null) }
    }

    fun onNewStudentNameChanged(name: String) {
        _uiState.update { it.copy(newStudentNameInput = name) }
    }

    fun onNewStudentBatchChanged(batch: String) {
        _uiState.update { it.copy(newStudentBatchInput = batch) }
    }

    fun onNewStudentUsernameChanged(username: String) {
        _uiState.update { it.copy(newStudentUsernameInput = username) }
    }

    fun onNewStudentPasswordChanged(password: String) {
        _uiState.update { it.copy(newStudentPasswordInput = password) }
    }

    fun toggleExpandStudent(id: String) {
        _uiState.update { currentState ->
            val nextId = if (currentState.expandedStudentId == id) null else id
            currentState.copy(expandedStudentId = nextId)
        }
    }

    fun toggleExpandStaff(id: String) {
        _uiState.update { currentState ->
            val nextId = if (currentState.expandedStaffId == id) null else id
            currentState.copy(expandedStaffId = nextId)
        }
    }

    fun onCreateStudentSubmit() {
        val currentState = _uiState.value
        val name = currentState.newStudentNameInput.trim()
        val password = currentState.newStudentPasswordInput.trim()
        val username = currentState.newStudentUsernameInput.trim()
        val batchName = currentState.newStudentBatchInput.trim()

        if (name.isBlank()) {
            _uiState.update { it.copy(userManagementErrorMessage = "Student full name is required.") }
            return
        }

        if (password.length < 8) {
            _uiState.update { it.copy(userManagementErrorMessage = "Initial password must be at least 8 characters.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingUser = true, userManagementErrorMessage = null) }

            val request = CreateUserRequest(
                fullName = name,
                username = username.ifBlank { null },
                password = password,
                role = "student",
                batchName = batchName.ifBlank { null }
            )

            val result = adminRepository.createUser(request)
            result.onSuccess { createdUser ->
                val newItem = EnrolledStudentAdminItem(
                    id = createdUser.id.toString(),
                    initials = createdUser.initials ?: extractInitials(createdUser.fullName),
                    name = createdUser.fullName,
                    batchName = createdUser.batchName ?: batchName.ifBlank { "Not Assigned" },
                    loginStatus = "Login Active",
                    username = createdUser.username ?: createdUser.email,
                    passwordMasked = password,
                    numericId = createdUser.id,
                    email = createdUser.email
                )

                _uiState.update {
                    it.copy(
                        studentsList = listOf(newItem) + it.studentsList,
                        showCreateStudentForm = false,
                        isProfileCreated = true,
                        isCreatingUser = false,
                        createdStudentSuccessName = name,
                        userManagementSuccessMessage = "✓ Student profile & credentials successfully issued for $name!",
                        newStudentNameInput = "",
                        newStudentBatchInput = "",
                        newStudentUsernameInput = "",
                        newStudentPasswordInput = ""
                    )
                }
                fetchUsers()
                _uiState.value.selectedBatchId?.let { fetchBatchRoster(it) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isCreatingUser = false,
                        userManagementErrorMessage = error.localizedMessage ?: "Failed to create student account."
                    )
                }
            }
        }
    }

    fun toggleCreateStaffForm() {
        _uiState.update { it.copy(showCreateStaffForm = !it.showCreateStaffForm, userManagementErrorMessage = null) }
    }

    fun onNewStaffNameChanged(name: String) {
        _uiState.update { it.copy(newStaffNameInput = name) }
    }

    fun onNewStaffRoleChanged(role: String) {
        _uiState.update { it.copy(newStaffRoleInput = role) }
    }

    fun onNewStaffBatchesChanged(batches: String) {
        _uiState.update { it.copy(newStaffBatchesInput = batches) }
    }

    fun onNewStaffSalaryChanged(salary: String) {
        _uiState.update { it.copy(newStaffSalaryInput = salary) }
    }

    fun onNewStaffUsernameChanged(username: String) {
        _uiState.update { it.copy(newStaffUsernameInput = username) }
    }

    fun onNewStaffPasswordChanged(password: String) {
        _uiState.update { it.copy(newStaffPasswordInput = password) }
    }

    fun onCreateStaffSubmit() {
        val currentState = _uiState.value
        val name = currentState.newStaffNameInput.trim()
        val role = currentState.newStaffRoleInput.trim()
        val batches = currentState.newStaffBatchesInput.trim()
        val salary = currentState.newStaffSalaryInput.trim()
        val username = currentState.newStaffUsernameInput.trim()
        val password = currentState.newStaffPasswordInput.trim()

        if (name.isBlank()) {
            _uiState.update { it.copy(userManagementErrorMessage = "Staff full name is required.") }
            return
        }

        if (password.length < 8) {
            _uiState.update { it.copy(userManagementErrorMessage = "Initial password must be at least 8 characters.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingUser = true, userManagementErrorMessage = null) }

            val request = CreateUserRequest(
                fullName = name,
                username = username.ifBlank { null },
                password = password,
                role = "instructor",
                staffRole = role.ifBlank { "Instructor" },
                batchName = batches.ifBlank { null },
                monthlySalary = salary.ifBlank { null }
            )

            val result = adminRepository.createUser(request)
            result.onSuccess { createdUser ->
                val newItem = StaffMemberItem(
                    id = createdUser.id.toString(),
                    name = createdUser.fullName,
                    role = createdUser.staffRole ?: role.ifBlank { "Instructor" },
                    assignedBatches = createdUser.batchName ?: batches.ifBlank { "All Academy Batches" },
                    monthlySalary = createdUser.monthlySalary ?: salary.ifBlank { "₹0 / mo" },
                    status = "Active",
                    username = createdUser.username ?: createdUser.email,
                    passwordMasked = password,
                    numericId = createdUser.id,
                    email = createdUser.email
                )

                _uiState.update {
                    it.copy(
                        staffList = listOf(newItem) + it.staffList,
                        showCreateStaffForm = false,
                        isProfileCreated = true,
                        isCreatingUser = false,
                        createdStaffSuccessName = name,
                        userManagementSuccessMessage = "✓ Staff profile & credentials successfully issued for $name!",
                        newStaffNameInput = "",
                        newStaffRoleInput = "",
                        newStaffBatchesInput = "",
                        newStaffSalaryInput = "",
                        newStaffUsernameInput = "",
                        newStaffPasswordInput = ""
                    )
                }
                fetchUsers()
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isCreatingUser = false,
                        userManagementErrorMessage = error.localizedMessage ?: "Failed to create staff account."
                    )
                }
            }
        }
    }

    // --- Admin Password Reset ---

    fun openResetPasswordDialog(userId: Int?, userName: String, role: String) {
        _uiState.update {
            it.copy(
                showResetPasswordDialog = true,
                resetTargetUserId = userId,
                resetTargetUserName = userName,
                resetTargetUserRole = role,
                newPasswordInput = "",
                resetPasswordError = null
            )
        }
    }

    fun onResetPasswordInputChanged(newPassword: String) {
        _uiState.update { it.copy(newPasswordInput = newPassword, resetPasswordError = null) }
    }

    fun closeResetPasswordDialog() {
        _uiState.update {
            it.copy(
                showResetPasswordDialog = false,
                resetTargetUserId = null,
                resetTargetUserName = "",
                resetTargetUserRole = "",
                newPasswordInput = "",
                resetPasswordError = null,
                isResettingPassword = false
            )
        }
    }

    fun confirmResetPassword() {
        val currentState = _uiState.value
        val userId = currentState.resetTargetUserId
        val newPassword = currentState.newPasswordInput.trim()
        val targetName = currentState.resetTargetUserName

        if (userId == null) {
            _uiState.update { it.copy(resetPasswordError = "Invalid target user.") }
            return
        }

        if (newPassword.length < 8) {
            _uiState.update { it.copy(resetPasswordError = "New password must be at least 8 characters.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isResettingPassword = true, resetPasswordError = null) }
            val result = adminRepository.resetPassword(userId = userId, newPassword = newPassword)
            result.onSuccess {
                val updatedStudents = _uiState.value.studentsList.map { student ->
                    if (student.numericId == userId) {
                        student.copy(passwordMasked = newPassword)
                    } else {
                        student
                    }
                }
                val updatedStaff = _uiState.value.staffList.map { staff ->
                    if (staff.numericId == userId) {
                        staff.copy(passwordMasked = newPassword)
                    } else {
                        staff
                    }
                }

                _uiState.update {
                    it.copy(
                        studentsList = updatedStudents,
                        staffList = updatedStaff,
                        showResetPasswordDialog = false,
                        isResettingPassword = false,
                        newPasswordInput = "",
                        resetPasswordError = null,
                        userManagementSuccessMessage = "✓ Password successfully reset for $targetName! Active login sessions were revoked."
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isResettingPassword = false,
                        resetPasswordError = error.localizedMessage ?: "Failed to reset password."
                    )
                }
            }
        }
    }

    fun clearUserManagementMessages() {
        _uiState.update {
            it.copy(userManagementErrorMessage = null, userManagementSuccessMessage = null)
        }
    }

    fun toggleNotifyStudents() {
        _uiState.update { it.copy(notifyStudentsChecked = !it.notifyStudentsChecked) }
    }

    fun toggleDay(day: String) {
        _uiState.update { currentState ->
            val updated = if (currentState.selectedDays.contains(day)) {
                currentState.selectedDays - day
            } else {
                currentState.selectedDays + day
            }
            currentState.copy(selectedDays = updated, isSaved = false)
        }
    }

    fun onTabSelected(tab: AdminTab) {
        if (tab == AdminTab.OVERVIEW) {
            _uiState.update { it.copy(navigationEvent = ManageBatchesNavigationEvent.NavigateToOverview) }
        } else {
            _uiState.update { it.copy(selectedTab = tab) }
        }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = ManageBatchesNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = ManageBatchesNavigationEvent.None) }
    }

    private fun extractInitials(fullName: String): String {
        val parts = fullName.trim().split("\\s+".toRegex()).filter { it.isNotBlank() }
        return when {
            parts.isEmpty() -> "NH"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "${parts[0].first()}${parts[1].first()}".uppercase()
        }
    }

    private fun UserDto.toEnrolledStudentAdminItem(): EnrolledStudentAdminItem {
        return EnrolledStudentAdminItem(
            id = id.toString(),
            initials = initials ?: extractInitials(fullName),
            name = fullName,
            batchName = batchName ?: "Not Assigned",
            loginStatus = if (status.equals("active", ignoreCase = true)) "Login Active" else status?.replaceFirstChar { it.uppercase() } ?: "Active",
            username = username ?: email,
            passwordMasked = "••••••••",
            numericId = id,
            email = email,
            attendanceSummary = levelAndAttendance
        )
    }

    private fun UserDto.toStaffMemberItem(): StaffMemberItem {
        return StaffMemberItem(
            id = id.toString(),
            name = fullName,
            role = staffRole ?: if (role.equals("admin", ignoreCase = true)) "Administrator" else "Instructor",
            assignedBatches = batchName ?: "All Academy Batches",
            monthlySalary = monthlySalary ?: "₹0 / mo",
            status = if (status.equals("active", ignoreCase = true)) "Active" else status?.replaceFirstChar { it.uppercase() } ?: "Active",
            username = username ?: email,
            passwordMasked = "••••••••",
            numericId = id,
            email = email
        )
    }
}
