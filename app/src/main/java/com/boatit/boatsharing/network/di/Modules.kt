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
import com.boatit.boatsharing.utils.session.SessionManager
import com.boatit.boatsharing.utils.session.TokenRefreshService
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
import org.koin.core.qualifier.named
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

    // Create HttpClient without SessionManager dependency first
    single { createKtorClient(get()) }
    
    // Session Management - TokenRefreshService uses the basic HttpClient
    single { TokenRefreshService(get(), get()) }
    single { SessionManager(get(), get(), get()) }
    
    // Create HttpClient with interceptor for repositories that need session management
    single(named("httpClientWithInterceptor")) { createKtorClientWithInterceptor(get(), get()) }

    single { FetchNearByVoyagesRepo(get(named("httpClientWithInterceptor")), androidContext()) }
    single { FetchCategoryRepo(get(named("httpClientWithInterceptor")), androidContext()) }
    viewModel {NearByVoyagesViewModel(get(), get())}

    single { SharedPrefManager(androidContext()) }
    single { LoginRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { LoginViewModel(get(),get ()) }

    single { RegistrationRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { RegistrationViewModel(get()) }

    single { VerifyEmailRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { VerifyEmailViewModel(get()) }

    single { PasswordRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { PasswordViewModel(get(), get()) }

    single { ForgotPassRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { ForgotPassViewModel(get()) }

    single { RoleRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { RoleViewModel(get(), get(), get()) }

    single { VoyagerProfileRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { VoyagerProfileViewModel(get(), get()) }

    single { GetVoyagerProfileRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { GetVoyagerProfileViewModel(get())}

    single { CaptainProfileRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { CaptainProfileViewModel(get()) }

    single { CaptainDocsRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { CaptainDocsViewModel(get()) }

    single { CaptainBoatRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { CaptainBoatViewModel(get(), get()) }

    single { GetCaptainProfileRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { GetCaptainProfileViewModel(get()) }

    single { GetCaptainDocsRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { GetCaptainDocsViewModel(get()) }

    single { GetCaptainBoatRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { GetCaptainBoatViewModel(get()) }

    single { UpdateStatusRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { UpdateStatusViewModel(get(), get())  }

    single { FCMTokenRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { FCMTokenViewModel(get()) }

    single { AcceptRequestRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { AcceptRequestViewModel(get()) }

    single { PaymentRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { PaymentViewModel(get()) }

    single { StartVoyageRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { StartVoyageViewModel(get()) }

    single { CompleteVoyageRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { CompleteVoyageViewModel(get()) }

    single { CancelVoyageRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { CancelVoyageViewModel(get()) }

    single { CaptainVoyagesRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { CaptainVoyagesViewModel(get()) }

    single { VoyagerVoyagesRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { VoyagerVoyagesViewModel(get()) }

    single { FindBoatRepo(get(named("httpClientWithInterceptor"))) }
    viewModel { FindBoatViewModel(get()) }

    single { GetActiveVoyageRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { GetActiveVoyageViewModel(get()) }

    single { ChatRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { ChatViewModel(get()) }

    single { VoyagersRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { VoyagersListViewModel(get()) }

    single { PaymentSheetConfigRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { PaymentSheetConfigViewModel(get()) }

    single { CaptainActiveVoyagesRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { CaptainActiveVoyagesViewModel(get()) }

    single { CalculateFairRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { CalculateFairViewModel(get()) }

    single { BookVoyageRepo(get(named("httpClientWithInterceptor"))) }
    viewModel { BookVoyageViewModel(get()) }

    single { FollowedVoyagerRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { FollowedVoyagerViewModel(get()) }

    single { SponcerVoyagesRepo(get(named("httpClientWithInterceptor"))) }
    viewModel { SponcerVoyagesViewModel(get()) }

    single { FutureVoyagesRepo(get(named("httpClientWithInterceptor"))) }
    viewModel { FutureVoyagesViewModel(get()) }

    single { SponsorPaymentSheetConfigRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { SponsorPaymentSheetConfigViewModel(get()) }

    single { SponsorPaymentConfirmationRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { SponsorPaymentConfirmationViewModel(get()) }

    single { ConfirmBookedVoyageRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { ConfirmBookedVoyageViewModel(get()) }

    single { CancelBookedVoyageRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { CancelBookedVoyageViewModel(get()) }

    single { CaptainFeedbackRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { CaptainFeedbackViewModel(get()) }

    single { FollowRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { FollowViewModel(get()) }

    single { FetchBusinessRepo(get(named("httpClientWithInterceptor"))) }
    viewModel { FetchBusinessViewModel(get()) }

    single { GetBusinessRepo(get(named("httpClientWithInterceptor"))) }
    single { GetBusinessDocksRepo(get(named("httpClientWithInterceptor"))) }
    viewModel { GetBusinessViewModel(get(),get())}

    single { GetBusinessProfileRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { GetBusinessProfileViewModel(get()) }

    single { BusinessProfileRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { BusinessProfileViewModel(get()) }

    single { BusinessInfoRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { BusinessInfoViewModel(get()) }

    single { GetBusinessInfoRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { GetBusinessInfoViewModel(get()) }

    single { BusinessAboutRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { BusinessAboutViewModel(get()) }

    single { BusinessLogoRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { BusinessLogoViewModel(get(), get()) }

    single { BusinessDashboardRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { BusinessDashViewModel(get()) }

    single { TravelNowRepo(get(named("httpClientWithInterceptor"))) }
    viewModel { TravelNowViewModel(get(), get(), get()) }

    single { VoyagerFeedbackRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { VoyagerFeedbackViewModel(get()) }

    single { FollowBusinessRepository(get(named("httpClientWithInterceptor"))) }
    viewModel { VoyagerFollowBusinessViewModel(get()) }

}