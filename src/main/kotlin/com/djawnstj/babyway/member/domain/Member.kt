package com.djawnstj.babyway.member.domain

import com.djawnstj.babyway.global.common.BaseEntity
import com.djawnstj.babyway.global.exception.ApplicationException
import com.djawnstj.babyway.global.exception.ErrorCode
import jakarta.persistence.*

@Entity
@Table(
    name = "member", uniqueConstraints = [UniqueConstraint(name = "uk_member_login_id", columnNames = ["login_id"])]
)
class Member(
    loginId: String?, password: Password, nickname: String?, memberType: MemberType?
) : BaseEntity() {
    @field:Column(name = "login_id", nullable = false, unique = true)
    var loginId: String = validateLoginId(loginId)
        protected set

    @field:Embedded
    var password: Password = password

    @field:Column(name = "nickname", nullable = false, length = 45)
    var nickname: String = validateNickname(nickname)
        protected set

    @field:Column(name = "account_type", nullable = false)
    @field:Enumerated(EnumType.STRING)
    var memberType: MemberType = validateMemberType(memberType)
        protected set

    private fun validateLoginId(loginId: String?): String {
        require(!loginId.isNullOrBlank()) {
            throw ApplicationException(ErrorCode.BLANK_LOGIN_ID)
        }

        require(loginId.matches(LOGIN_ID_REGEX)) {
            throw ApplicationException(ErrorCode.INVALID_LOGIN_ID_FORMAT)
        }

        return loginId
    }

    private fun validateNickname(nickname: String?): String {
        require(!nickname.isNullOrBlank()) {
            throw ApplicationException(ErrorCode.BLANK_NICKNAME)
        }

        require(nickname.matches(NICKNAME_REGEX)) {
            throw ApplicationException(ErrorCode.INVALID_NICKNAME_FORMAT)
        }

        return nickname
    }

    private fun validateMemberType(memberType: MemberType?): MemberType {
        require(memberType != null) {
            throw ApplicationException(ErrorCode.BLANK_MEMBER_TYPE)
        }

        return memberType
    }

    companion object {
        private val LOGIN_ID_REGEX = "^(?=.*[A-Za-z])[A-Za-z0-9!@#$%^&*()_+.,;:<>?-]{8,15}$".toRegex()
        private val NICKNAME_REGEX = "^[A-Za-z0-9가-힣]{8,15}$".toRegex()
    }
}
