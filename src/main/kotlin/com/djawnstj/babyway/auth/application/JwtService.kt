package com.djawnstj.babyway.auth.application

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.djawnstj.babyway.auth.domain.AuthenticationCredentials
import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.global.exception.ErrorCode
import com.djawnstj.babyway.member.domain.Member
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Base64
import java.util.Date
import java.util.UUID

@Service
class JwtService(
    private val tokenProvider: TokenProvider,
    private val jwtProperties: JwtProperties,
    private val objectMapper: ObjectMapper,
) {

    fun getUsername(token: String): String = tokenProvider.extractUsername(token).isValidClaimFromToken()

    fun getJti(token: String): String = tokenProvider.extractJti(token).isValidClaimFromToken()

    private fun String?.isValidClaimFromToken(): String {
        check (!isNullOrBlank()) {
            throw ApplicationException(ErrorCode.EMPTY_CLAIM)
        }

        return this
    }

    fun generateAuthenticationCredentials(member: Member): AuthenticationCredentials {
        val jti = UUID.randomUUID().toString()

        val accessToken = generateAccessToken(member, jti)
        val refreshToken = generateRefreshToken(member, jti)

        return AuthenticationCredentials(jti, accessToken, refreshToken)
    }

    fun generateAccessToken(
        member: Member,
        jti: String,
    ): String = tokenProvider.buildToken(member, jti, jwtProperties.accessTokenExpiration)

    fun generateRefreshToken(
        member: Member,
        jti: String,
    ): String = tokenProvider.buildToken(member, jti, jwtProperties.refreshTokenExpiration)

    fun isTokenValid(
        token: String,
        member: Member,
    ): Boolean = (getUsername(token) == member.loginId) && isTokenActive(token)

    private fun isTokenActive(token: String): Boolean {
        tokenProvider.extractExpiration(token)?.let {
            return it.after(Date())
        }

        return false
    }

    fun checkTokenExpiredByTokenString(token: String): Boolean {
        val parts = token.split(TOKEN_PART_SEPARATOR)

        checkValidTokenParts(parts)

        val payload = String(Base64.getDecoder().decode(parts[1]))

        val expiration = extractExpiration(payload)

        return ZonedDateTime.now().isAfter(ZonedDateTime.ofInstant(Instant.ofEpochMilli(expiration), ZoneId.systemDefault()))
    }

    private fun checkValidTokenParts(parts: List<String>) {
        check (parts.size == 3) {
            throw ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN)
        }
    }

    private fun extractExpiration(payload: String): Long {
        objectMapper.readValue(payload, object : TypeReference<MutableMap<String, String>>() {})[TOKEN_EXPIRATION_KEY]?.let {
            return it.toLong()
        }

        throw ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN)
    }

    companion object {
        private const val TOKEN_PART_SEPARATOR = "."
        private const val TOKEN_EXPIRATION_KEY = "exp"
    }
}
