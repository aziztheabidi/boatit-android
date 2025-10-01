package com.boatit.boatsharing.network.di

import LocationViewModel
import com.boatit.boatsharing.ui.business.repository.BusinessDashboardRepository
import com.boatit.boatsharing.ui.business.repository.GetBusinessDocksRepo
import com.boatit.boatsharing.ui.business.repository.GetBusinessRepo
import com.boatit.boatsharing.ui.business.viewmodel.BusinessDashViewModel
import com.boatit.boatsharing.ui.business.viewmodel.GetBusinessViewModel
import com.boatit.boatsharing.ui.captain.availabilitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.ui.captain.availabilitystatus.viewmodel.UpdateStatusViewModel
import com.boatit.boatsharing.ui.captain.dashboard.repository.AcceptRequestRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.CancelVoyageRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.CaptainActiveVoyagesRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.CaptainFeedbackRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.CompleteVoyageRepository
import com.boatit.boatsharing.ui.captain.dashboard.repository.StartVoyageRepository
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.AcceptRequestViewModel
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.CancelVoyageViewModel
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.CaptainActiveVoyagesViewModel
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.CaptainFeedbackViewModel
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.CompleteVoyageViewModel
import com.boatit.boatsharing.ui.captain.dashboard.viewmodel.StartVoyageViewModel
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
import com.boatit.boatsharing.ui.signup.business.repository.BusinessAboutRepository
import com.boatit.boatsharing.ui.signup.business.repository.BusinessInfoRepository
import com.boatit.boatsharing.ui.signup.business.repository.BusinessLogoRepository
import com.boatit.boatsharing.ui.signup.business.repository.BusinessProfileRepository
import com.boatit.boatsharing.ui.signup.business.repository.GetBusinessInfoRepository
import com.boatit.boatsharing.ui.signup.business.repository.GetBusinessProfileRepository
import com.boatit.boatsharing.ui.signup.business.viewmodel.BusinessAboutViewModel
import com.boatit.boatsharing.ui.signup.business.viewmodel.BusinessInfoViewModel
import com.boatit.boatsharing.ui.signup.business.viewmodel.BusinessLogoViewModel
import com.boatit.boatsharing.ui.signup.business.viewmodel.BusinessProfileViewModel
import com.boatit.boatsharing.ui.signup.business.viewmodel.GetBusinessInfoViewModel
import com.boatit.boatsharing.ui.signup.business.viewmodel.GetBusinessProfileViewModel
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
import com.boatit.boatsharing.ui.voyager.dashboard.repository.BookVoyageRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.CalculateFairRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.CancelBookedVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.ConfirmBookedVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FetchBusinessRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FetchCategoryRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FetchNearByVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FindBoatRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FollowBusinessRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FollowedVoyagerRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.FutureVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.GetActiveVoyageRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.GoogleDirectionsApi
import com.boatit.boatsharing.ui.voyager.dashboard.repository.PaymentRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.PaymentSheetConfigRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.RegistrationViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.repository.SponcerVoyagesRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.SponsorPaymentConfirmationRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.SponsorPaymentSheetConfigRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.TravelNowRepo
import com.boatit.boatsharing.ui.voyager.dashboard.repository.VoyagerFeedbackRepository
import com.boatit.boatsharing.ui.voyager.dashboard.repository.VoyagerVoyagesRepository
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.BookVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.CalculateFairViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.ConfirmBookedVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.FetchBusinessViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.FollowedVoyagerViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.FutureVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.GetActiveVoyageViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.NearByVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.PaymentSheetConfigViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.PaymentViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.RegistrationRepository
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.SponcerVoyagesViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.SponsorPaymentConfirmationViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.SponsorPaymentSheetConfigViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.TrackingLocationViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.TravelNowViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.VoyagerFeedbackViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.VoyagerFollowBusinessViewModel
import com.boatit.boatsharing.ui.voyager.dashboard.viewmodel.VoyagerVoyagesViewModel
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
    single { FetchCategoryRepo(get(), androidContext()) }
    viewModel {NearByVoyagesViewModel(get(), get())}

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
    viewModel { VoyagerProfileViewModel(get(), get()) }

    single { GetVoyagerProfileRepository(get()) }
    viewModel { GetVoyagerProfileViewModel(get())}

    single { CaptainProfileRepository(get()) }
    viewModel { CaptainProfileViewModel(get()) }

    single { CaptainDocsRepository(get()) }
    viewModel { CaptainDocsViewModel(get()) }

    single { CaptainBoatRepository(get()) }
    viewModel { CaptainBoatViewModel(get(), get()) }

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

    single { FetchBusinessRepo(get()) }
    viewModel { FetchBusinessViewModel(get()) }

    single { GetBusinessRepo(get()) }
    single { GetBusinessDocksRepo(get()) }
    viewModel { GetBusinessViewModel(get(),get())}

    single { GetBusinessProfileRepository(get()) }
    viewModel { GetBusinessProfileViewModel(get()) }

    single { BusinessProfileRepository(get()) }
    viewModel { BusinessProfileViewModel(get()) }

    single { BusinessInfoRepository(get()) }
    viewModel { BusinessInfoViewModel(get()) }

    single { GetBusinessInfoRepository(get()) }
    viewModel { GetBusinessInfoViewModel(get()) }

    single { BusinessAboutRepository(get()) }
    viewModel { BusinessAboutViewModel(get()) }

    single { BusinessLogoRepository(get()) }
    viewModel { BusinessLogoViewModel(get(), get()) }

    single { BusinessDashboardRepository(get()) }
    viewModel { BusinessDashViewModel(get()) }

    single { TravelNowRepo(get()) }
    viewModel { TravelNowViewModel(get(), get(), get()) }

    single { VoyagerFeedbackRepository(get()) }
    viewModel { VoyagerFeedbackViewModel(get()) }

    single { FollowBusinessRepository(get()) }
    viewModel { VoyagerFollowBusinessViewModel(get()) }

}