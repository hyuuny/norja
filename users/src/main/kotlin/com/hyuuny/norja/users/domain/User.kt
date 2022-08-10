package com.hyuuny.norja.users.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Table(name = "users")
@Entity
class User private constructor(
    val username: String,
    password: String,
    status: Status = Status.ACTIVE,
    nickname: String,
    phoneNumber: String,
    auth: String = "ROLE_USER",
    agreedTermsOfService: Boolean = true,
    agreedPrivacyPolicy: Boolean = true,
    agreedReceiveMessage: Boolean = true,
) : BaseEntity() {

    companion object {
        fun create(
            username: String,
            password: String,
            nickname: String,
            phoneNumber: String,
            agreedTermsOfService: Boolean = true,
            agreedPrivacyPolicy: Boolean = true,
            agreedReceiveMessage: Boolean = true,
        ) =
            User(
                username = username,
                password = password,
                nickname = nickname,
                phoneNumber = phoneNumber,
                agreedTermsOfService = agreedTermsOfService,
                agreedPrivacyPolicy = agreedPrivacyPolicy,
                agreedReceiveMessage = agreedReceiveMessage,
            )
    }

    var password = password
        private set

    @Enumerated(EnumType.STRING)
    var status = status
        private set

    var nickname = nickname
        private set

    var phoneNumber = phoneNumber
        private set

    var auth = auth
        private set

    val authorities
        get() = if (this.auth.isNotEmpty()) this.auth.split(",").toString() else ""

    var agreedTermsOfService = agreedTermsOfService
        private set

    var agreedPrivacyPolicy = agreedPrivacyPolicy
        private set

    var agreedReceiveMessage = agreedReceiveMessage
        private set

    fun encode(passwordEncoder: PasswordEncoder) {
        this.password = passwordEncoder.encode(this.password)
    }

    fun changePassword(passwordEncoder: PasswordEncoder, password: String) {
        this.password = passwordEncoder.encode(password)
    }

    fun changeNickname(nickname: String) {
        this.nickname = nickname
    }

    fun changePhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun changeAgreedTermsOfService(agreedTermsOfService: Boolean) {
        this.agreedTermsOfService = agreedTermsOfService
    }

    fun changeAgreedPrivacyPolicy(agreedPrivacyPolicy: Boolean) {
        this.agreedPrivacyPolicy = agreedPrivacyPolicy
    }

    fun changeAgreedReceiveMessage(agreedReceiveMessage: Boolean) {
        this.agreedReceiveMessage = agreedReceiveMessage
    }

    fun leave() {
        this.status = Status.LEAVE
    }

}