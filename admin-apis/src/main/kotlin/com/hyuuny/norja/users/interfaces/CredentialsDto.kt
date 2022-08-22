package com.hyuuny.norja.users.interfaces

import com.hyuuny.norja.users.domain.CredentialsCommand
import io.swagger.v3.oas.annotations.media.Schema

data class CredentialsDto(

    @field:Schema(description = "계졍 아이디", example = "hyuuny@knou.ac.kr", required = true)
    val username: String,

    @field:Schema(description = "비밀번호", example = "secret", required = true)
    val password: String,
) {
    fun toCommand() = CredentialsCommand(username = this.username, password = this.password)
}
