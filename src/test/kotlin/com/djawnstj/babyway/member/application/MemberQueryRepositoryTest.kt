package com.djawnstj.babyway.member.application

import com.djawnstj.babyway.member.fixture.MemberFixture
import com.djawnstj.babyway.member.infra.MemberJpaRepository
import com.djawnstj.babyway.support.KotestIntegrationTestSupport
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

@DisplayName("MemberQueryRepository 테스트")
class MemberQueryRepositoryTest : KotestIntegrationTestSupport() {

    @Autowired
    private lateinit var memberQueryRepository: MemberQueryRepository

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    init {
        Given("중복 된 loginId 존재 여부를 찾을 때") {
            val member = MemberFixture.`고객 1`.`회원 엔티티 생성`(passwordEncoder)
            val loginId = member.loginId

            When("이미 저장 된 회원 중 중복된 loginId 가 없는 경우") {
                val actual = memberQueryRepository.existsByLoginIdAndNotDeleted(loginId)

                Then("false 를 반환 한다") {
                    actual shouldBe false
                }
            }

            When("이미 저장 된 회원 중 중복된 loginId 가 있는 경우") {
                memberJpaRepository.save(member)
                val actual = memberQueryRepository.existsByLoginIdAndNotDeleted(loginId)

                Then("true 를 반환 한다") {
                    actual shouldBe true
                }
            }
        }

        Given("회원 ID 를 이용해 저장된 회원을 찾을 때") {
            val memberFixture = MemberFixture.`고객 1`.`회원 엔티티 생성`(passwordEncoder)

            When("ID 가 일치 하면서 삭제 플래그가 null 인 회원이 있다면") {
                val member = memberJpaRepository.saveAndFlush(memberFixture)
                val actual = memberQueryRepository.findByIdAndNotDeleted(member.id)

                Then("회원 엔티티를 반환 한다") {
                    actual shouldNotBe null
                }
            }

            When("ID 가 일치 하는 회원이 없다면") {
                val actual = memberQueryRepository.findByIdAndNotDeleted(1L)

                Then("null 을 반환 한다") {
                    actual shouldBe null
                }
            }

            When("ID 가 일치 하는 회원이 있지만 삭제 플래그가 null 이라면") {
                val member = memberJpaRepository.save(memberFixture)
                member.delete()
                memberJpaRepository.saveAndFlush(member)

                val actual = memberQueryRepository.findByIdAndNotDeleted(member.id)

                Then("null 을 반환 한다") {
                    actual shouldBe null
                }
            }
        }
    }
}
