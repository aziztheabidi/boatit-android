package com.boatit.boatsharing.ui.captain.availabilitystatus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.R
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityRequest
import com.boatit.boatsharing.ui.captain.availabilitystatus.model.CaptainAvailabilityResponse
import com.boatit.boatsharing.ui.captain.availabilitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.utils.prefmanager.RoleProvider
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import com.boatit.boatsharing.utils.prefmanager.StatusProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class UpdateStatusViewModel(
    private val repository: UpdateStatusRepository,
    private val statusProvider: StatusProvider
) : ViewModel() {

    private val _title = MutableStateFlow("Welcome!")
    val title: StateFlow<String> = _title

    private val _subtitle = MutableStateFlow("Tap the wheel to go online and start getting voyage requests")
    val subtitle: StateFlow<String> = _subtitle

    private val _image = MutableStateFlow(R.drawable.wheel_inactive)
    val image: StateFlow<Int> = _image

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val _isOnline = MutableStateFlow(false)
     val isOnline: StateFlow<Boolean> = _isOnline

    private val _navigateToDashboard = MutableSharedFlow<Unit>()
    val navigateToDashboard = _navigateToDashboard.asSharedFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    private val _loginState = MutableStateFlow<NetworkResponse<CaptainAvailabilityResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<CaptainAvailabilityResponse>> = _loginState

    fun toggleStatus(userId: String) {
        if (_isLoading.value) return

        val newStatus = _isOnline.value

        _isLoading.value = true
        _errorMessage.value = null

        if (newStatus) {
            _title.value = "You are Online!"
            _subtitle.value = "Start accepting voyager and help voyagers reach their destinations."
            _image.value = R.drawable.wheel_icon
        } else {
            _title.value = "Welcome!"
            _subtitle.value = "Tap the wheel to go online and start getting voyage requests"
            _image.value = R.drawable.wheel_inactive
        }

        viewModelScope.launch {
            val result = repository.status(CaptainAvailabilityRequest(userId, newStatus))
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
                statusProvider.setCaptainStatus(newStatus)
                _isOnline.value = newStatus
                _isLoading.value = false
                _toastMessage.emit(response.Message ?: "Status updated")
                if (newStatus) {
                    _navigateToDashboard.emit(Unit)
                }
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Status update failed")
                _errorMessage.value = error.message ?: "Network error, please try again."
                _isLoading.value = false
                _title.value = if (_isOnline.value) "You are Online!" else "Welcome!"
                _subtitle.value = if (_isOnline.value)
                    "Start accepting voyager and help voyagers reach their destinations."
                else
                    "Tap the wheel to go online and start getting voyage requests"
                _image.value = if (_isOnline.value) R.drawable.wheel_icon else R.drawable.wheel_inactive
            }
        }
    }

    fun getCaptainStatus(): Boolean = statusProvider.isCaptainOnline()
}



