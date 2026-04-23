package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.features.business.domain.usecase.DeleteBusinessDashboardImageUseCase
import com.boatit.boatsharing.features.business.domain.usecase.FetchBusinessDashboardProfileUseCase
import com.boatit.boatsharing.features.business.domain.usecase.FetchBusinessDocksUseCase
import com.boatit.boatsharing.features.business.domain.usecase.SaveBusinessDashboardProfileUseCase
import com.boatit.boatsharing.features.business.repository.BusinessDashboardRepository
import com.boatit.boatsharing.features.business.repository.GetBusinessDocksRepo
import com.boatit.boatsharing.features.business.repository.GetBusinessRepo
import com.boatit.boatsharing.features.business.viewmodel.BusinessDashboardViewModel
import com.boatit.boatsharing.features.business.viewmodel.GetBusinessViewModel
import com.boatit.boatsharing.features.signup.business.domain.usecase.FetchBusinessInfoUseCase
import com.boatit.boatsharing.features.signup.business.domain.usecase.FetchBusinessProfileUseCase
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessAboutUseCase
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessGalleryUseCase
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessInfoUseCase
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessLogoUseCase
import com.boatit.boatsharing.features.signup.business.domain.usecase.SaveBusinessProfileUseCase
import com.boatit.boatsharing.features.signup.business.repository.BusinessAboutRepository
import com.boatit.boatsharing.features.signup.business.repository.BusinessInfoRepository
import com.boatit.boatsharing.features.signup.business.repository.BusinessLogoRepository
import com.boatit.boatsharing.features.signup.business.repository.BusinessProfileRepository
import com.boatit.boatsharing.features.signup.business.repository.GetBusinessInfoRepository
import com.boatit.boatsharing.features.signup.business.repository.GetBusinessProfileRepository
import com.boatit.boatsharing.features.signup.business.viewmodel.BusinessAboutViewModel
import com.boatit.boatsharing.features.signup.business.viewmodel.BusinessInfoViewModel
import com.boatit.boatsharing.features.signup.business.viewmodel.BusinessLogoViewModel
import com.boatit.boatsharing.features.signup.business.viewmodel.BusinessProfileViewModel
import com.boatit.boatsharing.features.signup.business.viewmodel.GetBusinessInfoViewModel
import com.boatit.boatsharing.features.signup.business.viewmodel.GetBusinessProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val businessModule =
    module {
        single { GetBusinessRepo(get()) }
        single { GetBusinessDocksRepo(get()) }
        single {
            FetchBusinessDashboardProfileUseCase {
                get<GetBusinessRepo>().voyages()
            }
        }
        single {
            FetchBusinessDocksUseCase {
                get<GetBusinessDocksRepo>().voyages()
            }
        }
        viewModel { GetBusinessViewModel(get(), get()) }

        single { GetBusinessProfileRepository(get(), get()) }
        single {
            FetchBusinessProfileUseCase {
                get<GetBusinessProfileRepository>().GetBusinessProfile()
            }
        }
        viewModel { GetBusinessProfileViewModel(get()) }

        single { BusinessProfileRepository(get()) }
        single {
            SaveBusinessProfileUseCase { request ->
                get<BusinessProfileRepository>().BusinessProfile(request)
            }
        }
        viewModel { BusinessProfileViewModel(get()) }

        single { BusinessInfoRepository(get()) }
        single {
            SaveBusinessInfoUseCase { request ->
                get<BusinessInfoRepository>().BusinessInfo(request)
            }
        }
        viewModel { BusinessInfoViewModel(get(), get()) }

        single { GetBusinessInfoRepository(get(), get()) }
        single {
            FetchBusinessInfoUseCase {
                get<GetBusinessInfoRepository>().GetBusinessInfo()
            }
        }
        viewModel { GetBusinessInfoViewModel(get()) }

        single { BusinessAboutRepository(get()) }
        single {
            SaveBusinessAboutUseCase { request ->
                get<BusinessAboutRepository>().BusinessAbout(request)
            }
        }
        viewModel { BusinessAboutViewModel(get()) }

        single { BusinessLogoRepository(get()) }
        single {
            SaveBusinessLogoUseCase { userId, logoFile, logoFiles ->
                get<BusinessLogoRepository>().saveBusinessLogo(userId, logoFile, logoFiles)
            }
        }
        single {
            SaveBusinessGalleryUseCase { userId, logoFiles ->
                get<BusinessLogoRepository>().saveBusinessGallery(userId, logoFiles)
            }
        }
        viewModel { BusinessLogoViewModel(get(), get(), get()) }

        single { BusinessDashboardRepository(get()) }
        single {
            SaveBusinessDashboardProfileUseCase { request ->
                get<BusinessDashboardRepository>().BusinessInfo(request)
            }
        }
        single {
            DeleteBusinessDashboardImageUseCase { request ->
                get<BusinessDashboardRepository>().Delete(request)
            }
        }
        viewModel { BusinessDashboardViewModel(get(), get(), get(), get(), get()) }
    }
