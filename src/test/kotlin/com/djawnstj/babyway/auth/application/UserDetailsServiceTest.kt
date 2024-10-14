package com.djawnstj.babyway.auth.application

import com.djawnstj.babyway.auth.presentation.UserDetails
import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.member.application.MemberCommandRepository
import com.djawnstj.babyway.member.fixture.MemberFixture
import com.djawnstj.babyway.support.KotestIntegrationTestSupport
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.equality.shouldBeEqualToUsingFields
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.throwable.shouldHaveMessage
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("UserDetailService 통합 테스트")
class UserDetailsServiceTest : KotestIntegrationTestSupport() {

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var memberCommandRepository: MemberCommandRepository

    init {
        Given("가입 된 회원을 조회할 때") {

            When("유효한 회원이 존재 한다면") {
                val member = MemberFixture.`고객 1`.`회원 엔티티 생성`()
                memberCommandRepository.save(member)

                Then("회원 정보를 반환 한다") {
                    val actual = userDetailsService.loadUserByUsername(member.loginId)

                    actual.shouldNotBeNull()
                        .shouldBeEqualToUsingFields(UserDetails(1L, "memberNo1"), UserDetails::id, UserDetails::username)
                }
            }

            When("가입 된 회원은 있지만 탈퇴 한 회원 이라면") {
                val member = MemberFixture.`고객 1`.`회원 엔티티 생성`()
                member.delete()
                memberCommandRepository.save(member)

                Then("예외를 던진다") {
                    shouldThrow<ApplicationException> {
                        userDetailsService.loadUserByUsername(member.loginId)
                    } shouldHaveMessage "일치하는 회원 정보를 찾을 수 없습니다."
                }
            }

            When("가입 된 회원은 없다면") {
                Then("예외를 던진다") {
                    shouldThrow<ApplicationException> {
                        userDetailsService.loadUserByUsername(MemberFixture.`고객 1`.loginId!!)
                    } shouldHaveMessage "일치하는 회원 정보를 찾을 수 없습니다."
                }
            }
        }
    }
}
