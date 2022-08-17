package com.hyuuny.norja.users.domain

import com.hyuuny.norja.users.infrastructure.UserAdapter

data class UserWithToken(
    val userInfo: UserAdapter,
    val token: Token,
)
