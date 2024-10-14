package com.djawnstj.babyway.auth.application

import com.djawnstj.babyway.auth.presentation.UserDetails

interface UserDetailsService {
    fun loadUserByUsername(username: String): UserDetails?
}
