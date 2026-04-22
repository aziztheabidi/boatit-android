package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.features.captain.availabilitystatus.repository.UpdateStatusRepository
import com.boatit.boatsharing.features.captain.availabilitystatus.viewmodel.UpdateStatusViewModel
import com.boatit.boatsharing.features.captain.dashboard.repository.AcceptRequestRepository
import com.boatit.boatsharing.features.captain.dashboard.repository.CancelVoyageRepository
import com.boatit.boatsharing.features.captain.dashboard.repository.CaptainActiveVoyagesRepository
import com.boatit.boatsharing.features.captain.dashboard.repository.CaptainFeedbackRepository
import com.boatit.boatsharing.features.captain.dashboard.repository.CompleteVoyageRepository
import com.boatit.boatsharing.features.captain.dashboard.repository.ICaptainActiveVoyagesRepository
import com.boatit.boatsharing.features.captain.dashboard.repository.StartVoyageRepository
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.AcceptRequestViewModel
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.CancelVoyageViewModel
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.CaptainActiveVoyagesViewModel
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.CaptainFeedbackViewModel
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.CompleteVoyageViewModel
import com.boatit.boatsharing.features.captain.dashboard.viewmodel.StartVoyageViewModel
import com.boatit.boatsharing.features.captain.domain.usecase.AcceptVoyageUseCase
import com.boatit.boatsharing.features.captain.domain.usecase.CancelVoyageUseCase
import com.boatit.boatsharing.features.captain.domain.usecase.CompleteVoyageUseCase
import com.boatit.boatsharing.features.captain.domain.usecase.DeclineVoyageUseCase
import com.boatit.boatsharing.features.captain.domain.usecase.FetchCaptainActiveVoyagesUseCase
import com.boatit.boatsharing.features.captain.domain.usecase.FetchCaptainCompletedVoyagesUseCase
import com.boatit.boatsharing.features.captain.domain.usecase.StartVoyageUseCase
import com.boatit.boatsharing.features.captain.domain.usecase.SubmitCaptainFeedbackUseCase
import com.boatit.boatsharing.features.captain.domain.usecase.UpdateCaptainAvailabilityUseCase
import com.boatit.boatsharing.features.captain.voyages.repository.CaptainVoyagesRepository
import com.boatit.boatsharing.features.captain.voyages.repository.ICaptainVoyagesRepository
import com.boatit.boatsharing.features.captain.voyages.viewmodel.CaptainVoyagesViewModel
import com.boatit.boatsharing.features.signup.captain.domain.usecase.FetchCaptainBoatUseCase
import com.boatit.boatsharing.features.signup.captain.domain.usecase.FetchCaptainDocsUseCase
import com.boatit.boatsharing.features.signup.captain.domain.usecase.FetchCaptainProfileUseCase
import com.boatit.boatsharing.features.signup.captain.domain.usecase.SaveCaptainBoatUseCase
import com.boatit.boatsharing.features.signup.captain.domain.usecase.SaveCaptainDocsUseCase
import com.boatit.boatsharing.features.signup.captain.domain.usecase.SaveCaptainProfileUseCase
import com.boatit.boatsharing.features.signup.captain.repository.CaptainBoatRepository
import com.boatit.boatsharing.features.signup.captain.repository.CaptainDocsRepository
import com.boatit.boatsharing.features.signup.captain.repository.CaptainProfileRepository
import com.boatit.boatsharing.features.signup.captain.repository.GetCaptainBoatRepository
import com.boatit.boatsharing.features.signup.captain.repository.GetCaptainDocsRepository
import com.boatit.boatsharing.features.signup.captain.repository.GetCaptainProfileRepository
import com.boatit.boatsharing.features.signup.captain.viewmodel.CaptainBoatViewModel
import com.boatit.boatsharing.features.signup.captain.viewmodel.CaptainDocsViewModel
import com.boatit.boatsharing.features.signup.captain.viewmodel.CaptainProfileViewModel
import com.boatit.boatsharing.features.signup.captain.viewmodel.GetCaptainBoatViewModel
import com.boatit.boatsharing.features.signup.captain.viewmodel.GetCaptainDocsViewModel
import com.boatit.boatsharing.features.signup.captain.viewmodel.GetCaptainProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val captainModule =
    module {
        single { CaptainProfileRepository(get()) }
        single {
            SaveCaptainProfileUseCase { request ->
                get<CaptainProfileRepository>().CaptainProfile(request)
            }
        }
        viewModel { CaptainProfileViewModel(get()) }

        single { CaptainDocsRepository(get()) }
        single {
            SaveCaptainDocsUseCase { request ->
                get<CaptainDocsRepository>().CaptainDocs(request)
            }
        }
        viewModel { CaptainDocsViewModel(get(), get()) }

        single { CaptainBoatRepository(get()) }
        single {
            SaveCaptainBoatUseCase { request ->
                get<CaptainBoatRepository>().CaptainBoat(request)
            }
        }
        viewModel { CaptainBoatViewModel(get(), get(), get()) }

        single { GetCaptainProfileRepository(get(), get()) }
        single {
            FetchCaptainProfileUseCase {
                get<GetCaptainProfileRepository>().GetCaptainProfile()
            }
        }
        viewModel { GetCaptainProfileViewModel(get()) }

        single { GetCaptainDocsRepository(get(), get()) }
        single {
            FetchCaptainDocsUseCase {
                get<GetCaptainDocsRepository>().GetCaptainDocs()
            }
        }
        viewModel { GetCaptainDocsViewModel(get()) }

        single { GetCaptainBoatRepository(get(), get()) }
        single {
            FetchCaptainBoatUseCase {
                get<GetCaptainBoatRepository>().GetCaptainBoat()
            }
        }
        viewModel { GetCaptainBoatViewModel(get()) }

        single { UpdateStatusRepository(get()) }
        single {
            UpdateCaptainAvailabilityUseCase { request ->
                get<UpdateStatusRepository>().status(request)
            }
        }
        viewModel { UpdateStatusViewModel(get(), get()) }

        single { AcceptRequestRepository(get()) }
        single {
            AcceptVoyageUseCase { request ->
                get<AcceptRequestRepository>().status(request)
            }
        }
        single {
            DeclineVoyageUseCase { request ->
                get<AcceptRequestRepository>().decline(request)
            }
        }
        viewModel { AcceptRequestViewModel(get(), get()) }

        single { StartVoyageRepository(get()) }
        single {
            StartVoyageUseCase { request ->
                get<StartVoyageRepository>().status(request)
            }
        }
        viewModel { StartVoyageViewModel(get()) }

        single { CompleteVoyageRepository(get()) }
        single {
            CompleteVoyageUseCase { request ->
                get<CompleteVoyageRepository>().status(request)
            }
        }
        viewModel { CompleteVoyageViewModel(get()) }

        single { CancelVoyageRepository(get()) }
        single {
            CancelVoyageUseCase { request ->
                get<CancelVoyageRepository>().status(request)
            }
        }
        viewModel { CancelVoyageViewModel(get()) }

        single<ICaptainVoyagesRepository> { CaptainVoyagesRepository(get()) }
        single { FetchCaptainCompletedVoyagesUseCase(get()) }
        viewModel { CaptainVoyagesViewModel(get()) }

        single<ICaptainActiveVoyagesRepository> { CaptainActiveVoyagesRepository(get()) }
        single { FetchCaptainActiveVoyagesUseCase(get()) }
        viewModel { CaptainActiveVoyagesViewModel(get()) }

        single { CaptainFeedbackRepository(get()) }
        single {
            SubmitCaptainFeedbackUseCase { request ->
                get<CaptainFeedbackRepository>().status(request)
            }
        }
        viewModel { CaptainFeedbackViewModel(get()) }
    }
