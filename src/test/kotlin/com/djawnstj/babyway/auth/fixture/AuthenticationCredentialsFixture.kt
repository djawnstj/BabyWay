package com.djawnstj.babyway.auth.fixture

import com.djawnstj.babyway.auth.domain.Token

enum class AuthenticationCredentialsFixture(
    val jti: String,
    val accessToken: String,
    val refreshToken: String,
) {
    `토큰 1`("1", "accessToken1", "refreshToken1"),
    `토큰 2`("2", "accessToken2", "refreshToken2"),
    ;

    fun toToken(): Token = Token(jti, accessToken, refreshToken)
}
