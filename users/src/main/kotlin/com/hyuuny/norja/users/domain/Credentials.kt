package com.hyuuny.norja.users.domain

data class Credentials(
    val username: String,
    val password: String,
)

data class Token(
    val accessToke: String,
)

data class UserWithToken(
    val userInfo: UserInfo,
    val token: Token,
)
