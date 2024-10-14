package com.djawnstj.babyway.member.presentation.dto.request

import com.djawnstj.babyway.member.application.command.MemberCreateCommand
import com.djawnstj.babyway.member.domain.MemberType


data class MemberRegisterRequest(
    val loginId: String?,
    val password: String?,
    val nickname: String?,
    val memberType: MemberType?
) {
    fun toCommand(): MemberCreateCommand = MemberCreateCommand(loginId, password, nickname, memberType)
}
