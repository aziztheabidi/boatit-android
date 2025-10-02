package com.boatit.boatsharing.network.di

import com.boatit.boatsharing.network.interceptors.NetworkInterceptor
import com.boatit.boatsharing.utils.AppConstants
import com.boatit.boatsharing.utils.prefmanager.TokenProvider
import com.boatit.boatsharing.utils.session.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json

/**
 * Custom Ktor plugin for NetworkInterceptor integration
 * This approach works reliably with Ktor 2.x
 */
class NetworkInterceptorPlugin(private val sessionManager: SessionManager) {
    class Config {
        var sessionManager: SessionManager? = null
    }

    companion object Plugin : HttpClientPlugin<Config, NetworkInterceptorPlugin> {
        override val key = AttributeKey<NetworkInterceptorPlugin>("NetworkInterceptor")

        override fun prepare(block: Config.() -> Unit): NetworkInterceptorPlugin {
            val config = Config().apply(block)
            return NetworkInterceptorPlugin(config.sessionManager!!)
        }

        override fun install(plugin: NetworkInterceptorPlugin, scope: HttpClient) {
            val networkInterceptor = NetworkInterceptor(plugin.sessionManager)

            // Intercept requests using the standard Ktor pipeline
            scope.requestPipeline.intercept(HttpRequestPipeline.State) { request ->
                try {
                    // Safe casting with type checking
                    val requestBuilder = request as? HttpRequestBuilder
                        ?: throw IllegalStateException("Expected HttpRequestBuilder at HttpRequestPipeline.State")
                    
                    val response = networkInterceptor.intercept(requestBuilder) { req ->
                        val result = proceedWith(req)
                        result as? HttpResponse
                            ?: throw IllegalStateException("Expected HttpResponse from proceedWith")
                    }
                    proceedWith(response)
                } catch (e: Exception) {
                    // Log the error for debugging
                    android.util.Log.e("NetworkInterceptorPlugin", "Pipeline interception failed: ${e.message}")
                    throw e
                }
            }
        }
    }
}

fun createKtorClient(
    tokenProvider: TokenProvider,
    sessionManager: SessionManager
): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
        install(Auth) {
            bearer {
                loadTokens {
                    tokenProvider.getAccessToken()?.let { token ->
                        BearerTokens(token, tokenProvider.getRefreshToken() ?: "")
                    }
                }
            }
        }
        defaultRequest {
            val token = tokenProvider.getAccessToken()
            println("Hello" + token)
            if (token != null) {
                headers.append(HttpHeaders.Authorization, "Bearer $token")
            }else if (AppConstants.JWT_TOKEN != null){
                headers.append(HttpHeaders.Authorization, "Bearer " + AppConstants.JWT_TOKEN)
            }
        }
        
        // Disable built-in retry plugin to use our custom NetworkInterceptor
        install(HttpRequestRetry) {
            maxRetries = 0
        }
        
        // Install our custom NetworkInterceptor plugin
        install(NetworkInterceptorPlugin) {
            this.sessionManager = sessionManager
        }
    }
}