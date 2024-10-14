package com.djawnstj.babyway.member.fixture

import com.djawnstj.babyway.auth.application.command.LoginCommand
import com.djawnstj.babyway.auth.presentation.dto.request.LoginRequest
import com.djawnstj.babyway.member.application.command.MemberCreateCommand
import com.djawnstj.babyway.member.domain.Member
import com.djawnstj.babyway.member.domain.MemberType
import com.djawnstj.babyway.member.domain.Password
import com.djawnstj.babyway.member.fake.FakePasswordEncoder
import com.djawnstj.babyway.member.presentation.dto.request.MemberRegisterRequest
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

enum class MemberFixture(
    val loginId: String?,
    val password: String?,
    val nickname: String?,
    val memberType: MemberType?,
    val deletedAt: LocalDateTime? = null
) {
    // 정상 객체
    `고객 1`("memberNo1", "password123", "nickname", MemberType.USER),
    `고객 2`("memberNo2", "password456", "nickname2", MemberType.USER),
    `삭제된 고객 1`("memberNo3", "password123", "nickname", MemberType.USER, LocalDateTime.now()),

    // 비정상 객체
    `아이디 NULL 회원`(null, "password123", "nickname", MemberType.USER),
    `아이디 공백 회원`("", "password123", "nickname", MemberType.USER),
    `아이디 형식 비정상 회원1`("한글 아이디 123", "password123", "nickname", MemberType.USER),
    `아이디 형식 비정상 회원2`("member", "password123", "nickname", MemberType.USER),
    `아이디 형식 비정상 회원3`("member1234567890", "password123", "nickname", MemberType.USER),
    `비밀번호 NULL 회원`("memberNo1", null, "nickname", MemberType.USER),
    `비밀번호 공백 회원`("memberNo1", "", "nickname", MemberType.USER),
    `비밀번호 형식 비정상 회원1`("memberNo1", "pass123", "nickname", MemberType.USER),
    `비밀번호 형식 비정상 회원2`("memberNo1", "longlonglong1234", "nickname", MemberType.USER),
    `비밀번호 형식 비정상 회원3`("memberNo1", "한국어비밀번호1", "nickname", MemberType.USER),
    `비밀번호 형식 비정상 회원4`("memberNo1", "!@#$%^&*", "nickname", MemberType.USER),
    `비밀번호 형식 비정상 회원5`("memberNo1", "!@#$%^&*1", "nickname", MemberType.USER),
    `닉네임 NULL 회원`("memberNo1", "password123", null, MemberType.USER),
    `닉네임 공백 회원`("memberNo1", "password123", "", MemberType.USER),
    `닉네임 형식 비정상 회원1`("memberNo1", "password123", "nicknam", MemberType.USER),
    `닉네임 형식 비정상 회원2`("memberNo1", "password123", "longnickname1234", MemberType.USER),
    `닉네임 형식 비정상 회원3`("memberNo1", "password123", "nickname!", MemberType.USER),
    `회원 유형 NULL 회원`("memberNo1", "password123", "nickname", null),
    ;

    fun `회원 엔티티 생성`(passwordEncoder: PasswordEncoder = FakePasswordEncoder.INSTANCE): Member =
        Member(loginId, Password(password, passwordEncoder), nickname, memberType).also {
            if (deletedAt != null) it.delete()
        }

    fun `회원 생성 COMMAND 생성`(): MemberCreateCommand =
        MemberCreateCommand(loginId, password, nickname, memberType)

    fun `회원 가입 요청 DTO 생성`(): MemberRegisterRequest = MemberRegisterRequest(loginId, password, nickname, memberType)

    fun `로그인 COMMAND 생성`(): LoginCommand = LoginCommand(loginId, password)

    fun `로그인 요청 DTO 생성`(): LoginRequest = LoginRequest(loginId, password)

}
