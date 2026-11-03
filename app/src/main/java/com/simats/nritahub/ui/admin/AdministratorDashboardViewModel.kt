package com.simats.nritahub.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.nritahub.data.repository.AdminRepository
import com.simats.nritahub.data.repository.AdminStatsResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdministratorDashboardViewModel(
    private val adminRepository: AdminRepository = AdminRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdministratorDashboardUiState())
    val uiState: StateFlow<AdministratorDashboardUiState> = _uiState.asStateFlow()

    init {
        fetchDashboardStats()
    }

    fun fetchDashboardStats() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = adminRepository.getDashboardStats()) {
                is AdminStatsResult.Success -> {
                    val stats = result.stats
                    val fee = stats.feeCollection

                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = null,
                            overviewBadge = stats.overviewBadge ?: current.overviewBadge,
                            enrolledStudentsCount = stats.enrolledStudentsCount ?: current.enrolledStudentsCount,
                            activeStaffCount = stats.activeStaffCount ?: current.activeStaffCount,
                            activeBatchesCount = stats.activeBatchesCount ?: current.activeBatchesCount,
                            calculatedSalaryTotal = stats.calculatedSalaryTotal ?: current.calculatedSalaryTotal,
                            feeCollectionTitle = fee?.title ?: current.feeCollectionTitle,
                            feePercentageText = fee?.feePercentageText ?: current.feePercentageText,
                            totalCollectedAmount = fee?.totalCollectedAmount ?: current.totalCollectedAmount,
                            totalBilledText = fee?.totalBilledText ?: current.totalBilledText,
                            collectionProgress = fee?.collectionProgress ?: current.collectionProgress,
                            pendingAmountText = fee?.pendingAmountText ?: current.pendingAmountText,
                            overdueAmountText = fee?.overdueAmountText ?: current.overdueAmountText,
                            recitalNotice = stats.recitalNotice ?: current.recitalNotice
                        )
                    }
                }
                is AdminStatsResult.Error -> {
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

    fun onTabSelected(tab: AdminTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun onManageCollectionsClicked() {
        _uiState.update { it.copy(navigationEvent = AdministratorDashboardNavigationEvent.ManageCollections) }
    }

    fun onManageSchedulesClicked() {
        _uiState.update { it.copy(navigationEvent = AdministratorDashboardNavigationEvent.ManageSchedules) }
    }

    fun onAttentionItemClicked(actionType: String) {
        _uiState.update { it.copy(navigationEvent = AdministratorDashboardNavigationEvent.AttentionItemClicked(actionType)) }
    }

    fun onNotificationsClicked() {
        _uiState.update { it.copy(navigationEvent = AdministratorDashboardNavigationEvent.NotificationsClicked) }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigationEvent = AdministratorDashboardNavigationEvent.NavigateBack) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigationEvent = AdministratorDashboardNavigationEvent.None) }
    }
}
