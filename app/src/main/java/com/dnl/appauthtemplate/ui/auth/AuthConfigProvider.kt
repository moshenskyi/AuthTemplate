package com.dnl.appauthtemplate.ui.auth

import androidx.core.net.toUri
import net.openid.appauth.AuthorizationServiceConfiguration
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Asynchronously fetches the OpenID Connect [AuthorizationServiceConfiguration] from the issuer URI.
 *
 * This function uses [suspendCoroutine] to wrap the asynchronous callback-style
 * `fetchFromIssuer` method provided by AppAuth into a suspending function.
 *
 * @return The [AuthorizationServiceConfiguration] fetched from the issuer.
 * @throws AuthorizationException if fetching the configuration fails.
 * @throws Exception if an unexpected error occurs while resuming the coroutine.
 *
 * Example usage:
 * ```
 * val config = receiveAuthConfig()
 * ```
 */
suspend fun receiveAuthConfig(): AuthorizationServiceConfiguration =
    suspendCoroutine { continuation ->
        val uri = AuthConfig.OPEN_ID_CONNECT_ISSUER_URI.toUri()

        AuthorizationServiceConfiguration.fetchFromIssuer(uri) { config, exception ->
            if (exception != null) {
                continuation.resumeWithException(exception)
            } else if (config != null) {
                try {
                    continuation.resume(config)
                } catch (ex: Exception) {
                    continuation.resumeWithException(ex)
                }
            }
        }
    }