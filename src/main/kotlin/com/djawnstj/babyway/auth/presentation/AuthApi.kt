package com.djawnstj.babyway.auth.presentation

import com.djawnstj.babyway.auth.application.AuthCommandService
import com.djawnstj.babyway.auth.application.command.LogoutCommand
import com.djawnstj.babyway.auth.presentation.dto.request.LoginRequest
import com.djawnstj.babyway.auth.presentation.dto.request.TokenRefreshRequest
import com.djawnstj.babyway.auth.presentation.dto.response.LoginResponse
import com.djawnstj.babyway.auth.presentation.dto.response.TokenRefreshResponse
import com.djawnstj.babyway.global.common.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class AuthApi(
    private val authCommandService: AuthCommandService
) {

    @PostMapping("/api/auth/login")
    fun login(@RequestBody request: LoginRequest): SuccessResponse<LoginResponse> = SuccessResponse(LoginResponse(authCommandService.login(request.toCommand())))

    @DeleteMapping("/api/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@LoginUser userDetails: UserDetails) = authCommandService.logout(LogoutCommand(userDetails.token.jti))

    @PostMapping("/api/auth/refresh")
    fun refresh(@LoginUser userDetails: UserDetails, @RequestBody request: TokenRefreshRequest): SuccessResponse<TokenRefreshResponse> =
        SuccessResponse(TokenRefreshResponse(authCommandService.refreshToken(request.toCommand(userDetails.username))))
}
