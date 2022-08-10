package com.hyuuny.norja.users.domain

import java.time.LocalDateTime

data class UserInfo(
    val id: Long,
    val nickname: String,
    val phoneNumber: String,
    val agreedTermsOfService: Boolean = true,
    val agreedPrivacyPolicy: Boolean = true,
    val agreedReceiveMessage: Boolean = true,
    val createdAt: LocalDateTime? = null,
) {
    constructor(entity: User) : this(
        id = entity.id!!,
        nickname = entity.nickname,
        phoneNumber = entity.phoneNumber,
        agreedTermsOfService = entity.agreedTermsOfService,
        agreedPrivacyPolicy = entity.agreedPrivacyPolicy,
        agreedReceiveMessage = entity.agreedReceiveMessage,
        createdAt = entity.createdAt,
    )
}