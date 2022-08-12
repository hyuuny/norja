package com.hyuuny.norja.users.interfaces

import com.hyuuny.norja.users.domain.CredentialsCommand

data class CredentialsDto(
    val username: String,
    val password: String,
) {
    fun toCommand() = CredentialsCommand(username = this.username, password = this.password)
}
