package com.djawnstj.babyway.global.util

import org.springframework.util.AntPathMatcher

private val matcher = AntPathMatcher()

fun matchUrl(pattern: String, url: String): Boolean = matcher.match(pattern, url)
