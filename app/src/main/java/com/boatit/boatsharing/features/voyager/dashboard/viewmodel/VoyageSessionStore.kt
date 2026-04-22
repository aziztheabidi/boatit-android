package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VoyageSessionStore {
    private val _voyageId = MutableStateFlow("")
    val voyageId: StateFlow<String> = _voyageId.asStateFlow()

    fun setVoyageId(id: String) {
        _voyageId.value = id
    }

    fun clear() {
        _voyageId.value = ""
    }
}
