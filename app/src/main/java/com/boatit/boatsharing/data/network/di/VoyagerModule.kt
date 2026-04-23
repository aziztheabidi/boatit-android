package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.features.chat.repository.VoyagersRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.GoogleDirectionsApi
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.TrackingLocationViewModel
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.BookVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.CalculateVoyageFareUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.CancelBookedVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.ConfirmBookedVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.ConfirmSponsorPaymentUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.ConfirmVoyagePaymentUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.DeclineSponsorPaymentUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchActiveVoyageUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchActiveVoyagersUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchBusinessRelationshipsUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchFollowedVoyagersUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchFutureVoyagesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchNearbyPlacesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchPaymentSheetConfigUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchSponsorPaymentSheetConfigUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchSponsorPaymentsUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchTravelNowVoyagesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchVoyageCategoriesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FetchVoyagerPastVoyagesUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FindBoatUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.FollowBusinessUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.SubmitVoyagerFeedbackUseCase
import com.boatit.boatsharing.features.voyager.dashboard.domain.usecase.UnFollowBusinessUseCase
import com.boatit.boatsharing.features.voyager.dashboard.repository.BookVoyageRepo
import com.boatit.boatsharing.features.voyager.dashboard.repository.CalculateFairRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.CancelBookedVoyageRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.ConfirmBookedVoyageRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.FetchBusinessRepo
import com.boatit.boatsharing.features.voyager.dashboard.repository.FetchCategoryRepo
import com.boatit.boatsharing.features.voyager.dashboard.repository.FetchNearByVoyagesRepo
import com.boatit.boatsharing.features.voyager.dashboard.repository.FindBoatRepo
import com.boatit.boatsharing.features.voyager.dashboard.repository.FollowBusinessRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.FollowedVoyagerRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.FutureVoyagesRepo
import com.boatit.boatsharing.features.voyager.dashboard.repository.GetActiveVoyageRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.ISponsorVoyagesRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.PaymentRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.PaymentSheetConfigRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.SponsorPaymentConfirmationRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.SponsorPaymentSheetConfigRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.SponsorVoyagesRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.TravelNowRepo
import com.boatit.boatsharing.features.voyager.dashboard.repository.VoyagerFeedbackRepository
import com.boatit.boatsharing.features.voyager.dashboard.repository.VoyagerVoyagesRepository
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.BookVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.BusinessSelectionStore
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CalculateFairViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CancelBookedVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.ConfirmBookedVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CreateVoyageDraftStore
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CreateVoyageRateCalcViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.CreateVoyageSponsorViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FetchBusinessViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FindBoatPrefillStore
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FindBoatViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FollowedVoyagerViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.FutureVoyagesViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.GetActiveVoyageViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.NearByVoyagesViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.PaymentSheetConfigViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.PaymentViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.SponsorPaymentConfirmationViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.SponsorPaymentSheetConfigViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.SponsorVoyagesViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.TravelNowViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.VoyageSessionStore
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.VoyagerFeedbackViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.VoyagerFollowBusinessViewModel
import com.boatit.boatsharing.features.voyager.dashboard.viewmodel.VoyagerVoyagesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val voyagerModule =
    module {
        single { GoogleDirectionsApi() }
        viewModel { TrackingLocationViewModel(get(), get(), get(), get()) }

        single { FetchNearByVoyagesRepo(get(), androidContext()) }
        single { FetchCategoryRepo(get(), androidContext()) }
        single {
            FetchNearbyPlacesUseCase {
                get<FetchNearByVoyagesRepo>().getNearbyPlaces()
            }
        }
        single {
            FetchVoyageCategoriesUseCase {
                get<FetchCategoryRepo>().getNearbyPlaces()
            }
        }
        viewModel { NearByVoyagesViewModel(get(), get()) }

        single { PaymentRepository(get()) }
        single {
            ConfirmVoyagePaymentUseCase { request ->
                get<PaymentRepository>().payment(request)
            }
        }
        viewModel { PaymentViewModel(get()) }

        single { VoyagerVoyagesRepository(get(), get()) }
        single {
            FetchVoyagerPastVoyagesUseCase {
                get<VoyagerVoyagesRepository>().voyages()
            }
        }
        viewModel { VoyagerVoyagesViewModel(get()) }

        single { FindBoatRepo(get()) }
        single {
            FindBoatUseCase { request ->
                get<FindBoatRepo>().findboat(request)
            }
        }
        single { FindBoatPrefillStore() }
        viewModel { FindBoatViewModel(get(), get()) }

        single { GetActiveVoyageRepository(get(), get()) }
        single {
            FetchActiveVoyageUseCase {
                get<GetActiveVoyageRepository>().voyages()
            }
        }
        viewModel { GetActiveVoyageViewModel(get()) }

        single { PaymentSheetConfigRepository(get()) }
        single {
            FetchPaymentSheetConfigUseCase { id ->
                get<PaymentSheetConfigRepository>().sheetConfig(id)
            }
        }
        viewModel { PaymentSheetConfigViewModel(get()) }

        single { CalculateFairRepository(get()) }
        single {
            CalculateVoyageFareUseCase { durationInHours, fromDockId, toDockId, voyageCategoryId, noOfVoyagers ->
                get<CalculateFairRepository>().calculateFair(
                    durationInHours = durationInHours,
                    fromDockId = fromDockId,
                    toDockId = toDockId,
                    voyageCategoryId = voyageCategoryId,
                    noOfVoyagers = noOfVoyagers,
                )
            }
        }
        single { CreateVoyageDraftStore() }
        single { VoyageSessionStore() }
        viewModel { CalculateFairViewModel(get(), get()) }
        viewModel { CreateVoyageRateCalcViewModel(get()) }
        single {
            FetchActiveVoyagersUseCase {
                get<VoyagersRepository>().voyages()
            }
        }
        viewModel { CreateVoyageSponsorViewModel(get(), get()) }

        single { BookVoyageRepo(get()) }
        single {
            BookVoyageUseCase { request ->
                get<BookVoyageRepo>().bookVoyage(request)
            }
        }
        viewModel { BookVoyageViewModel(get()) }

        single { FollowedVoyagerRepository(get(), get()) }
        single {
            FetchFollowedVoyagersUseCase {
                get<FollowedVoyagerRepository>().getFollowedVoyagers()
            }
        }
        viewModel { FollowedVoyagerViewModel(get()) }

        single<ISponsorVoyagesRepository> { SponsorVoyagesRepository(get(), get()) }
        single {
            FetchSponsorPaymentsUseCase {
                get<ISponsorVoyagesRepository>().voyages()
            }
        }
        viewModel { SponsorVoyagesViewModel(get()) }

        single { FutureVoyagesRepo(get(), get()) }
        single {
            FetchFutureVoyagesUseCase {
                get<FutureVoyagesRepo>().voyages()
            }
        }
        viewModel { FutureVoyagesViewModel(get()) }

        single { SponsorPaymentSheetConfigRepository(get()) }
        single {
            FetchSponsorPaymentSheetConfigUseCase { request ->
                get<SponsorPaymentSheetConfigRepository>().sheetConfig(request)
            }
        }
        single {
            DeclineSponsorPaymentUseCase { request ->
                get<SponsorPaymentSheetConfigRepository>().paymentDecline(request)
            }
        }
        viewModel { SponsorPaymentSheetConfigViewModel(get(), get()) }

        single { SponsorPaymentConfirmationRepository(get()) }
        single {
            ConfirmSponsorPaymentUseCase { request ->
                get<SponsorPaymentConfirmationRepository>().payment(request)
            }
        }
        viewModel { SponsorPaymentConfirmationViewModel(get()) }

        single { ConfirmBookedVoyageRepository(get()) }
        single {
            ConfirmBookedVoyageUseCase { request ->
                get<ConfirmBookedVoyageRepository>().findboat(request)
            }
        }
        viewModel { ConfirmBookedVoyageViewModel(get()) }

        single { CancelBookedVoyageRepository(get()) }
        single {
            CancelBookedVoyageUseCase { request ->
                get<CancelBookedVoyageRepository>().findboat(request)
            }
        }
        viewModel { CancelBookedVoyageViewModel(get()) }

        single { FetchBusinessRepo(get()) }
        single {
            FetchBusinessRelationshipsUseCase {
                get<FetchBusinessRepo>().getNearbyPlaces()
            }
        }
        single { BusinessSelectionStore() }
        viewModel { FetchBusinessViewModel(get()) }

        single { TravelNowRepo(get()) }
        single {
            FetchTravelNowVoyagesUseCase {
                get<TravelNowRepo>().voyages()
            }
        }
        viewModel { TravelNowViewModel(get(), get(), get()) }

        single { VoyagerFeedbackRepository(get()) }
        single {
            SubmitVoyagerFeedbackUseCase { request ->
                get<VoyagerFeedbackRepository>().status(request)
            }
        }
        viewModel { VoyagerFeedbackViewModel(get()) }

        single { FollowBusinessRepository(get()) }
        single {
            FollowBusinessUseCase { request ->
                get<FollowBusinessRepository>().status(request)
            }
        }
        single {
            UnFollowBusinessUseCase { request ->
                get<FollowBusinessRepository>().unFollow(request)
            }
        }
        viewModel { VoyagerFollowBusinessViewModel(get(), get()) }
    }
