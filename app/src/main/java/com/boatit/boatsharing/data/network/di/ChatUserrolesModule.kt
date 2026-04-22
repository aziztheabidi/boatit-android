package com.boatit.boatsharing.data.network.di

import com.boatit.boatsharing.features.chat.domain.usecase.ComplainVoyagerUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.FetchVoyagersUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.FollowVoyagerUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.ListenForMessagesUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.MarkMessagesAsReadUseCase
import com.boatit.boatsharing.features.chat.domain.usecase.SendChatMessageUseCase
import com.boatit.boatsharing.features.chat.repository.ChatRepository
import com.boatit.boatsharing.features.chat.repository.FollowRepository
import com.boatit.boatsharing.features.chat.repository.VoyagersRepository
import com.boatit.boatsharing.features.chat.viewmodel.ChatViewModel
import com.boatit.boatsharing.features.chat.viewmodel.FollowViewModel
import com.boatit.boatsharing.features.chat.viewmodel.VoyagersListViewModel
import com.boatit.boatsharing.features.userroles.domain.usecase.AssignUserRoleUseCase
import com.boatit.boatsharing.features.userroles.domain.usecase.UpdateDeviceTokenUseCase
import com.boatit.boatsharing.features.userroles.repository.FCMTokenRepository
import com.boatit.boatsharing.features.userroles.repository.IFCMTokenRepository
import com.boatit.boatsharing.features.userroles.repository.IRoleRepository
import com.boatit.boatsharing.features.userroles.repository.RoleRepository
import com.boatit.boatsharing.features.userroles.viewmodel.FCMTokenViewModel
import com.boatit.boatsharing.features.userroles.viewmodel.RoleViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val chatUserrolesModule =
    module {
        single<IFCMTokenRepository> { FCMTokenRepository(get()) }
        single { UpdateDeviceTokenUseCase(get()) }
        viewModel { FCMTokenViewModel(get()) }

        single<IRoleRepository> { RoleRepository(get()) }
        single { AssignUserRoleUseCase(get()) }
        viewModel { RoleViewModel(get(), get(), get()) }

        single { ChatRepository(get()) }
        single {
            ListenForMessagesUseCase {
                    chatId,
                    currentUserId,
                    onMessagesUpdated,
                ->
                get<ChatRepository>().listenForMessages(chatId, currentUserId, onMessagesUpdated)
            }
        }
        single { SendChatMessageUseCase { chatId, senderId, message -> get<ChatRepository>().sendMessage(chatId, senderId, message) } }
        single { MarkMessagesAsReadUseCase { chatId, currentUserId -> get<ChatRepository>().markMessagesAsRead(chatId, currentUserId) } }
        viewModel { ChatViewModel(get(), get(), get()) }

        single { VoyagersRepository(get(), get()) }
        single { FetchVoyagersUseCase { get<VoyagersRepository>().voyages() } }
        viewModel { VoyagersListViewModel(get()) }

        single { FollowRepository(get()) }
        single { FollowVoyagerUseCase { request -> get<FollowRepository>().findboat(request) } }
        single { ComplainVoyagerUseCase { request -> get<FollowRepository>().complian(request) } }
        viewModel { FollowViewModel(get(), get()) }
    }
