package com.djawnstj.babyway.member.presentation.dto.response

import com.djawnstj.babyway.member.domain.Member
import com.djawnstj.babyway.member.domain.MemberType

data class MemberRegisterResponse(
    val id: Long,
    val loginId: String,
    val nickname: String,
    val memberType: MemberType,
) {
    constructor(member: Member): this(
        member.id,
        member.loginId,
        member.nickname,
        member.memberType,
    )
}
