package com.djawnstj.babyway.member.fake

import com.djawnstj.babyway.member.application.MemberCommandRepository
import com.djawnstj.babyway.member.application.MemberQueryRepository
import com.djawnstj.babyway.member.domain.Member

class FakeMemberRepository : MemberQueryRepository, MemberCommandRepository {

    private val members = mutableMapOf<Long, Member>()

    override fun existsByLoginIdAndNotDeleted(loginId: String?): Boolean = members.values.any { it.loginId == loginId && it.deletedAt == null }

    override fun findByIdAndNotDeleted(id: Long): Member? = members[id]

    override fun findByLoginIdAndNotDeleted(loginId: String?): Member? = members.values.firstOrNull { it.loginId == loginId && it.deletedAt == null }

    fun init() {
        members.clear()
    }

    override fun save(member: Member): Member {
        this.members[member.id] = member

        return member
    }
}
