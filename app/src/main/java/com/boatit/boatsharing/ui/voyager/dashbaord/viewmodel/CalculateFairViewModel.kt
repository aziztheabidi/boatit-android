package com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkreposne.NetworkResponse
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainProfileResponse
import com.boatit.boatsharing.ui.signup.captain.repository.GetCaptainProfileRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.model.CalculateFair
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.CalculateFairRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CalculateFairViewModel(private val repository: CalculateFairRepository) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<CalculateFair>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<CalculateFair>> = _registrationState

    fun CalculateFairFunc(FromDockId: String,ToDockId:String,DurationInHours:String) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = repository.CalculateFairRepoFunc(FromDockId,ToDockId,DurationInHours)
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }
}


