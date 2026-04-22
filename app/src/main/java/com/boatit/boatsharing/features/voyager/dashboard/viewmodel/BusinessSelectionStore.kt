package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import com.boatit.boatsharing.features.voyager.dashboard.model.BusinessData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BusinessSelectionState(
    val business: BusinessData? = null,
    val isFollowed: Boolean = false,
)

class BusinessSelectionStore {
    private val _state = MutableStateFlow(BusinessSelectionState())
    val state: StateFlow<BusinessSelectionState> = _state.asStateFlow()

    fun setSelection(
        business: BusinessData,
        isFollowed: Boolean,
    ) {
        _state.value =
            BusinessSelectionState(
                business = business,
                isFollowed = isFollowed,
            )
    }

    fun updateFollowState(isFollowed: Boolean) {
        _state.value = _state.value.copy(isFollowed = isFollowed)
    }
}
