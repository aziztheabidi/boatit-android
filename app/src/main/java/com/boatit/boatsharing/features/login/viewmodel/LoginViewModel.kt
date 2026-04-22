package com.boatit.boatsharing.features.login.viewmodel

import androidx.lifecycle.viewModelScope
import com.boatit.boatsharing.domain.core.Resource
import com.boatit.boatsharing.domain.core.toResource
import com.boatit.boatsharing.core.presentation.BaseViewModel
import com.boatit.boatsharing.core.presentation.UiEffect
import com.boatit.boatsharing.core.presentation.UiEvent
import com.boatit.boatsharing.core.presentation.UiState
import com.boatit.boatsharing.features.login.domain.model.AuthenticatedUser
import com.boatit.boatsharing.features.login.domain.model.LoginDomainModel
import com.boatit.boatsharing.features.login.domain.model.toUserData
import com.boatit.boatsharing.features.login.domain.usecase.LoginUserUseCase
import com.boatit.boatsharing.features.login.model.UserData
import com.boatit.boatsharing.data.local.prefmanager.SharedPrefManager
import com.boatit.boatsharing.data.local.session.SessionController
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) : UiState {
    val isEmailValid: Boolean
        get() = email.contains("@") && email.contains(".")

    val isPasswordValid: Boolean
        get() = password.length >= 6

    val isFormValid: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty() && isEmailValid && isPasswordValid
}

sealed interface LoginUiEvent : UiEvent {
    data class EmailChanged(val value: String) : LoginUiEvent

    data class PasswordChanged(val value: String) : LoginUiEvent

    data object LoginClicked : LoginUiEvent

    data object ClearError : LoginUiEvent
}

/** Where to go after a successful login (mirrors prior [LoginScreen] routing). */
sealed interface PostLoginDestination {
    data object SelectRole : PostLoginDestination

    data object VoyagerDashboard : PostLoginDestination

    data object VoyagerAccountInfo : PostLoginDestination

    data object BusinessDashboard : PostLoginDestination

    data object BusinessAccountInfo : PostLoginDestination

    data object CaptainOffline : PostLoginDestination

    data object CaptainAccountInfo : PostLoginDestination
}

sealed interface LoginUiEffect : UiEffect {
    data class ShowToast(val message: String) : LoginUiEffect

    /**
     * Single effect after login so FCM registration and navigation stay in the same order
     * as the previous UI implementation.
     */
    data class PostLogin(
        val userIdForFcm: String?,
        val destination: PostLoginDestination,
    ) : LoginUiEffect
}

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val sharedPrefManager: SharedPrefManager,
    private val sessionController: SessionController,
) : BaseViewModel<LoginUiState, LoginUiEvent, LoginUiEffect>(LoginUiState()) {
    override fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                updateState { copy(email = event.value, errorMessage = null) }
            }

            is LoginUiEvent.PasswordChanged -> {
                updateState { copy(password = event.value, errorMessage = null) }
            }

            LoginUiEvent.LoginClicked -> login()
            LoginUiEvent.ClearError -> updateState { copy(errorMessage = null) }
        }
    }

    private fun login() {
        if (!currentState.isFormValid || currentState.isLoading) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            when (val result = loginUserUseCase(currentState.email, currentState.password).toResource()) {
                is Resource.Success -> {
                    emitEffect(LoginUiEffect.ShowToast("Login Successful!"))
                    val domainUser = result.data.user
                    if (domainUser == null) {
                        emitEffect(
                            LoginUiEffect.PostLogin(
                                userIdForFcm = null,
                                destination = PostLoginDestination.SelectRole,
                            ),
                        )
                        updateState { copy(isLoading = false, errorMessage = null) }
                        return@launch
                    }
                    saveLoginData(domainUser.toUserData())
                    emitEffect(resolvePostLogin(domainUser, domainUser.userId))
                    updateState { copy(isLoading = false, errorMessage = null) }
                }

                is Resource.Error -> {
                    val message = result.error.toMessage()
                    updateState { copy(isLoading = false, errorMessage = message) }
                    emitEffect(LoginUiEffect.ShowToast(message))
                }

                Resource.Loading -> {
                    updateState { copy(isLoading = true) }
                }
            }
        }
    }

    private fun saveLoginData(userData: UserData) {
        sharedPrefManager.saveLoginData(userData)
    }

    private fun resolvePostLogin(
        user: AuthenticatedUser?,
        userIdForFcm: String,
    ): LoginUiEffect.PostLogin {
        if (user == null) {
            return LoginUiEffect.PostLogin(userIdForFcm = null, destination = PostLoginDestination.SelectRole)
        }
        val destination =
            when (user.role) {
                "Voyager" ->
                    if (user.missingStep == 0) {
                        PostLoginDestination.VoyagerDashboard
                    } else {
                        PostLoginDestination.VoyagerAccountInfo
                    }
                "Business" ->
                    if (user.missingStep == 0) {
                        PostLoginDestination.BusinessDashboard
                    } else {
                        PostLoginDestination.BusinessAccountInfo
                    }
                "Captain" ->
                    if (user.missingStep == 0) {
                        PostLoginDestination.CaptainOffline
                    } else {
                        PostLoginDestination.CaptainAccountInfo
                    }
                else -> PostLoginDestination.SelectRole
            }
        return LoginUiEffect.PostLogin(userIdForFcm = userIdForFcm, destination = destination)
    }

    fun getUserData(): UserData? = sharedPrefManager.getUserData()

    fun clearUserData() {
        sessionController.logoutAndResolveRoute()
    }
}
