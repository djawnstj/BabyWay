package com.djawnstj.babyway.auth.presentation.dto.request

import com.djawnstj.babyway.auth.application.command.LoginCommand

data class LoginRequest(
    val loginId: String?,
    val password: String?
) {
    fun toCommand(): LoginCommand = LoginCommand(loginId, password)
}
