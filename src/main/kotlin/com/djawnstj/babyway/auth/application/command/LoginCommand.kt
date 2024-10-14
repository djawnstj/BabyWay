package com.djawnstj.babyway.auth.application.command

data class LoginCommand(
    val loginId: String?,
    val password: String?,
)
