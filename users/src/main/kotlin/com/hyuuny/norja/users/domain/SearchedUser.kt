package com.hyuuny.norja.users.domain

import java.time.LocalDateTime

data class SearchedUser(
    val id: Long? = null,
    val username: String? = null,
    val nickname: String? = null,
    val status: Status? = null,
    val phoneNumber: String? = null,
    val createdAt: LocalDateTime? = null,
)
