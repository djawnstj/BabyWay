package com.djawnstj.babyway.global.common

import org.springframework.http.HttpStatus
import com.djawnstj.babyway.global.common.Response.Meta
import com.djawnstj.babyway.global.exception.ErrorCode

typealias SuccessResponse<T> = Response<T>
typealias ErrorResponse = Response<*>

data class Response<T>(
    val meta: Meta,
    val data: T?,
) {
    data class Meta(
        val code: Int,
        val message: String,
    )
}

fun <T> SuccessResponse(
    data: T,
    status: HttpStatus = HttpStatus.OK,
): SuccessResponse<T> =
    Response(
        meta = Meta(
            code = status.value(),
            message = status.reasonPhrase
        ), data = data
    )

fun ErrorResponse(
    errorCode: ErrorCode,
    message: String? = null,
): ErrorResponse =
    Response(
        meta = Meta(
            code = errorCode.status.value(),
            message = if (message.isNullOrBlank()) errorCode.message else message
        ), data = null
    )
