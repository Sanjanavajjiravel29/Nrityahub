package com.simats.nritahub.ui.fees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.nritahub.data.repository.FeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeesInvoicesViewModel(
    private val feeRepository: FeeRepository = FeeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeesInvoicesUiState())
    val uiState: StateFlow<FeesInvoicesUiState> = _uiState.asStateFlow()

    init {
        fetchStudentFees()
    }

    fun fetchStudentFees() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = feeRepository.listFees(type = "student_fee")
            result.onSuccess { response ->
                val allFees = response.studentFees.ifEmpty { response.records }
                val paidRecord = allFees.firstOrNull { it.isPaid || it.status.equals("paid", ignoreCase = true) }
                val unpaidRecord = allFees.firstOrNull { !it.isPaid && !it.status.equals("paid", ignoreCase = true) }

                _uiState.update { current ->
                    val studentName = paidRecord?.userName ?: unpaidRecord?.userName ?: current.studentName
                    val studentClass = paidRecord?.batchName ?: unpaidRecord?.batchName ?: current.studentClass
                    val studentIdStr = "STU${paidRecord?.userId ?: unpaidRecord?.userId ?: 1}"

                    val paidAmount = paidRecord?.amount ?: "₹0"
                    val paymentDate = paidRecord?.dueDate ?: current.paymentDate
                    val invoiceNo = if (paidRecord != null) "NH${paidRecord.id.toString().padStart(4, '0')}" else current.invoiceNo
                    val feePeriod = paidRecord?.title ?: unpaidRecord?.title ?: current.feePeriod

                    val hasUnpaid = unpaidRecord != null
                    val outstandingAmount = unpaidRecord?.amount ?: "₹0"

                    val clearDuesTitle: String
                    val clearDuesNotice: String

                    if (hasUnpaid) {
                        val isOverdue = unpaidRecord?.status.equals("overdue", ignoreCase = true)
                        if (isOverdue) {
                            clearDuesTitle = "Overdue Fee Notice"
                            clearDuesNotice = "Your fee payment of ${unpaidRecord?.amount} was due on ${unpaidRecord?.dueDate ?: "prior date"} and is now overdue. Please clear your dues immediately."
                        } else {
                            clearDuesTitle = "Clear your fee dues"
                            clearDuesNotice = "Your fee payment of ${unpaidRecord?.amount} is pending. Due date: ${unpaidRecord?.dueDate ?: "soon"}. Please contact your administrator."
                        }
                    } else {
                        clearDuesTitle = "All Dues Cleared"
                        clearDuesNotice = "All academy fee dues for your enrolled classes are settled. Thank you for your timely payment!"
                    }

                    current.copy(
                        isLoading = false,
                        studentName = studentName,
                        studentId = studentIdStr,
                        studentClass = studentClass,
                        feePeriod = feePeriod,
                        paymentDate = paymentDate,
                        invoiceNo = invoiceNo,
                        paidFeeAmount = paidAmount,
                        outstandingFeeAmount = outstandingAmount,
                        hasPaidFee = paidRecord != null,
                        hasOutstandingFee = hasUnpaid,
                        clearDuesTitle = clearDuesTitle,
                        clearDuesNotice = clearDuesNotice
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to load fee information."
                    )
                }
            }
        }
    }

    fun onGenerateInvoiceClicked() {
        _uiState.update { it.copy(isInvoiceGenerated = true) }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = FeesInvoicesNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = FeesInvoicesNavigationEvent.None) }
    }
}
