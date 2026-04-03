package com.boatit.boatsharing.ui.chat.repository

import android.util.Log
import com.abanapps.socailqrscanner.data_layer.model.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore

class ChatRepository(private val firestore: FirebaseFirestore) {

    fun sendMessage(chatId: String, senderId: String, message: String) {
        val newMessage = ChatMessage(user = senderId, text = message, status = "sent")

        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(newMessage)
    }

    fun markMessagesAsRead(chatId: String, currentUserId: String) {
        val messagesRef = firestore.collection("chats")
            .document(chatId)
            .collection("messages")

        messagesRef.whereEqualTo("status", "sent")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    doc.reference.update("status", "read")
                        .addOnSuccessListener {
                            Log.d("FirestoreUpdate", "Message ${doc.id} marked as read")
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirestoreUpdate", "Failed to update message ${doc.id}", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreUpdate", "Error fetching unread messages", e)
            }
    }

    fun listenForMessages(chatId: String, currentUserId: String , onMessagesUpdated: (List<ChatMessage>) -> Unit) {
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val messages = snapshot.toObjects(ChatMessage::class.java)

                snapshot.documents.forEach { doc ->
                    val status = doc.getString("status")
                    val senderId = doc.getString("user")
                    if (status == "sent" && senderId != currentUserId) { // Only update if message is unread & sent by others
                        doc.reference.update("status", "read")
                            .addOnSuccessListener {
                                Log.d("FirestoreUpdate", "Message ${doc.id} marked as read")
                            }
                            .addOnFailureListener { e ->
                                Log.e("FirestoreUpdate", "Failed to update message ${doc.id}", e)
                            }
                    }
                }

                onMessagesUpdated(messages)
            }
    }
}

