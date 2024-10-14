package com.djawnstj.babyway.auth.fixture

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.djawnstj.babyway.auth.application.command.LogoutCommand
import com.djawnstj.babyway.auth.application.command.TokenRefreshCommand
import com.djawnstj.babyway.auth.domain.AuthenticationCredentials
import com.djawnstj.babyway.auth.domain.Token
import com.djawnstj.babyway.auth.fake.FakeTokenProvider
import com.djawnstj.babyway.member.fixture.MemberFixture
import java.util.*

enum class TokenFixture(
    val jti: String?,
    val accessToken: FakeTokenProvider.FakeTokenObject?,
    val refreshToken: FakeTokenProvider.FakeTokenObject?,
) {
    // 정상 토큰
    `토큰 1`("jti1", FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, "jti1", 1000L), FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, "jti1", 5000L)),
    `토큰 2`("jti2", FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 2`.loginId, "jti2", 1000L), FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 2`.loginId, "jti2", 5000L)),
    `액세스 토큰 만료`("jti3", FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, "jti3", Date().time - 1000), FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, "jti3", Date().time + 5000)),
    `액세스 토큰 유효`("jti4", FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, "jti4", Date().time + 1000), FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, "jti4", Date().time + 5000)),

    // 비정상 토큰
    `저장 되지 않은 토큰`(null, FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, null, 1000L), FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, null, 5000L)),
    `회원 정보가 서로 다른 토큰`("jti2-1", FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, "jti2-1", 1000L), FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 2`.loginId, "jti2-1", 5000L)),
    `잘못 저장 된 토큰`("jti2-2", FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, "jti1", 1000L), FakeTokenProvider.FakeTokenObject(MemberFixture.`고객 1`.loginId, "jti1", Date().time + 5000)),
    ;

    fun `토큰 엔티티 생성`(): Token = AuthenticationCredentials(jti!!, objectMapper.writeValueAsString(accessToken), objectMapper.writeValueAsString(refreshToken))

    fun `로그 아웃 COMMAND 생성`(): LogoutCommand = LogoutCommand(jti!!)

    fun `토큰 재발급 COMMAND 생성`(): TokenRefreshCommand = TokenRefreshCommand(accessToken?.username!!, objectMapper.writeValueAsString(refreshToken))

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }
}
