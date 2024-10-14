package com.djawnstj.babyway.member.infra

import com.djawnstj.babyway.member.application.MemberCommandRepository
import com.djawnstj.babyway.member.domain.Member
import org.springframework.stereotype.Repository

@Repository
class MemberCommandJdslRepository(
    private val memberJpaRepository : MemberJpaRepository,
) : MemberCommandRepository {
    override fun save(member: Member): Member = memberJpaRepository.save(member)
}
