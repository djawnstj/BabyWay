package com.djawnstj.babyway.auth.application

import com.djawnstj.babyway.auth.domain.Token

interface TokenQueryRepository {
    fun findByJti(jti: String): Token?
}
