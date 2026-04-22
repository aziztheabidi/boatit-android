package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FindBoatBusinessPrefill(
    val pending: Boolean = false,
    val target: String = "",
    val dockId: Int? = null,
    val dockName: String = "",
)

class FindBoatPrefillStore {
    private val _state = MutableStateFlow(FindBoatBusinessPrefill())
    val state: StateFlow<FindBoatBusinessPrefill> = _state.asStateFlow()

    fun prefillPickup(
        dockId: Int?,
        dockName: String,
    ) {
        _state.value =
            FindBoatBusinessPrefill(
                pending = true,
                target = "Pick",
                dockId = dockId,
                dockName = dockName,
            )
    }

    fun prefillDropOff(
        dockId: Int?,
        dockName: String,
    ) {
        _state.value =
            FindBoatBusinessPrefill(
                pending = true,
                target = "Drop",
                dockId = dockId,
                dockName = dockName,
            )
    }

    fun consume() {
        _state.value = FindBoatBusinessPrefill()
    }
}
