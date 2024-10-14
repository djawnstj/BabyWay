package com.djawnstj.babyway.member.fake

import org.springframework.security.crypto.password.PasswordEncoder

class FakePasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: CharSequence?): String = rawPassword.toString()

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean =
        rawPassword.toString() == encodedPassword.toString()

    companion object {
        val INSTANCE = FakePasswordEncoder()
    }
}
