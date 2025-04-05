package com.dnl.appauthtemplate.ui.auth

import android.content.Intent
import androidx.core.net.toUri
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.ResponseTypeValues

/**
 * Implementation of [AuthManager] using AppAuth for Android.
 *
 * Handles OpenID Connect authentication and logout flows by interacting with
 * [AuthorizationService] and configuration fetched from the issuer.
 *
 * @property service The [AuthorizationService] used to initiate auth and logout requests.
 */
class AppAuthManager(
    private val service: AuthorizationService
) : AuthManager {

    /**
     * Starts the OpenID Connect login flow.
     *
     * This method fetches the authorization configuration from the issuer,
     * creates an [AuthorizationRequest], and launches the corresponding
     * intent through the provided [onLaunch] callback.
     *
     * @param onLaunch Callback that receives the intent to start the login flow.
     * @return A [Job] representing the login coroutine.
     */
    override suspend fun login(onLaunch: (Intent) -> Unit): Job = coroutineScope {
        launch {
            val config = receiveAuthConfig()
            val request = createAuthRequest(config)

            service.getAuthorizationRequestIntent(request).also(onLaunch)
        }
    }

    /**
     * Creates an [AuthorizationRequest] for the login flow using the provided
     * [AuthorizationServiceConfiguration].
     *
     * @param config The authorization configuration.
     * @return A configured [AuthorizationRequest] instance.
     */
    private fun createAuthRequest(config: AuthorizationServiceConfiguration): AuthorizationRequest =
        AuthorizationRequest.Builder(
            config,
            AuthConfig.CLIENT_ID,
            ResponseTypeValues.CODE,
            AuthConfig.LOGIN_REDIRECT_URI.toUri(),
        )
            .setScopes(AuthConfig.scopes)
            .setCodeVerifier(null) // TODO: Change if needed
            .build()

    /**
     * Starts the OpenID Connect logout flow.
     *
     * This method fetches the configuration, creates an [EndSessionRequest],
     * and launches the logout intent through the provided [onLaunch] callback.
     *
     * @param token The ID token hint used for logout.
     * @param onLaunch Callback that receives the intent to start the logout flow.
     * @return A [Job] representing the logout coroutine.
     */
    override suspend fun logOut(token: String, onLaunch: (Intent) -> Unit): Job = coroutineScope {
        launch {
            val config = receiveAuthConfig()
            val request = createEndSessionRequest(config, token)

            service.getEndSessionRequestIntent(request).also(onLaunch)
        }
    }

    /**
     * Creates an [EndSessionRequest] used for the logout flow.
     *
     * @param config The authorization service configuration.
     * @param token The ID token hint.
     * @return A configured [EndSessionRequest] instance.
     */
    private fun createEndSessionRequest(
        config: AuthorizationServiceConfiguration,
        token: String
    ): EndSessionRequest = EndSessionRequest.Builder(config)
        .setPostLogoutRedirectUri(AuthConfig.LOGOUT_REDIRECT_URI.toUri())
        .setIdTokenHint(token)
        .build()

    /**
     * Releases any resources held by the [AuthorizationService].
     */
    override fun dispose() = service.dispose()
}