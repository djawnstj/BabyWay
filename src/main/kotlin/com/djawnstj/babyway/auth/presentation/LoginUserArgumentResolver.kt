package com.djawnstj.babyway.auth.presentation

import com.djawnstj.babyway.auth.application.UserDetailsService
import com.djawnstj.babyway.auth.domain.Token
import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.global.exception.ErrorCode
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class LoginUserArgumentResolver(
    private val userDetailsService: UserDetailsService,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.hasParameterAnnotation(LoginUser::class.java)

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any {
        val (username, token) = getAuthAttribute(webRequest)

        val userDetails = userDetailsService.loadUserByUsername(username) ?: throw ApplicationException(ErrorCode.INVALID_ACCESS_TOKEN)
        userDetails.setToken(token)

        return userDetails
    }

    private fun getAuthAttribute(webRequest: NativeWebRequest): Pair<String, Token> {
        return webRequest.let { request ->
            val username = request.getAttribute(USERNAME_ATTRIBUTE_KEY, RequestAttributes.SCOPE_REQUEST)
            val token = request.getAttribute(TOKEN_ATTRIBUTE_KEY, RequestAttributes.SCOPE_REQUEST)

            checkAuthAttribute(username, token)

            return@let username.toString() to token as Token
        }
    }

    private fun checkAuthAttribute(username: Any?, token: Any?) {
        require(username != null && token != null) {
            throw ApplicationException(ErrorCode.UNAUTHORIZED_ACCESS_ATTEMPT)
        }
    }

    companion object {
        private const val USERNAME_ATTRIBUTE_KEY = "username"
        private const val TOKEN_ATTRIBUTE_KEY = "token"
    }
}
