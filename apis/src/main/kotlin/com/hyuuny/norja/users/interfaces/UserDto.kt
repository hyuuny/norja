package com.hyuuny.norja.users.interfaces

import com.hyuuny.norja.users.domain.ChangeAgreedCommand
import com.hyuuny.norja.users.domain.ChangePasswordCommand
import com.hyuuny.norja.users.domain.SignUpCommand
import com.hyuuny.norja.users.domain.UserUpdateCommand
import io.swagger.v3.oas.annotations.media.Schema

class UserDto

data class SignUpDto(

    @field:Schema(description = "이메일", example = "hyuuny@knou.ac.kr", required = true)
    val username: String,

    @field:Schema(description = "비밀번호", example = "a123456A", required = true)
    val password: String,

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
) {
    fun toCommand() = SignUpCommand(
        username = this.username,
        password = this.password,
        nickname = this.nickname,
        phoneNumber = this.phoneNumber,
        agreedTermsOfService = this.agreedTermsOfService,
        agreedPrivacyPolicy = this.agreedPrivacyPolicy,
        agreedReceiveMessage = this.agreedReceiveMessage
    )
}

data class ChangePasswordDto(

    @field:Schema(description = "비밀번호", example = "a123456A", required = true)
    val password: String,

    ) {
    fun toCommand() = ChangePasswordCommand(password = this.password)
}

data class ChangeAgreedDto(

    @field:Schema(description = "서비스 약관 동의 여부", example = "true")
    val agreedTermsOfService: Boolean = true,

    @field:Schema(description = "개인 정보 보호 동의 여부", example = "true")
    val agreedPrivacyPolicy: Boolean = true,

    @field:Schema(description = "메시지 수신 동의 여부", example = "true")
    val agreedReceiveMessage: Boolean = true,
) {
    fun toCommand() = ChangeAgreedCommand(
        agreedTermsOfService = this.agreedTermsOfService,
        agreedPrivacyPolicy = this.agreedPrivacyPolicy,
        agreedReceiveMessage = this.agreedReceiveMessage,
    )
}

data class UserUpdateDto(

    @field:Schema(description = "닉네임", example = "김성현", required = true)
    val nickname: String,

    @field:Schema(description = "전화번호", example = "010-1234-5678", required = true)
    val phoneNumber: String,
) {
    fun toCommand() = UserUpdateCommand(
        nickname = this.nickname,
        phoneNumber = this.phoneNumber
    )
}