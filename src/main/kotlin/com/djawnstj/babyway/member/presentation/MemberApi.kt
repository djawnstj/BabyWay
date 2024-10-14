package com.djawnstj.babyway.member.presentation

import com.djawnstj.babyway.global.common.SuccessResponse
import com.djawnstj.babyway.member.application.MemberCommandService
import com.djawnstj.babyway.member.application.MemberQueryService
import com.djawnstj.babyway.member.presentation.dto.request.MemberRegisterRequest
import com.djawnstj.babyway.member.presentation.dto.response.MemberRegisterResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberApi(
    private val memberCommandService: MemberCommandService,
    private val memberQueryService: MemberQueryService
) {

    @PostMapping("/api/members")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody request: MemberRegisterRequest): SuccessResponse<MemberRegisterResponse> {
        val id = memberCommandService.createMember(request.toCommand())
        return SuccessResponse(MemberRegisterResponse(memberQueryService.findById(id)), HttpStatus.CREATED)
    }
}
