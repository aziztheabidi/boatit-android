package com.boatit.boatsharing.network.di

import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.LocationViewModel
import com.boatit.boatsharing.ui.captain.availabilitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.captain.availabilitystatus.viewmodel.UpdateStatusViewModel
import com.boatit.boatsharing.ui.captain.dashboard.repository.AcceptRequestRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.CancelVoyageRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.CompleteVoyageRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.StartVoyageRepository
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.AcceptRequestViewModel
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.CancelVoyageViewModel
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.CompleteVoyageViewModel
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.StartVoyageViewModel
import com.boatit.boatsharing.ui.captain.voyages.repository.CaptainVoyagesRepository
import com.boatit.boatsharing.ui.captain.voyages.viewmodel.CaptainVoyagesViewModel
import com.boatit.boatsharing.ui.chat.repository.ChatRepository
import com.boatit.boatsharing.ui.chat.viewmodel.ChatViewModel
import com.boatit.boatsharing.ui.forgotpassword.repository.ForgotPassRepository
import com.boatit.boatsharing.ui.forgotpassword.viewmodel.ForgotPassViewModel
import com.boatit.boatsharing.ui.login.repository.LoginRepository
import com.boatit.boatsharing.ui.login.viewmodel.LoginViewModel
import com.boatit.boatsharing.ui.login.viewmodel.NotificationViewModel
import com.boatit.boatsharing.ui.signup.captain.repository.CaptainBoatRepository
import com.boatit.boatsharing.ui.signup.captain.repository.CaptainDocsRepository
import com.boatit.boatsharing.ui.signup.captain.repository.CaptainProfileRepository
import com.boatit.boatsharing.ui.signup.captain.repository.GetCaptainBoatRepository
import com.boatit.boatsharing.ui.signup.captain.repository.GetCaptainDocsRepository
import com.boatit.boatsharing.ui.signup.captain.repository.GetCaptainProfileRepository
import com.boatit.boatsharing.ui.signup.captain.viewmodel.CaptainBoatViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.CaptainDocsViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.CaptainProfileViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.GetCaptainBoatViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.GetCaptainDocsViewModel
import com.boatit.boatsharing.ui.signup.captain.viewmodel.GetCaptainProfileViewModel
import com.boatit.boatsharing.ui.signup.general.repository.GetVoyagerProfileViewModel
import com.boatit.boatsharing.ui.signup.general.repository.PasswordViewModel
import com.boatit.boatsharing.ui.signup.general.repository.VerifyEmailViewModel
import com.boatit.boatsharing.ui.signup.general.repository.VoyagerProfileViewModel
import com.boatit.boatsharing.ui.signup.general.viewmodel.GetVoyagerProfileRepository
import com.boatit.boatsharing.ui.signup.general.viewmodel.PasswordRepository
import com.boatit.boatsharing.ui.signup.general.viewmodel.VerifyEmailRepository
import com.boatit.boatsharing.ui.signup.general.viewmodel.VoyagerProfileRepository
import com.boatit.boatsharing.ui.userroles.repository.FCMTokenRepository
import com.boatit.boatsharing.ui.userroles.repository.RoleRepository
import com.boatit.boatsharing.ui.userroles.viewmodel.FCMTokenViewModel
import com.boatit.boatsharing.ui.userroles.viewmodel.RoleViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FetchNearByVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FindBoatRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.PaymentRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.RegistrationViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.repository.VoyagerVoyagesRepository
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.NearByVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.PaymentViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.RegistrationRepository
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.VoyagerVoyagesViewModel
import com.boatit.boatsharing.utils.prefmanager.RoleProvider
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val Modules = module {

    single { FirebaseAuth.getInstance() }
    single { FirebaseDatabase.getInstance() }
    single { FirebaseMessaging.getInstance() }
    single { FirebaseFirestore.getInstance() }

    viewModel { NotificationViewModel() }
    viewModel {LocationViewModel(get(), get(), androidContext()) }

    single { TokenProvider(androidContext()) }
    single { RoleProvider(androidContext()) }

    single { FetchNearByVoyagesRepo(get()) }
    viewModelOf(::NearByVoyagesViewModel)

    single { SharedPrefManager(androidContext()) }
    single { LoginRepository(get()) }
    viewModel { LoginViewModel(get(),get ()) }

    single { RegistrationRepository(get()) }
    viewModel { RegistrationViewModel(get()) }

    single { VerifyEmailRepository(get()) }
    viewModel { VerifyEmailViewModel(get()) }

    single { PasswordRepository(get()) }
    viewModel { PasswordViewModel(get(), get()) }

    single { ForgotPassRepository(get()) }
    viewModel { ForgotPassViewModel(get()) }

    single { RoleRepository(get()) }
    viewModel { RoleViewModel(get(), get(), get()) }

    single { VoyagerProfileRepository(get()) }
    viewModel { VoyagerProfileViewModel(get()) }

    single { GetVoyagerProfileRepository(get()) }
    viewModel { GetVoyagerProfileViewModel(get())}

    single { CaptainProfileRepository(get()) }
    viewModel { CaptainProfileViewModel(get()) }

    single { CaptainDocsRepository(get()) }
    viewModel { CaptainDocsViewModel(get()) }

    single { CaptainBoatRepository(get()) }
    viewModel { CaptainBoatViewModel(get()) }

    single { GetCaptainProfileRepository(get()) }
    viewModel { GetCaptainProfileViewModel(get()) }

    single { GetCaptainDocsRepository(get()) }
    viewModel { GetCaptainDocsViewModel(get()) }

    single { GetCaptainBoatRepository(get()) }
    viewModel { GetCaptainBoatViewModel(get()) }

    single { UpdateStatusRepository(get()) }
    viewModel { UpdateStatusViewModel(get()) }

    single { FCMTokenRepository(get()) }
    viewModel { FCMTokenViewModel(get()) }

    single { AcceptRequestRepository(get()) }
    viewModel { AcceptRequestViewModel(get()) }

    single { PaymentRepository(get()) }
    viewModel { PaymentViewModel(get()) }

    single { StartVoyageRepository(get()) }
    viewModel { StartVoyageViewModel(get()) }

    single { CompleteVoyageRepository(get()) }
    viewModel { CompleteVoyageViewModel(get()) }

    single { CancelVoyageRepository(get()) }
    viewModel { CancelVoyageViewModel(get()) }

    single { CaptainVoyagesRepository(get()) }
    viewModel { CaptainVoyagesViewModel(get()) }

    single { VoyagerVoyagesRepository(get()) }
    viewModel { VoyagerVoyagesViewModel(get()) }

    single { FindBoatRepo(get()) }
    viewModel { FindBoatViewModel(get()) }

    single { ChatRepository(get()) }
    viewModel { ChatViewModel(get()) }
}