package com.hyuuny.norja.users.domain

data class CredentialsCommand(
    val username: String,
    val password: String,
)

data class Token(
    val accessToken: String,
)
