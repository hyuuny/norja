package com.hyuuny.norja.users.domain

data class UserSearchQuery(
    val id: Long? = null,
    val username: String? = null,
    val status: Status? = null,
)
