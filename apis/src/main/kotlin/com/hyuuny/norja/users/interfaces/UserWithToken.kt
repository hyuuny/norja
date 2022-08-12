package com.hyuuny.norja.users.interfaces

import com.hyuuny.norja.users.domain.Token
import com.hyuuny.norja.users.infrastructure.UserAdapter

data class UserWithToken(
    val userInfo: UserAdapter,
    val token: Token,
)
