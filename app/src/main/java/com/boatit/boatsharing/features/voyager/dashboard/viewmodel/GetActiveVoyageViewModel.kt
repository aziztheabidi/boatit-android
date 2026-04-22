package com.boatit.boatsharing.features.voyager.dashboard.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchActiveVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.model.ActiveVoyageResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GetActiveVoyageViewModel(private val fetchActiveVoyageUseCase: FetchActiveVoyageUseCase) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _loginState = MutableStateFlow<NetworkResponse<ActiveVoyageResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<ActiveVoyageResponse>> = _loginState

    fun voyages() {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            when (val result = fetchActiveVoyageUseCase().toResource()) {
                is Resource.Success -> {
                    Log.e("popup_res", result.data.toString())
                    _loginState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    Log.e("popup_res_err", message)
                    _loginState.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _loginState.value = NetworkResponse.Loading()
                }
            }
        }
    }

    fun resetNearbyPlaces() {
        _loginState.value = NetworkResponse.Loading()
    }
}
