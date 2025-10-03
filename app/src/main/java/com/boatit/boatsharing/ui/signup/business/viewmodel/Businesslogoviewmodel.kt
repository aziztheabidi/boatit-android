package com.boatit.boatsharing.ui.signup.business.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessLogoResponse
import com.boatit.boatsharing.ui.signup.business.repository.BusinessLogoRepository
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class BusinessLogoViewModel(private val businessLogoRepository: BusinessLogoRepository,
                            private val sharedPrefManager: SharedPrefManager
) : ViewModel() {

    private val _registrationState = MutableStateFlow<NetworkResponse<SaveBusinessLogoResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveBusinessLogoResponse>> = _registrationState

    private val _imageList = mutableStateListOf<Uri>()
    var imageList: List<Uri> = _imageList

    fun addImages(uris: List<Uri>) {
        _imageList.clear()
        _imageList.addAll(uris)
    }

    fun removeImage(uri: Uri) {
        _imageList.remove(uri)
    }

    fun clearImages() {
        _imageList.clear()
    }

    fun resetNearbyPlaces() {
        _registrationState.value = NetworkResponse.Loading()
    }

    fun uploadBusinessLogo(userId: String, logoFile: File,logoFiles: List<File?>) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = businessLogoRepository.saveBusinessLogo(userId, logoFile, logoFiles)
            result.onSuccess { response ->
                _registrationState.value = NetworkResponse.Success(response)
                saveLoginData(0)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Unknown error occurred")
            }
        }
    }

    fun uploadBusinessGallery(userId: String, logoFiles: List<File?>) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            val result = businessLogoRepository.saveBusinessGallery(userId, logoFiles)
            result.onSuccess { response ->
                _registrationState.value = NetworkResponse.Success(response)
            }.onFailure { error ->
                _registrationState.value = NetworkResponse.Error(error.message ?: "Unknown error occurred")
            }
        }
    }

    private fun saveLoginData(userData: Int) {
        sharedPrefManager.saveMissingStep(userData)
    }
}


