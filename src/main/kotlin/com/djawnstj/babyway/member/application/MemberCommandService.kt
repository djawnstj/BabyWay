package com.djawnstj.babyway.member.application

import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.global.exception.ErrorCode
import com.djawnstj.babyway.member.application.command.MemberCreateCommand
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberCommandService(
    private val memberCommandRepository: MemberCommandRepository,
    private val memberQueryRepository: MemberQueryRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun createMember(memberCreateCommand: MemberCreateCommand): Long {
        if (isAlreadyExistLoginId(memberCreateCommand.loginId)) {
            throw ApplicationException(ErrorCode.DUPLICATED_REGISTER_LOGIN_ID)
        }

        val member = memberCreateCommand.toEntity(passwordEncoder)

        return memberCommandRepository.save(member).id
    }

    private fun isAlreadyExistLoginId(loginId: String?): Boolean = memberQueryRepository.existsByLoginIdAndNotDeleted(loginId)
}
