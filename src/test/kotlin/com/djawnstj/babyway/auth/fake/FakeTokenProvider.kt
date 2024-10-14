package com.djawnstj.babyway.auth.fake

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.djawnstj.babyway.auth.application.TokenProvider
import com.djawnstj.babyway.member.domain.Member
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

private const val TEST_SECRET_KEY = "TestSecretKey"

class FakeTokenProvider : TokenProvider {
    override val secretKey: SecretKey = SecretKeySpec(TEST_SECRET_KEY.toByteArray(), "HmacSHA256")

    private val objectMapper = jacksonObjectMapper()

    override fun extractUsername(token: String): String? {
        val tokenObject = objectMapper.readValue(token, FakeTokenObject::class.java)
        return tokenObject.username
    }

    override fun extractJti(token: String): String? {
        val tokenObject = objectMapper.readValue(token, FakeTokenObject::class.java)
        return tokenObject.jti
    }

    override fun extractExpiration(token: String): Date? {
        val tokenObject = objectMapper.readValue(token, FakeTokenObject::class.java)
        return tokenObject.exp?.let { Date(it) }
    }

    override fun buildToken(member: Member, jti: String, expiration: Long, additionalClaims: Map<String, Any>): String {
        val expirationDate = System.currentTimeMillis() + expiration
        return "${member.loginId}:$jti:$expirationDate:${additionalClaims.entries.joinToString(",") { "${it.key}=${it.value}" }}"
    }

    data class FakeTokenObject(val username: String?, val jti: String?, val exp: Long?)
}
