package com.simats.nritahub.ui.fees

sealed interface FeesInvoicesNavigationEvent {
    data object None : FeesInvoicesNavigationEvent
    data object NavigateBack : FeesInvoicesNavigationEvent
}

data class FeesInvoicesUiState(
    val title: String = "Fee Details",
    val subtitle: String = "STUDENT VIEW",
    val studentName: String = "Student",
    val studentId: String = "STU001",
    val studentClass: String = "Bharatanatyam",
    val feePeriod: String = "Current Term",
    val paymentDate: String = "15 Oct 2026",
    val invoiceNo: String = "NH-INV-001",
    val paidFeeAmount: String = "₹2,400",
    val outstandingFeeAmount: String = "₹0",
    val isInvoiceGenerated: Boolean = false,
    val hasPaidFee: Boolean = true,
    val hasOutstandingFee: Boolean = false,
    val clearDuesTitle: String = "Clear your fee dues",
    val clearDuesNotice: String = "Your fee payment has not yet been confirmed. Please contact your teacher or academy administrator.",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigationEvent: FeesInvoicesNavigationEvent = FeesInvoicesNavigationEvent.None
)
