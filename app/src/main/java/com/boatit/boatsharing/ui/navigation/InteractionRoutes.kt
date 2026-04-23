package com.boatit.boatsharing.ui.navigation

import android.net.Uri

object InteractionRoutes {
    const val CHAT_ID_ARG = "chatId"
    const val CURRENT_USER_ID_ARG = "currentUserId"
    const val NAME_ARG = "name"
    const val SENDER_ID_ARG = "senderId"
    const val FEEDBACK_VOYAGE_ID_ARG = "value"

    val chatPattern: String = "${AppRoutes.Chat.CHAT}/{$CHAT_ID_ARG}/{$CURRENT_USER_ID_ARG}/{$NAME_ARG}/{$SENDER_ID_ARG}"
    val voyagerFeedbackPattern: String = "${AppRoutes.Voyager.VOYAGER_FEEDBACK}/{$FEEDBACK_VOYAGE_ID_ARG}"
    val captainFeedbackPattern: String = "${AppRoutes.Captain.FEEDBACK}/{$FEEDBACK_VOYAGE_ID_ARG}"

    fun chat(
        chatId: String,
        currentUserId: String,
        name: String,
        senderId: String,
    ): String {
        return "${AppRoutes.Chat.CHAT}/${Uri.encode(
            chatId,
        )}/${Uri.encode(currentUserId)}/${Uri.encode(name)}/${Uri.encode(senderId)}"
    }

    fun voyagerFeedback(voyageId: String?): String {
        return "${AppRoutes.Voyager.VOYAGER_FEEDBACK}/${Uri.encode(voyageId ?: "null")}"
    }

    fun captainFeedback(voyageId: String?): String {
        return "${AppRoutes.Captain.FEEDBACK}/${Uri.encode(voyageId ?: "null")}"
    }
}
