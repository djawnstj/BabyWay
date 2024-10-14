package com.djawnstj.babyway.member.application.command

import com.djawnstj.babyway.member.domain.Member
import com.djawnstj.babyway.member.domain.MemberType
import com.djawnstj.babyway.member.domain.Password
import org.springframework.security.crypto.password.PasswordEncoder

data class MemberCreateCommand(
    val loginId: String?,
    val password: String?,
    val nickname: String?,
    val memberType: MemberType?,
) {
    fun toEntity(passwordEncoder: PasswordEncoder): Member = Member(loginId, Password(password, passwordEncoder), nickname, memberType)
}
