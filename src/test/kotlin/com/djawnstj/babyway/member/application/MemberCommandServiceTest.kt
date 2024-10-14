package com.djawnstj.babyway.member.application

import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.member.fixture.MemberFixture
import com.djawnstj.babyway.support.KotestUnitTestSupport
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage

@DisplayName("MemberCommandService 테스트")
class MemberCommandServiceTest : KotestUnitTestSupport() {
    private val memberCommandService = MemberCommandService(memberCommandRepository, memberQueryRepository, passwordEncoder)

    init {
        Given("새로운 회원을 생성할 때") {
            val memberCommand = MemberFixture.`고객 1`.`회원 생성 COMMAND 생성`()

            When("기존 회원 중 중복된 loginId 가 없는 경우") {
                val actual = memberCommandService.createMember(memberCommand)

                Then("회원을 생성 후 key 값을 반환 한다") {
                    actual shouldNotBe null
                }
            }

            When("기존 회원 중 중복된 loginId 가 존재 하는 경우") {
                memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())

                Then("예외를 던진다") {
                    shouldThrow<ApplicationException> {
                        memberCommandService.createMember(memberCommand)
                    } shouldHaveMessage "이미 존재 하는 아이디 입니다."
                }
            }
        }
    }
}
