package com.djawnstj.babyway.auth.fake

import com.djawnstj.babyway.auth.application.JwtProperties

enum class FakeJwtProperties(val properties: JwtProperties) {
    `만료 시간 5초`(JwtProperties("1", 5000L, 5000L))
}
