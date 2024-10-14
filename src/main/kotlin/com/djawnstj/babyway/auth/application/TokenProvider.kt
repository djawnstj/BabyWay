package com.djawnstj.babyway.auth.application

import com.djawnstj.babyway.member.domain.Member
import java.util.Date
import javax.crypto.SecretKey

interface TokenProvider {
    val secretKey: SecretKey

    fun extractUsername(token: String): String?

    fun extractJti(token: String): String?

    fun extractExpiration(token: String): Date?

    fun buildToken(
        member: Member,
        jti: String,
        expiration: Long,
        additionalClaims: Map<String, Any> = emptyMap(),
    ): String
}
