package com.djawnstj.babyway.auth.presentation.dto.response

import com.djawnstj.babyway.auth.domain.AuthenticationCredentials

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
) {
    constructor(authenticationCredentials: AuthenticationCredentials) : this(
        authenticationCredentials.accessToken,
        authenticationCredentials.refreshToken
    )
}
