package com.boatit.boatsharing.ui.signup.business.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.signup.business.model.SaveBusinessLogoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.utils.io.streams.asInput
import java.io.File

class BusinessLogoRepository(private val httpClient: HttpClient) {

    suspend fun saveBusinessLogo(userId: String, logoFile: File): Result<SaveBusinessLogoResponse> {
        return try {
            val response: HttpResponse = httpClient.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.SAVE_BUSINESS_LOGO}") {
                contentType(ContentType.MultiPart.FormData)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("UserId", userId)
                            appendInput(
                                key = "Logo",
                                headers = Headers.build {
                                    append(HttpHeaders.ContentDisposition, "form-data; name=\"Logo\"; filename=\"${logoFile.name}\"")
                                    append(HttpHeaders.ContentType, ContentType.Image.Any.toString()) // Dynamic image type
                                }
                            ) { logoFile.inputStream().asInput() }
                        }
                    )
                )
            }
            if (response.status == HttpStatusCode.OK) {
                val result: SaveBusinessLogoResponse = response.body()
                Result.success(result)
            } else {
                Result.failure(Exception("API Error: ${response.status} - ${response.bodyAsText()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.localizedMessage}", e))
        }
    }
}

