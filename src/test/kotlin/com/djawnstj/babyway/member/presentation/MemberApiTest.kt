package com.djawnstj.babyway.member.presentation

import com.djawnstj.babyway.global.common.SuccessResponse
import com.djawnstj.babyway.member.domain.MemberType
import com.djawnstj.babyway.member.fixture.MemberFixture
import com.djawnstj.babyway.member.presentation.dto.response.MemberRegisterResponse
import com.djawnstj.babyway.support.KotestControllerTestSupport
import io.kotest.assertions.assertSoftly
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.equality.shouldBeEqualUsingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

@DisplayName("MemberApi 테스트")
class MemberApiTest : KotestControllerTestSupport() {
    init {
        Given("회원 가입 요청이 왔을 때") {
            When("정상 적인 아이디, 비밀번호를 받았다면") {
                val actual = client.post("/api/members") {
                    setBody(MemberFixture.`고객 2`.`회원 가입 요청 DTO 생성`())
                }

                Then("201 상태 코드를 반환 한다") {
                    actual.status shouldBe HttpStatusCode.Created
                }

                Then("가입 된 회원의 id, loginId 을 반환 한다") {
                    val body: SuccessResponse<MemberRegisterResponse> = actual.body()

                    assertSoftly {
                        body.data shouldNotBe null
                        body.data!! shouldBeEqualUsingFields MemberRegisterResponse(2L, "memberNo2", "nickname2", MemberType.USER)
                    }
                }
            }
        }
    }
}
