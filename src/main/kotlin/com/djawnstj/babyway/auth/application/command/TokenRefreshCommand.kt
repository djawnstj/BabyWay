package com.djawnstj.babyway.auth.application.command

import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.global.exception.ErrorCode

data class TokenRefreshCommand(
    val loginId: String,
    val refreshToken: String?,
) {
    init {
        require (refreshToken != null) {
            throw ApplicationException(ErrorCode.EMPTY_PARAM)
        }
    }
}
