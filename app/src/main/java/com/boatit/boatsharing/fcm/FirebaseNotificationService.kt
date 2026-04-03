package com.boatit.boatsharing.fcm

import android.content.Context
import com.android.volley.toolbox.StringRequest
import com.boatit.boatsharing.R
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import android.os.Build

class FirebaseNotificationService(private val firestore: FirebaseFirestore) {

    fun sendPushNotification(receiverId: String, message: String, context: Context) {
        firestore.collection("users").document(receiverId).get()
            .addOnSuccessListener { document ->
                val token = document.getString("fcmToken")
                token?.let { sendFCMNotification(it, message, context) }
            }
    }

    private fun sendFCMNotification(fcmToken: String, message: String, context: Context) {

        val fcmUrl = "https://fcm.googleapis.com/v1/projects/socail-qr-scanner/messages:send"

        val jsonBody = JSONObject().apply {
            put("message", JSONObject().apply {
                put("token", fcmToken)
                put("notification", JSONObject().apply {
                    put("title", "New Message")
                    put("body", message)
                })
                put("data", JSONObject().apply {
                    put("chatId", fcmToken)
                })
            })
        }

        val requestBody = jsonBody.toString()

        val request = object : StringRequest(Method.POST, fcmUrl, Response.Listener {
        }, Response.ErrorListener {
        }) {
            override fun getBody(): ByteArray = requestBody.toByteArray()

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf(
                    "Authorization" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) "Bearer ${
//                        getAccessToken(context)
                    "key=55fada9bba8f18fc11160252e86a2e133cfab9ed"
                    }" else "key=55fada9bba8f18fc11160252e86a2e133cfab9ed",
                    "Content-Type" to "application/json"
                )
            }
        }
        Volley.newRequestQueue(context).add(request)
    }

//    private fun getAccessToken(context: Context): String {
//        val inputStream = context.resources.openRawResource(R.raw.socail_buddy_key)
//        val googleCredentials = GoogleCredentials.fromStream(inputStream)
//            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
//        return googleCredentials.refreshAccessToken().tokenValue
//    }
}
