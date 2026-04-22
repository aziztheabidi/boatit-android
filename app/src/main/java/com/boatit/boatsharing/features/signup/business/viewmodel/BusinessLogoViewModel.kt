package com.boatit.boatsharing.features.signup.business.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessGalleryUseCase
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessLogoUseCase
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessLogoResponse
import com.boatit.boatsharing.data.local.prefmanager.SharedPrefManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class BusinessLogoViewModel(
    private val saveBusinessLogoUseCase: SaveBusinessLogoUseCase,
    private val saveBusinessGalleryUseCase: SaveBusinessGalleryUseCase,
    private val sharedPrefManager: SharedPrefManager,
) : com.boatit.boatsharing.core.presentation.LegacyMviViewModel() {
    private val _registrationState =
        MutableStateFlow<NetworkResponse<SaveBusinessLogoResponse>>(NetworkResponse.Loading())
    val registrationState: StateFlow<NetworkResponse<SaveBusinessLogoResponse>> = _registrationState

    private val _imageList = mutableStateListOf<Uri>()
    val imageList: SnapshotStateList<Uri> get() = _imageList

    fun addImages(uris: List<Uri>) {
        val remaining = (6 - _imageList.size).coerceAtLeast(0)
        _imageList.addAll(uris.take(remaining))
    }

    fun removeImage(uri: Uri) {
        _imageList.remove(uri)
    }

    fun setImages(list: List<Uri>) {
        _imageList.clear()
        _imageList.addAll(list)
    }

    fun clearImages() {
        _imageList.clear()
    }

    fun resetNearbyPlaces() {
        _registrationState.value = NetworkResponse.Loading()
    }

    fun uploadBusinessLogo(
        userId: String,
        logoFile: File,
        logoFiles: List<File?>,
    ) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            when (val result = saveBusinessLogoUseCase(userId, logoFile, logoFiles).toResource()) {
                is Resource.Success -> {
                    _registrationState.value = NetworkResponse.Success(result.data)
                    saveLoginData(0)
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

    fun uploadBusinessGallery(
        userId: String,
        logoFiles: List<File?>,
    ) {
        viewModelScope.launch {
            _registrationState.value = NetworkResponse.Loading()
            when (val result = saveBusinessGalleryUseCase(userId, logoFiles).toResource()) {
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

    private fun saveLoginData(userData: Int) {
        sharedPrefManager.saveMissingStep(userData)
    }
}
