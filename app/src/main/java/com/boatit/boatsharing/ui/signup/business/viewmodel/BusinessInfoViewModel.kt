package com.boatit.boatsharing.ui.signup.business.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.signup.business.model.BusinessInfoRequest
import com.boatit.boatsharing.ui.signup.business.model.BusinessInfoResponse
import com.boatit.boatsharing.ui.signup.business.model.GetBusinessProfileResponse
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessInfoResponse
import com.boatit.boatsharing.ui.signup.business.repository.BusinessInfoRepository
import com.boatit.boatsharing.ui.signup.captain.model.GetCaptainDocumentResponse
import com.boatit.boatsharing.ui.signup.captain.model.SaveCaptainDocumentRequest
import com.boatit.boatsharing.utils.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BusinessInfoViewModel(private val repository: BusinessInfoRepository) : ViewModel() {

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
            val result = repository.BusinessInfo(
                BusinessInfoRequest(
                    UserId = AppConstants.USER_ID.toString(),
                    Name = businessName,
                    Type = businessType,
                    Address = businessAddress,
                    PhoneNumber = businessPhoneNo,
                    YearOfEstablishment = establishmentYear,
                    Time = businessTime
                )
            )
            result.onSuccess { placesResponse ->
                _registrationState.value = NetworkResponse.Success(placesResponse)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Registration failed")
            }
        }
    }

    fun loadInitialData(data: BusinessInfoResponse?) {
        businessName = data?.obj?.Name .orEmpty()
        businessType = data?.obj?.Type  .orEmpty()
        businessAddress = data?.obj?.Address.orEmpty()
        businessPhoneNo = data?.obj?.PhoneNumber .orEmpty()
        establishmentYear = data?.obj?.YearOfEstablishment.toString().orEmpty()
        businessTime = data?.obj?.Time.orEmpty()
        bookingDate = data?.obj?.Description.orEmpty()
    }
}


