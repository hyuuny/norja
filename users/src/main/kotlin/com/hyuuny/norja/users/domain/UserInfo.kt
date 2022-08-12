package com.hyuuny.norja.users.domain

import java.time.LocalDateTime

data class UserInfo(
    val id: Long,
    val username: String,
    val nickname: String,
    val status: Status,
    val phoneNumber: String,
    val agreedTermsOfService: Boolean = true,
    val agreedPrivacyPolicy: Boolean = true,
    val agreedReceiveMessage: Boolean = true,
    val createdAt: LocalDateTime? = null,
) {
    constructor(entity: User) : this(
        id = entity.id!!,
        username = entity.username,
        nickname = entity.nickname,
        phoneNumber = entity.phoneNumber,
        status = entity.status,
        agreedTermsOfService = entity.agreedTermsOfService,
        agreedPrivacyPolicy = entity.agreedPrivacyPolicy,
        agreedReceiveMessage = entity.agreedReceiveMessage,
        createdAt = entity.createdAt,
    )
}