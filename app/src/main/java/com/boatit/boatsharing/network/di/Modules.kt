package com.boatit.boatsharing.network.di

import LocationViewModel
import com.boatit.boatsharing.ui.business.repository.GetBusinessRepo
import com.boatit.boatsharing.ui.business.viewmodel.GetBusinessViewModel
import com.boatit.boatsharing.ui.captain.availablitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.captain.availablitystatus.viewmodel.UpdateStatusViewModel
import com.boatit.boatsharing.ui.captain.dashbaord.repository.AcceptRequestRepository
import com.boatit.boatsharing.ui.captain.dashbaord.repository.CancelVoyageRepository
import com.boatit.boatsharing.ui.captain.dashbaord.repository.CaptainActiveVoyagesRepository
import com.boatit.boatsharing.ui.captain.dashbaord.repository.CaptainFeedbackRepository
import com.boatit.boatsharing.ui.captain.dashbaord.repository.CompleteVoyageRepository
import com.boatit.boatsharing.ui.captain.dashbaord.repository.StartVoyageRepository
import com.boatit.boatsharing.ui.captain.dashbaord.viewmodel.AcceptRequestViewModel
import com.boatit.boatsharing.ui.captain.dashbaord.viewmodel.CancelVoyageViewModel
import com.boatit.boatsharing.ui.captain.dashbaord.viewmodel.CaptainActiveVoyagesViewModel
import com.boatit.boatsharing.ui.captain.dashbaord.viewmodel.CaptainFeedbackViewModel
import com.boatit.boatsharing.ui.captain.dashbaord.viewmodel.CompleteVoyageViewModel
import com.boatit.boatsharing.ui.captain.dashbaord.viewmodel.StartVoyageViewModel
import com.boatit.boatsharing.ui.captain.voyages.repository.CaptainVoyagesRepository
import com.boatit.boatsharing.ui.captain.voyages.viewmodel.CaptainVoyagesViewModel
import com.boatit.boatsharing.ui.chat.repository.ChatRepository
import com.boatit.boatsharing.ui.chat.repository.FollowRepository
import com.boatit.boatsharing.ui.chat.repository.VoyagersRepository
import com.boatit.boatsharing.ui.chat.viewmodel.ChatViewModel
import com.boatit.boatsharing.ui.chat.viewmodel.FollowViewModel
import com.boatit.boatsharing.ui.chat.viewmodel.VoyagersListViewModel
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
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.BookVoyageRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.CalculateFairRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.CancelBookedVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.ConfirmBookedVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FetchNearByVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FindBoatRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FollowedVoyagerRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.FutureVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.GetActiveVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.GoogleDirectionsApi
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.PaymentRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.PaymentSheetConfigRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.RegistrationViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.SponcerVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.SponsorPaymentConfirmationRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.SponsorPaymentSheetConfigRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.TravelNowRepo
import com.boatit.boatsharing.ui.voyager.dashbaord.repository.VoyagerVoyagesRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.BookVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.CalculateFairViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.ConfirmBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FollowedVoyagerViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.FutureVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.GetActiveVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.NearByVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.PaymentSheetConfigViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.PaymentViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.RegistrationRepository
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.SponcerVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.SponsorPaymentConfirmationViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.SponsorPaymentSheetConfigViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.TrackingLocationViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.TravelNowViewModel
import com.boatit.boatsharing.ui.voyager.dashbaord.viewmodel.VoyagerVoyagesViewModel
import com.boatit.boatsharing.utils.prefmanager.RoleProvider
import com.boatit.boatsharing.utils.prefmanager.SharedPrefManager
import com.boatit.boatsharing.utils.prefmanager.StatusProvider
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.google.android.gms.location.LocationServices

val Modules = module {

    single { FirebaseAuth.getInstance() }
    single { FirebaseDatabase.getInstance() }
    single { FirebaseMessaging.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single<FusedLocationProviderClient> { LocationServices.getFusedLocationProviderClient(androidContext()) }
    single { GoogleDirectionsApi() }

    viewModel { NotificationViewModel() }
    viewModel { TrackingLocationViewModel(get(), get(), get(), get(), get()) }
    viewModel {LocationViewModel(get(), get(), androidContext()) }

    single { TokenProvider(androidContext()) }
    single { RoleProvider(androidContext()) }
    single { StatusProvider(androidContext()) }

    single { createKtorClient(get()) }

    single { FetchNearByVoyagesRepo(get(), androidContext()) }
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
    viewModel { UpdateStatusViewModel(get(), get())  }

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

    single { GetActiveVoyageRepository(get()) }
    viewModel { GetActiveVoyageViewModel(get()) }

    single { ChatRepository(get()) }
    viewModel { ChatViewModel(get()) }

    single { VoyagersRepository(get()) }
    viewModel { VoyagersListViewModel(get()) }

    single { PaymentSheetConfigRepository(get()) }
    viewModel { PaymentSheetConfigViewModel(get()) }

    single { CaptainActiveVoyagesRepository(get()) }
    viewModel { CaptainActiveVoyagesViewModel(get()) }

    single { CalculateFairRepository(get()) }
    viewModel { CalculateFairViewModel(get()) }

    single { BookVoyageRepo(get()) }
    viewModel { BookVoyageViewModel(get()) }

    single { FollowedVoyagerRepository(get()) }
    viewModel { FollowedVoyagerViewModel(get()) }

    single { SponcerVoyagesRepo(get()) }
    viewModel { SponcerVoyagesViewModel(get()) }

    single { FutureVoyagesRepo(get()) }
    viewModel { FutureVoyagesViewModel(get()) }

    single { SponsorPaymentSheetConfigRepository(get()) }
    viewModel { SponsorPaymentSheetConfigViewModel(get()) }

    single { SponsorPaymentConfirmationRepository(get()) }
    viewModel { SponsorPaymentConfirmationViewModel(get()) }

    single { ConfirmBookedVoyageRepository(get()) }
    viewModel { ConfirmBookedVoyageViewModel(get()) }

    single { CancelBookedVoyageRepository(get()) }
    viewModel { CancelBookedVoyageViewModel(get()) }

    single { CaptainFeedbackRepository(get()) }
    viewModel { CaptainFeedbackViewModel(get()) }

    single { FollowRepository(get()) }
    viewModel { FollowViewModel(get()) }

    single { GetBusinessRepo(get()) }
    viewModel { GetBusinessViewModel(get())}

    single { TravelNowRepo(get()) }
    viewModel { TravelNowViewModel(get(), get(), get()) }


}