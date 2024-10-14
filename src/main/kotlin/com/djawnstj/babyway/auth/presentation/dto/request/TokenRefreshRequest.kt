package com.djawnstj.babyway.auth.presentation.dto.request

import com.djawnstj.babyway.auth.application.command.TokenRefreshCommand

data class TokenRefreshRequest(
    val refreshToken: String?,
) {
    fun toCommand(username: String): TokenRefreshCommand = TokenRefreshCommand(username, refreshToken)
}
