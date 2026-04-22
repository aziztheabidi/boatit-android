package com.boatit.boatsharing.features.userroles.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.userroles.domain.model.DeviceTokenUpdateDomainModel
import com.boatit.boatsharing.features.userroles.domain.usecase.UpdateDeviceTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FCMTokenViewModel(
    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _tokenUpdateState = MutableStateFlow<NetworkResponse<DeviceTokenUpdateDomainModel>>(NetworkResponse.Loading())
    val tokenUpdateState: StateFlow<NetworkResponse<DeviceTokenUpdateDomainModel>> = _tokenUpdateState

    @Deprecated("Use tokenUpdateState")
    val loginState: StateFlow<NetworkResponse<DeviceTokenUpdateDomainModel>> = tokenUpdateState

    fun updateFcmToken(
        userId: String,
        token: String,
    ) {
        viewModelScope.launch {
            _tokenUpdateState.value = NetworkResponse.Loading()
            when (val result = updateDeviceTokenUseCase(userId, token).toResource()) {
                is Resource.Success -> {
                    _tokenUpdateState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    _tokenUpdateState.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _tokenUpdateState.value = NetworkResponse.Loading()
                }
            }
        }
    }

    @Deprecated("Use updateFcmToken")
    fun fcm(
        userid: String,
        token: String,
    ) {
        updateFcmToken(userId = userid, token = token)
    }
}
