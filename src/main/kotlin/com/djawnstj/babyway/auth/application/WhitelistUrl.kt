package com.djawnstj.babyway.auth.application

import com.djawnstj.babyway.global.util.matchUrl
import com.djawnstj.babyway.member.domain.MemberType
import org.springframework.http.HttpMethod

data class WhitelistUrl(
    val url: String,
    val methods: List<HttpMethod> = listOf(),
    val role: MemberType = MemberType.USER,
) {
    fun match(uri: String, method: String): Boolean = isMatchUri(uri) && isMatchMethod(method)

    private fun isMatchMethod(method: String): Boolean {
        val httpMethod = HttpMethod.valueOf(method)

        return methods.isEmpty() || methods.any { it == httpMethod }
    }

    private fun isMatchUri(uri: String): Boolean = matchUrl(this.url, uri)
}
