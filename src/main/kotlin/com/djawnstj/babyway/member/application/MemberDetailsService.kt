package com.djawnstj.babyway.member.application

import com.djawnstj.babyway.auth.application.UserDetailsService
import com.djawnstj.babyway.auth.presentation.UserDetails
import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.global.exception.ErrorCode
import org.springframework.stereotype.Service

@Service
class MemberDetailsService(
    private val memberQueryRepository: MemberQueryRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        memberQueryRepository.findByLoginIdAndNotDeleted(username)?.let {
            return UserDetails(it.id, it.loginId)
        }

        throw ApplicationException(ErrorCode.MEMBER_NOT_FOUND)
    }
}
