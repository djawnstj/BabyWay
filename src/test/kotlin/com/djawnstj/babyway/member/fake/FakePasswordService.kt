package com.djawnstj.babyway.member.fake

import com.djawnstj.babyway.member.application.PasswordService

class FakePasswordService : PasswordService(FakePasswordEncoder.INSTANCE)
