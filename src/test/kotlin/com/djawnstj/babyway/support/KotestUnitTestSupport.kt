package com.djawnstj.babyway.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.djawnstj.babyway.auth.application.TokenCommandRepository
import com.djawnstj.babyway.auth.application.TokenQueryRepository
import com.djawnstj.babyway.auth.fake.FakeTokenRepository
import com.djawnstj.babyway.member.application.MemberCommandRepository
import com.djawnstj.babyway.member.application.MemberQueryRepository
import com.djawnstj.babyway.member.fake.FakeMemberRepository
import com.djawnstj.babyway.member.fake.FakePasswordEncoder
import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.security.crypto.password.PasswordEncoder

abstract class KotestUnitTestSupport : BehaviorSpec() {
    private val memberRepository = FakeMemberRepository()
    protected val memberQueryRepository: MemberQueryRepository = memberRepository
    protected val memberCommandRepository: MemberCommandRepository = memberRepository

    private val tokenRepository = FakeTokenRepository()
    protected val tokenQueryRepository: TokenQueryRepository = tokenRepository
    protected val tokenCommandRepository: TokenCommandRepository = tokenRepository

    protected val objectMapper: ObjectMapper = jacksonObjectMapper()
    protected val passwordEncoder: PasswordEncoder = FakePasswordEncoder()

    init {
        afterContainer {
            if (it.a.isWhen()) {
                memberRepository.init()
                tokenRepository.init()
            }
        }
    }
}
