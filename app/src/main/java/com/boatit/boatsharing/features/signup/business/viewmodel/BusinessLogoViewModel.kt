package com.boatit.boatsharing.features.signup.business.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.data.local.prefmanager.SharedPrefManager
import com.boatit.boatsharing.data.network.networkresponse.NetworkResponse
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessGalleryUseCase
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessLogoUseCase
import com.boatit.boatsharing.features.signup.business.model.SaveBusinessLogoResponse
import kotlinx.coroutines.launch
import java.io.File

data class BusinessLogoUiState(
    val imageList: List<Uri> = emptyList(),
    val registrationState: NetworkResponse<SaveBusinessLogoResponse> = NetworkResponse.Loading(),
) : UiState

sealed interface BusinessLogoUiEvent : UiEvent {
    data class AddImages(val uris: List<Uri>) : BusinessLogoUiEvent

    data class RemoveImage(val uri: Uri) : BusinessLogoUiEvent

    data class SetImages(val list: List<Uri>) : BusinessLogoUiEvent

    data object ClearImages : BusinessLogoUiEvent

    data class UploadLogo(
        val userId: String,
        val logoFile: File,
        val logoFiles: List<File?>,
    ) : BusinessLogoUiEvent

    data class UploadGallery(
        val userId: String,
        val logoFiles: List<File?>,
    ) : BusinessLogoUiEvent
}

sealed interface BusinessLogoUiEffect : UiEffect {
    data object NoOpEffect : BusinessLogoUiEffect
}

class BusinessLogoViewModel(
    private val saveBusinessLogoUseCase: SaveBusinessLogoUseCase,
    private val saveBusinessGalleryUseCase: SaveBusinessGalleryUseCase,
    private val sharedPrefManager: SharedPrefManager,
) : BaseViewModel<BusinessLogoUiState, BusinessLogoUiEvent, BusinessLogoUiEffect>(BusinessLogoUiState()) {
    override fun onEvent(event: BusinessLogoUiEvent) {
        when (event) {
            is BusinessLogoUiEvent.AddImages -> addImages(event.uris)
            is BusinessLogoUiEvent.RemoveImage -> removeImage(event.uri)
            is BusinessLogoUiEvent.SetImages -> setImages(event.list)
            BusinessLogoUiEvent.ClearImages -> clearImages()
            is BusinessLogoUiEvent.UploadLogo -> uploadBusinessLogo(event.userId, event.logoFile, event.logoFiles)
            is BusinessLogoUiEvent.UploadGallery -> uploadBusinessGallery(event.userId, event.logoFiles)
        }
    }

    fun addImages(uris: List<Uri>) {
        val current = currentState.imageList
        val remaining = (6 - current.size).coerceAtLeast(0)
        updateState { copy(imageList = current + uris.take(remaining)) }
    }

    fun removeImage(uri: Uri) {
        updateState { copy(imageList = imageList.filterNot { it == uri }) }
    }

    fun setImages(list: List<Uri>) {
        updateState { copy(imageList = list) }
    }

    fun clearImages() {
        updateState { copy(imageList = emptyList()) }
    }

    fun resetNearbyPlaces() {
        updateState { copy(registrationState = NetworkResponse.Loading()) }
    }

    fun uploadBusinessLogo(
        userId: String,
        logoFile: File,
        logoFiles: List<File?>,
    ) {
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (val result = saveBusinessLogoUseCase(userId, logoFile, logoFiles).toResource()) {
                is Resource.Success -> {
                    updateState { copy(registrationState = NetworkResponse.Success(result.data)) }
                    saveLoginData(0)
                }

                is Resource.Error -> {
                    updateState { copy(registrationState = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(registrationState = NetworkResponse.Loading()) }
                }
            }
        }
    }

    fun uploadBusinessGallery(
        userId: String,
        logoFiles: List<File?>,
    ) {
        viewModelScope.launch {
            updateState { copy(registrationState = NetworkResponse.Loading()) }
            when (val result = saveBusinessGalleryUseCase(userId, logoFiles).toResource()) {
                is Resource.Success -> {
                    updateState { copy(registrationState = NetworkResponse.Success(result.data)) }
                }

                is Resource.Error -> {
                    updateState { copy(registrationState = NetworkResponse.Error(result.error)) }
                }

                Resource.Loading -> {
                    updateState { copy(registrationState = NetworkResponse.Loading()) }
                }
            }
        }
    }

    private fun saveLoginData(userData: Int) {
        sharedPrefManager.saveMissingStep(userData)
    }
}
