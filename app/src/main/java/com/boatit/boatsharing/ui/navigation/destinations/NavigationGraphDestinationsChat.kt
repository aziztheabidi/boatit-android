package com.boatit.boatsharing.ui.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.boatit.boatsharing.features.chat.view.ChatScreen
import com.boatit.boatsharing.ui.navigation.InteractionRoutes
import com.boatit.boatsharing.ui.navigation.optDecodedStringArg

fun NavGraphBuilder.registerChatDestinations(navController: NavHostController) {
    composable(InteractionRoutes.chatPattern) { backStackEntry ->
        val chatId = backStackEntry.optDecodedStringArg(InteractionRoutes.CHAT_ID_ARG).orEmpty()
        val currentUserId = backStackEntry.optDecodedStringArg(InteractionRoutes.CURRENT_USER_ID_ARG).orEmpty()
        val name = backStackEntry.optDecodedStringArg(InteractionRoutes.NAME_ARG).orEmpty()
        val senderId = backStackEntry.optDecodedStringArg(InteractionRoutes.SENDER_ID_ARG).orEmpty()
        ChatScreen(navController, chatId, currentUserId, name, senderId)
    }
}
