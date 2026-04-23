package com.boatit.boatsharing.testutils

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json

fun httpClientReturningJson(
    json: String,
    status: HttpStatusCode = HttpStatusCode.OK,
): HttpClient =
    HttpClient(
        MockEngine {
            respond(
                content = json,
                status = status,
                headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
            )
        },
    ) {
        install(ContentNegotiation) {
            json()
        }
    }
