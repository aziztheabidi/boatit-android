package com.abanapps.socailqrscanner.data_layer.model

import com.google.firebase.Timestamp
import kotlinx.serialization.Serializable
import java.util.UUID

data class ChatMessage(
    val user: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val status: String = "sent" // "sent", "delivered", "read"
)
