package com.hyuuny.norja.users.interfaces

import com.fasterxml.jackson.annotation.JsonInclude
import com.hyuuny.norja.users.domain.Status
import com.hyuuny.norja.users.domain.UserInfo
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponse(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "이메일", example = "hyuuny@knou.ac.kr", required = true)
    val username: String,

    @field:Schema(description = "상태", example = "ACTIVE", required = true)
    val status: Status,

    @field:Schema(description = "닉네임", example = "김성현", required = true)
    val nickname: String,

    @field:Schema(description = "전화번호", example = "010-1234-5678", required = true)
    val phoneNumber: String,

    @field:Schema(description = "서비스 약관 동의 여부", example = "true")
    val agreedTermsOfService: Boolean = true,

    @field:Schema(description = "개인 정보 보호 동의 여부", example = "true")
    val agreedPrivacyPolicy: Boolean = true,

    @field:Schema(description = "메시지 수신 동의 여부", example = "true")
    val agreedReceiveMessage: Boolean = true,

    @field:Schema(description = "등록일", example = "2022-08-11T21:51:00.797659")
    val createdAt: LocalDateTime? = null,
) {
    constructor(info: UserInfo) : this(
        id = info.id,
        username = info.username,
        status = info.status,
        nickname = info.nickname,
        phoneNumber = info.phoneNumber,
        agreedTermsOfService = info.agreedTermsOfService,
        agreedPrivacyPolicy = info.agreedPrivacyPolicy,
        agreedReceiveMessage = info.agreedReceiveMessage,
        createdAt = info.createdAt,
    )
}