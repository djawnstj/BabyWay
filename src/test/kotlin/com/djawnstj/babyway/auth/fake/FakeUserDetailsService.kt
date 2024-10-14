package com.djawnstj.babyway.auth.fake

import com.djawnstj.babyway.auth.application.UserDetailsService
import com.djawnstj.babyway.auth.presentation.UserDetails
import com.djawnstj.babyway.member.application.MemberQueryRepository

class FakeUserDetailsService(
    private val memberQueryRepository : MemberQueryRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails? {
        memberQueryRepository.findByLoginIdAndNotDeleted(username)?.let {
            return UserDetails(it.id, it.loginId)
        }

        return null
    }
}
