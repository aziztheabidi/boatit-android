package com.boatit.boatsharing.ui.userroles.repository

import com.boatit.boatsharing.network.di.ApiConstants
import com.boatit.boatsharing.ui.login.model.LoginResponse
import com.boatit.boatsharing.ui.login.model.LoginRequest
import com.boatit.boatsharing.ui.userroles.model.RoleRequest
import com.boatit.boatsharing.ui.userroles.model.RoleResponse
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class RoleRepository(private val client: HttpClient) {
    suspend fun login(userid: String, role: String): Result<RoleResponse> {
        return try {
            val response: HttpResponse = client.post("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.ROLE}") {
                contentType(ContentType.Application.Json)
                setBody(RoleRequest(userid, role))
            }
            if (response.status == HttpStatusCode.OK) {
                val result = response.body<RoleResponse>()
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
