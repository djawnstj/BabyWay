package com.djawnstj.babyway.auth.application

import com.djawnstj.babyway.auth.fake.FakeJwtService
import com.djawnstj.babyway.auth.fixture.TokenFixture
import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.member.fake.FakePasswordService
import com.djawnstj.babyway.member.fixture.MemberFixture
import com.djawnstj.babyway.support.KotestUnitTestSupport
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.throwable.shouldHaveMessage

@DisplayName("AuthCommandService 단위 테스트")
class AuthCommandServiceTest : KotestUnitTestSupport() {
    private val authCommandService = AuthCommandService(memberQueryRepository, FakePasswordService(), FakeJwtService(), tokenCommandRepository, tokenQueryRepository)

    init {
        Given("로그인 요청을 받았을 때") {
            When("해당하는 회원이 존재 한다면") {
                val memberFixture = MemberFixture.`고객 1`
                memberCommandRepository.save(memberFixture.`회원 엔티티 생성`())
                val command = memberFixture.`로그인 COMMAND 생성`()

                val actual = authCommandService.login(command)

                Then("토큰을 생성 후 반환 한다") {
                    actual shouldNotBe null
                }
            }

            When("존재하는 회원 정보가 아니라면") {
                val command = MemberFixture.`삭제된 고객 1`.`로그인 COMMAND 생성`()

                Then("예외를 던진다") {
                    shouldThrow<ApplicationException> {
                        authCommandService.login(command)
                    } shouldHaveMessage "아이디 또는 비밀번호를 잘못 입력 하셨습니다."
                }
            }
        }

        Given("로그아웃 요청을 받으면") {
            val command = TokenFixture.`토큰 1`.`로그 아웃 COMMAND 생성`()

            When("로그인 회원의 토큰을 이용해 토큰을 삭제한 후엔") {
                authCommandService.logout(command)

                val actual = tokenQueryRepository.findByJti("jti1")

                Then("저장소에서 해당 토큰을 조회할 수 없다") {
                    actual shouldBe null
                }
            }
        }

        Given("refreshToken 을 재발급 할때") {

            When("전달 받은 refreshToken 상태가 정상적이라면") {
                memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
                val tokenFixture = TokenFixture.`액세스 토큰 만료`
                tokenCommandRepository.save(tokenFixture.`토큰 엔티티 생성`())
                val tokenRefreshCommand = tokenFixture.`토큰 재발급 COMMAND 생성`()

                val actual = authCommandService.refreshToken(tokenRefreshCommand)

                Then("새로운 accessToken 을 반환 한다") {
                    assertSoftly {
                        actual.accessToken shouldStartWith "memberNo1:"
                        actual.refreshToken shouldStartWith "memberNo1:"
                    }
                }
            }

            When("전달 받은 refreshToken 의 jti 으로 찾은 토큰이 없다면") {
                memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
                val tokenRefreshCommand = TokenFixture.`저장 되지 않은 토큰`.`토큰 재발급 COMMAND 생성`()

                Then("예외를 던진다") {
                    shouldThrow<ApplicationException> {
                        authCommandService.refreshToken(tokenRefreshCommand)
                    } shouldHaveMessage "토큰을 찾을 수 없습니다."
                }
            }

            When("요청 헤더의 accessToken 과 본문의 refreshToken 의 회원 정보가 서로 다르면") {
                memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
                val tokenFixture = TokenFixture.`회원 정보가 서로 다른 토큰`
                tokenCommandRepository.save(tokenFixture.`토큰 엔티티 생성`())
                val tokenRefreshCommand = tokenFixture.`토큰 재발급 COMMAND 생성`()

                Then("예외를 던진다") {
                    shouldThrow<ApplicationException> {
                        authCommandService.refreshToken(tokenRefreshCommand)
                    } shouldHaveMessage "리프레시 토큰이 유효하지 않습니다."
                }
            }

            When("저장소에 저장 된 refreshToken 과 파라미터로 받은 refreshToken 이 다르면") {
                memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
                tokenCommandRepository.save(TokenFixture.`토큰 1`.`토큰 엔티티 생성`())
                val tokenRefreshCommand = TokenFixture.`잘못 저장 된 토큰`.`토큰 재발급 COMMAND 생성`()

                Then("예외를 던진다") {
                    shouldThrow<ApplicationException> {
                        authCommandService.refreshToken(tokenRefreshCommand)
                    } shouldHaveMessage "토큰이 일치하지 않습니다."
                }
            }

            When("전달 받은 토큰의 jti 로 찾은 accessToken 의 만료시간이 지나지 않았다면") {
                memberCommandRepository.save(MemberFixture.`고객 1`.`회원 엔티티 생성`())
                val tokenFixture = TokenFixture.`액세스 토큰 유효`
                tokenCommandRepository.save(tokenFixture.`토큰 엔티티 생성`())
                val tokenRefreshCommand = tokenFixture.`토큰 재발급 COMMAND 생성`()

                Then("예외를 던진다") {
                    shouldThrow<ApplicationException> {
                        authCommandService.refreshToken(tokenRefreshCommand)
                    } shouldHaveMessage "토큰을 재발급 할 수 없습니다."
                }
            }
        }
    }
}
