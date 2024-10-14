package com.djawnstj.babyway.auth.fake

import com.djawnstj.babyway.auth.application.TokenCommandRepository
import com.djawnstj.babyway.auth.application.TokenQueryRepository
import com.djawnstj.babyway.auth.domain.Token

class FakeTokenRepository : TokenCommandRepository, TokenQueryRepository {

    private val tokens = mutableMapOf<String, Token>()

    override fun save(token: Token): Token {
        tokens[token.jti] = token
        return token
    }

    override fun deleteByJti(jti: String) {
        tokens.remove(jti)
    }

    override fun findByJti(jti: String): Token? = tokens[jti]

    fun init() {
        tokens.clear()
    }
}
