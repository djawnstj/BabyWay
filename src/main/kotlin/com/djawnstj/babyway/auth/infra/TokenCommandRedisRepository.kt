package com.djawnstj.babyway.auth.infra

import com.fasterxml.jackson.databind.ObjectMapper
import com.djawnstj.babyway.auth.application.JwtProperties
import com.djawnstj.babyway.auth.application.TokenCommandRepository
import com.djawnstj.babyway.auth.domain.Token
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class TokenCommandRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val jwtProperties: JwtProperties,
) : TokenCommandRepository {

    private val valueOperations: ValueOperations<String, String>
        get() = this.redisTemplate.opsForValue()

    override fun save(token: Token): Token {
        valueOperations[token.jti] = objectMapper.writeValueAsString(token)
        valueOperations.getAndExpire(token.jti, jwtProperties.refreshTokenExpiration, TimeUnit.MILLISECONDS)

        return token
    }

    override fun deleteByJti(jti: String) {
        redisTemplate.delete(jti)
    }
}
