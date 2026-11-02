package com.simats.nritahub.ui.admin

enum class AdminTab { OVERVIEW, BATCHES, FEES, PROFILE }

data class AttentionItem(
    val title: String,
    val subtitle: String,
    val actionType: String
)

sealed interface AdministratorDashboardNavigationEvent {
    data object None : AdministratorDashboardNavigationEvent
    data object NavigateBack : AdministratorDashboardNavigationEvent
    data object ManageCollections : AdministratorDashboardNavigationEvent
    data object ManageSchedules : AdministratorDashboardNavigationEvent
    data object NotificationsClicked : AdministratorDashboardNavigationEvent
    data class AttentionItemClicked(val actionType: String) : AdministratorDashboardNavigationEvent
}

data class AdministratorDashboardUiState(
    val headerNumber: String = "29",
    val title: String = "Your academy, in focus",
    val academyAndDateText: String = "Sri Kanchi Shethira Kala Mandir · Chief Admin View",
    val overviewBadge: String = "ADMIN CONTROL PANEL",
    val enrolledStudentsCount: String = "156",
    val activeStaffCount: String = "6",
    val activeBatchesCount: String = "8",
    val calculatedSalaryTotal: String = "₹1,80,000",
    val feeCollectionTitle: String = "October fee collection",
    val feePercentageText: String = "71% collected",
    val totalCollectedAmount: String = "₹1,32,000",
    val totalBilledText: String = "of ₹1,87,200 billed · 78 invoices",
    val collectionProgress: Float = 0.71f,
    val pendingAmountText: String = "Pending ₹43,200",
    val overdueAmountText: String = "Overdue ₹12,000",
    val attentionTitle: String = "Admin Quick Controls",
    val attentionItems: List<AttentionItem> = listOf(
        AttentionItem("Manage Batches & Name Lists", "Student & staff rosters", "nav_page_30"),
        AttentionItem("Fees & Salary Calculation", "Fee management & staff payout calculations", "nav_page_31"),
        AttentionItem("Publish Program Announcement", "Recital, workshop & exam notices", "add_announcement"),
        AttentionItem("View Class Schedule & Timetable", "Academy class schedule and studio availability", "nav_schedule")
    ),
    val recitalNotice: String = "Program Announcement: Autumn recital on 18 October. Fee collection and registration controlled by Admin.",
    val selectedTab: AdminTab = AdminTab.OVERVIEW,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigationEvent: AdministratorDashboardNavigationEvent = AdministratorDashboardNavigationEvent.None
)
