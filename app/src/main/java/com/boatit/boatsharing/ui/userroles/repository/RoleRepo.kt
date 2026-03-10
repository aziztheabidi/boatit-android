package com.boatit.boatsharing.ui.userroles.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.network.di.invalidateTokens
import com.boatit.boatsharing.ui.userroles.model.RoleRequest
import com.boatit.boatsharing.ui.userroles.model.RoleResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class RoleRepository(private val client: HttpClient) {
    suspend fun login(userid: String, role: String, token: String?): Result<RoleResponse> {
        return try {
            val response: HttpResponse =
                client.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.ROLE}") {
                    contentType(ContentType.Application.Json)
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $token")
                    }
                    setBody(RoleRequest(userid, role))
                }
            if (response.status == HttpStatusCode.OK) {
                val result = response.body<RoleResponse>()
                client.invalidateTokens()
                Result.success(result)
            } else {
                val result = response.body<RoleResponse>()
                Result.failure(Exception(result.Message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error registering: ${e.localizedMessage}", e))
        }
    }
}
