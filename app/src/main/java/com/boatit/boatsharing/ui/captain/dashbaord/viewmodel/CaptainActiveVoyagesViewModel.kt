package com.boatit.boatsharing.ui.captain.dashbaord.viewmodel

import CaptainActiveVoyagesResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.captain.dashbaord.repository.CaptainActiveVoyagesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CaptainActiveVoyagesViewModel(private val repository: CaptainActiveVoyagesRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<CaptainActiveVoyagesResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<CaptainActiveVoyagesResponse>> = _loginState

    fun voyages() {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.voyages()
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }
}


