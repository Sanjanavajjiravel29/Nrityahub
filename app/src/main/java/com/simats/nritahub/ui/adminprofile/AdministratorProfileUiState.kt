package com.simats.nritahub.ui.adminprofile

import com.simats.nritahub.ui.admin.AdminTab

data class AdminPrivilegeItem(
    val title: String,
    val description: String
)

sealed interface AdministratorProfileNavigationEvent {
    data object None : AdministratorProfileNavigationEvent
    data object NavigateToOverview : AdministratorProfileNavigationEvent
    data object EditProfileClicked : AdministratorProfileNavigationEvent
    data object SignOutClicked : AdministratorProfileNavigationEvent
}

data class AdministratorProfileUiState(
    val headerNumber: String = "32",
    val title: String = "Administrator Profile",
    val userInitials: String = "RD",
    val adminName: String = "Dr. Rukmini Devi",
    val adminTitle: String = "Founder & Chief Administrator",
    val roleBadge: String = "Chief Administrator",
    val academyName: String = "Sri Kanchi Shethira Kala Mandir",
    val emailAddress: String = "rukmini.admin@srikanchikalamandir.example",
    val mobileNumber: String = "+91 98765 43220",
    val academySummaryBadge: String = "156 enrolled students · 6 staff · 8 active batches",
    val adminPrivileges: List<AdminPrivilegeItem> = listOf(
        AdminPrivilegeItem("Profile Creation (Admin Only)", "Students & staff cannot register directly. Only Admin creates profiles & issues logins."),
        AdminPrivilegeItem("Student Access & Name List", "Students can only sign in. Admin manages full student name rosters & batch assignments."),
        AdminPrivilegeItem("Staff Roster & Salary Calculation", "Admin manages staff profiles and computes monthly staff salary payouts."),
        AdminPrivilegeItem("Program Fees & Announcements", "Adding program fee structures and publishing academy announcements is restricted to Admin.")
    ),
    val appVersionFooter: String = "NrityaHub · Version 1.0 · Learning, together",
    val selectedTab: AdminTab = AdminTab.PROFILE,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigationEvent: AdministratorProfileNavigationEvent = AdministratorProfileNavigationEvent.None
)
