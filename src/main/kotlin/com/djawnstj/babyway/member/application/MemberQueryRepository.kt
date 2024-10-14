package com.djawnstj.babyway.member.application

import com.djawnstj.babyway.member.domain.Member

interface MemberQueryRepository {
    fun existsByLoginIdAndNotDeleted(loginId: String?): Boolean
    fun findByIdAndNotDeleted(id: Long): Member?
    fun findByLoginIdAndNotDeleted(loginId: String?): Member?
}
