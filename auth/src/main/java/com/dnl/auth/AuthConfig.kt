package com.dnl.auth

object AuthConfig {
    const val OPEN_ID_CONNECT_ISSUER_URI = "Your issuer URI"
    const val CLIENT_ID = "Your clientID"
    const val LOGIN_REDIRECT_URI = "Your login redirect_uri"
    const val LOGOUT_REDIRECT_URI = "Your logout redirect_uri"

    val scopes = listOf("openid", "auth")
}