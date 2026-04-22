package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.features.forgotpassword.domain.usecase.SendForgotPasswordUseCase
import com.boatit.boatsharing.features.forgotpassword.repository.ForgotPassRepository
import com.boatit.boatsharing.features.forgotpassword.repository.IForgotPassRepository
import com.boatit.boatsharing.features.forgotpassword.viewmodel.ForgotPassViewModel
import com.boatit.boatsharing.features.login.domain.usecase.LoginUserUseCase
import com.boatit.boatsharing.features.login.repository.ILoginRepository
import com.boatit.boatsharing.features.login.repository.LoginRepository
import com.boatit.boatsharing.features.login.viewmodel.LoginViewModel
import com.boatit.boatsharing.features.signup.general.domain.usecase.FetchVoyagerProfileUseCase
import com.boatit.boatsharing.features.signup.general.domain.usecase.RegisterPasswordUseCase
import com.boatit.boatsharing.features.signup.general.domain.usecase.RegisterUserUseCase
import com.boatit.boatsharing.features.signup.general.domain.usecase.SaveVoyagerProfileUseCase
import com.boatit.boatsharing.features.signup.general.domain.usecase.VerifySignupEmailUseCase
import com.boatit.boatsharing.features.signup.general.repository.GetVoyagerProfileRepository
import com.boatit.boatsharing.features.signup.general.repository.IGetVoyagerProfileRepository
import com.boatit.boatsharing.features.signup.general.repository.IPasswordRepository
import com.boatit.boatsharing.features.signup.general.repository.IRegistrationRepository
import com.boatit.boatsharing.features.signup.general.repository.IVerifyEmailRepository
import com.boatit.boatsharing.features.signup.general.repository.IVoyagerProfileRepository
import com.boatit.boatsharing.features.signup.general.repository.PasswordRepository
import com.boatit.boatsharing.features.signup.general.repository.RegistrationRepository
import com.boatit.boatsharing.features.signup.general.repository.VerifyEmailRepository
import com.boatit.boatsharing.features.signup.general.repository.VoyagerProfileRepository
import com.boatit.boatsharing.features.signup.general.viewmodel.GetVoyagerProfileViewModel
import com.boatit.boatsharing.features.signup.general.viewmodel.PasswordViewModel
import com.boatit.boatsharing.features.signup.general.viewmodel.RegistrationViewModel
import com.boatit.boatsharing.features.signup.general.viewmodel.VerifyEmailViewModel
import com.boatit.boatsharing.features.signup.general.viewmodel.VoyagerProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule =
    module {
        single<ILoginRepository> { LoginRepository(get()) }
        single { LoginUserUseCase(get()) }
        viewModel { LoginViewModel(get(), get(), get()) }

        single<IRegistrationRepository> { RegistrationRepository(get()) }
        single { RegisterUserUseCase(get()) }
        viewModel { RegistrationViewModel(get()) }

        single<IVerifyEmailRepository> { VerifyEmailRepository(get()) }
        single { VerifySignupEmailUseCase(get()) }
        viewModel { VerifyEmailViewModel(get()) }

        single<IPasswordRepository> { PasswordRepository(get()) }
        single { RegisterPasswordUseCase(get()) }
        viewModel { PasswordViewModel(get(), get()) }

        single<IForgotPassRepository> { ForgotPassRepository(get()) }
        single { SendForgotPasswordUseCase(get()) }
        viewModel { ForgotPassViewModel(get()) }

        single<IVoyagerProfileRepository> { VoyagerProfileRepository(get()) }
        single { SaveVoyagerProfileUseCase(get()) }
        viewModel { VoyagerProfileViewModel(get(), get()) }

        single<IGetVoyagerProfileRepository> { GetVoyagerProfileRepository(get(), get()) }
        single { FetchVoyagerProfileUseCase(get()) }
        viewModel { GetVoyagerProfileViewModel(get()) }
    }
