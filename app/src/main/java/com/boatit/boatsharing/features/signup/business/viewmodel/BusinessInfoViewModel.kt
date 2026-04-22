package com.boatit.boatsharing.features.signup.business.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessInfoUseCase
import com.boatit.boatsharing.features.signup.business.model.BusinessInfoRequest
import com.boatit.boatsharing.features.signup.business.model.BusinessInfoResponse
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessInfoResponse
import com.boatit.boatsharing.data.local.prefmanager.UserSessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessInfoViewModel(
    private val saveBusinessInfoUseCase: SaveBusinessInfoUseCase,
    private val userSessionStore: UserSessionStore,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _registrationState = MutableStateFlow<NetworkResponse<SaveBusinessInfoResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveBusinessInfoResponse>> = _registrationState

    var businessName by mutableStateOf("")

    var businessType by mutableStateOf("")

    var businessAddress by mutableStateOf("")

    var businessPhoneNo by mutableStateOf("")

    var establishmentYear by mutableStateOf("")

    var businessTime by mutableStateOf("")

    var bookingDate by mutableStateOf("")

    var showDialog by mutableStateOf(false)

    fun saveBusinessProfile() {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            when (
                val result =
                    saveBusinessInfoUseCase(
                        BusinessInfoRequest(
                            UserId = userSessionStore.currentUserId(),
                            Name = businessName,
                            Type = businessType,
                            Address = businessAddress,
                            PhoneNumber = businessPhoneNo,
                            YearOfEstablishment = establishmentYear,
                            Time = businessTime,
                        ),
                    ).toResource()
            ) {
                is Resource.Success -> {
                    _registrationState.value = NetworkResponse.Success(result.data)
                }

                is Resource.Error -> {
                    _registrationState.value = NetworkResponse.Error(result.error)
                }

                Resource.Loading -> {
                    _registrationState.value = NetworkResponse.Loading()
                }
            }
        }
    }

    fun loadInitialData(data: BusinessInfoResponse?) {
        businessName = data?.obj?.Name.orEmpty()
        businessType = data?.obj?.Type.orEmpty()
        businessAddress = data?.obj?.Address.orEmpty()
        businessPhoneNo = data?.obj?.PhoneNumber.orEmpty()
        establishmentYear = data?.obj?.YearOfEstablishment.toString().orEmpty()
        businessTime = data?.obj?.Time.orEmpty()
        bookingDate = data?.obj?.Description.orEmpty()
    }
}
