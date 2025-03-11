package com.abanapps.socailqrscanner.data_layer.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val user: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val status: String = "sent" // "sent", "delivered", "read"
)