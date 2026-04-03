package com.boatit.boatsharing.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.UserData
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: LoginRepository, private val sharedPrefManager: SharedPrefManager) : ViewModel() {

    private val _loginState = MutableStateFlow<NetworkResponse<LoginResponse>>(NetworkResponse.Loading())
    val loginState: StateFlow<NetworkResponse<LoginResponse>> = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = NetworkResponse.Loading()
            val result = repository.login(username, password)
            result.onSuccess { response ->
                _loginState.value = NetworkResponse.Success(response)
                saveLoginData(response.obj)
            }.onFailure { error ->
                _loginState.value = NetworkResponse.Error(error.message ?: "Login failed")
            }
        }
    }

    fun saveLoginData(userData: UserData) {
        sharedPrefManager.saveLoginData(userData)
    }

    fun getUserData(): UserData? {
        return sharedPrefManager.getUserData()
    }

    fun clearUserData() {
        sharedPrefManager.clearUserData()
    }
}


