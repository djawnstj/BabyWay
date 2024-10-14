package com.djawnstj.babyway.member.application

import com.djawnstj.babyway.member.domain.Member

interface MemberCommandRepository {
    fun save(member: Member): Member
}
