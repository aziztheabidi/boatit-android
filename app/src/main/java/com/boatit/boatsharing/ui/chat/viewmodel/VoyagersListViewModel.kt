package com.boatit.boatsharing.ui.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.chat.model.ActiveVoyagersResponse
import com.boatit.boatsharing.ui.chat.repository.VoyagersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VoyagersListViewModel(private val repository: VoyagersRepository) : ViewModel() {

    private val _loginState =
        MutableStateFlow<NetworkResponse<ActiveVoyagersResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<ActiveVoyagersResponse>> = _loginState

    fun voyages() {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.voyages()
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Failed to load voyagers")
            }
        }
    }
}
