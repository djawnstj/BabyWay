package com.djawnstj.babyway.member.application

import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.global.exception.ErrorCode
import com.djawnstj.babyway.member.domain.Member
import org.springframework.stereotype.Service

@Service
class MemberQueryService(
    private val memberQueryRepository : MemberQueryRepository
) {
    fun findById(id: Long): Member =
        memberQueryRepository.findByIdAndNotDeleted(id) ?: throw ApplicationException(ErrorCode.MEMBER_NOT_FOUND)
}
