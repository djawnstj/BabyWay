package com.djawnstj.babyway.auth.application

import com.djawnstj.babyway.auth.application.command.LoginCommand
import com.djawnstj.babyway.auth.application.command.LogoutCommand
import com.djawnstj.babyway.auth.application.command.TokenRefreshCommand
import com.djawnstj.babyway.auth.domain.AuthenticationCredentials
import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.global.exception.ErrorCode
import com.djawnstj.babyway.member.application.MemberQueryRepository
import com.djawnstj.babyway.member.application.PasswordService
import com.djawnstj.babyway.member.domain.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthCommandService(
    private val memberQueryRepository: MemberQueryRepository,
    private val passwordService: PasswordService,
    private val jwtService: JwtService,
    private val tokenCommandRepository: TokenCommandRepository,
    private val tokenQueryRepository: TokenQueryRepository,
) {

    fun login(loginCommand: LoginCommand): AuthenticationCredentials {
        val member = memberQueryRepository.findByLoginIdAndNotDeleted(loginCommand.loginId).validateLoginField(loginCommand)

        val authenticationCredentials = jwtService.generateAuthenticationCredentials(member)
        tokenCommandRepository.save(authenticationCredentials)

        return authenticationCredentials
    }

    private fun Member?.validateLoginField(loginCommand: LoginCommand): Member {
        check(this != null&& passwordService.matches(loginCommand.password, password)) {
            throw ApplicationException(ErrorCode.NOT_MATCH_LOGIN_FIELD)
        }

        return this
    }

    fun logout(logoutCommand: LogoutCommand) {
        tokenCommandRepository.deleteByJti(logoutCommand.jti)
    }

    fun refreshToken(tokenRefreshCommand: TokenRefreshCommand): AuthenticationCredentials {
        val presentedRefreshToken = tokenRefreshCommand.refreshToken

        val member = memberQueryRepository.findByLoginIdAndNotDeleted(tokenRefreshCommand.loginId) ?: throw ApplicationException(ErrorCode.MEMBER_NOT_FOUND)

        val foundToken = tokenQueryRepository.findByJti(jwtService.getJti(presentedRefreshToken!!))
        validateToken(foundToken, presentedRefreshToken, member)

        val authenticationCredentials = jwtService.generateAuthenticationCredentials(member)

        tokenCommandRepository.save(authenticationCredentials)
        tokenCommandRepository.deleteByJti(foundToken!!.jti)

        return authenticationCredentials
    }

    private fun validateToken(
        authenticationCredentials: AuthenticationCredentials?,
        refreshToken: String,
        member: Member,
    ) {
        check (authenticationCredentials != null) {
            throw ApplicationException(ErrorCode.TOKEN_NOT_FOUND)
        }

        check (jwtService.isTokenValid(refreshToken, member)) {
            throw ApplicationException(ErrorCode.INVALID_REFRESH_TOKEN)
        }

        check(authenticationCredentials.isSameRefreshToken(refreshToken)) {
            throw ApplicationException(ErrorCode.MISS_MATCH_TOKEN)
        }

        validateActiveAccessToken(authenticationCredentials.accessToken, authenticationCredentials.jti)
    }

    private fun validateActiveAccessToken(
        accessToken: String,
        jti: String,
    ) {
        if (!jwtService.checkTokenExpiredByTokenString(accessToken)) {
            tokenCommandRepository.deleteByJti(jti)
            throw ApplicationException(ErrorCode.INVALID_TOKEN_REISSUE_REQUEST)
        }
    }
}
