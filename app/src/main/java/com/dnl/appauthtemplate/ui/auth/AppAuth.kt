package com.dnl.appauthtemplate.ui.auth

import android.content.Intent
import kotlinx.coroutines.Job

/**
 * Interface for managing authentication operations such as login and logout.
 *
 * Provides a contract for handling OpenID Connect flows using intent-based
 * interactions, suitable for use with libraries like AppAuth for Android.
 */
interface AuthManager {

    /**
     * Initiates the login flow.
     *
     * Implementations should prepare the authorization request and provide
     * an [Intent] via the [onLaunch] callback to be used for launching the
     * authentication UI.
     *
     * @param onLaunch Callback to launch the authorization [Intent].
     * @return A [Job] representing the coroutine running the login flow.
     */
    suspend fun login(onLaunch: (Intent) -> Unit): Job

    /**
     * Initiates the logout (end session) flow.
     *
     * Implementations should prepare the end session request and provide
     * an [Intent] via the [onLaunch] callback to be used for launching the
     * logout UI.
     *
     * @param token The ID token hint used to identify the session to terminate.
     * @param onLaunch Callback to launch the logout [Intent].
     * @return A [Job] representing the coroutine running the logout flow.
     */
    suspend fun logOut(token: String, onLaunch: (Intent) -> Unit): Job

    /**
     * Releases any held resources, such as instances of [AuthorizationService].
     */
    fun dispose()
}