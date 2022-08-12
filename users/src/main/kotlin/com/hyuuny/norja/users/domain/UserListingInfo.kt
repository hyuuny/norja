package com.hyuuny.norja.users.domain

import java.time.LocalDateTime

data class UserListingInfo(
    val id: Long,
    val username: String,
    val nickname: String,
    val status: Status,
    val phoneNumber: String,
    val createdAt: LocalDateTime,
) {
    constructor(searched: SearchedUser): this(
        id = searched.id!!,
        username =  searched.username!!,
        nickname = searched.nickname!!,
        status = searched.status!!,
        phoneNumber = searched.phoneNumber!!,
        createdAt = searched.createdAt!!
    )
}