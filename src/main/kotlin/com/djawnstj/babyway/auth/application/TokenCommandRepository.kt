package com.djawnstj.babyway.auth.application

import com.djawnstj.babyway.auth.domain.Token

interface TokenCommandRepository {
    fun save(token: Token): Token

    fun deleteByJti(jti: String)
}
