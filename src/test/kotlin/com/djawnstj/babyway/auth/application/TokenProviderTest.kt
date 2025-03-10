package com.djawnstj.babyway.auth.application

import com.djawnstj.babyway.member.fake.FakePasswordEncoder
import com.djawnstj.babyway.member.fixture.MemberFixture
import com.djawnstj.babyway.support.KotestIntegrationTestSupport
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@DisplayName("TokenProvider 통합 테스트")
class TokenProviderTest : KotestIntegrationTestSupport() {

    @Autowired
    private lateinit var tokenProvider: TokenProvider

    init {
        Given("토큰을 이용해") {
            val memberEntity = MemberFixture.`고객 1`.`회원 엔티티 생성`(FakePasswordEncoder.INSTANCE)
            val jti = "jti"
            val expiration = 5000L
            val token = tokenProvider.buildToken(memberEntity, jti, expiration)

            When("username 을 추출 하여") {
                val actual = tokenProvider.extractUsername(token)

                Then("정상 적인 username 을 반환 한다") {
                    actual shouldBe "memberNo1"
                }
            }

            When("jti 를 추출 하여") {
                val actual = tokenProvider.extractJti(token)

                Then("정상 적인 jti 를 반환 한다") {
                    actual shouldBe "jti"
                }
            }

            When("만료 시간을 추출 하여") {
                val actual = tokenProvider.extractExpiration(token)

                Then("정상 적인 만료 시간을 반환 한다") {
                    actual.shouldNotBeNull().after(Date())
                }
            }
        }

        Given("회원 정보와 jti 를 받아") {
            val memberEntity = MemberFixture.`고객 1`.`회원 엔티티 생성`(FakePasswordEncoder.INSTANCE)
            val jti = "jti"
            val expiration = 1000L

            When("새로운 토큰을 생성 하여") {
                val actual = tokenProvider.buildToken(memberEntity, jti, expiration)

                Then("정상 적인 토큰을 반환 한다") {
                    actual.shouldNotBeNull().shouldNotBeBlank()
                }
            }
        }
    }
}
