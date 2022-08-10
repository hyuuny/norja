package com.hyuuny.norja.users.domain

class UserCommand

data class SignUpCommand(
    val username: String,
    val password: String,
    val nickname: String,
    val phoneNumber: String,
    val agreedTermsOfService: Boolean = true,
    val agreedPrivacyPolicy: Boolean = true,
    val agreedReceiveMessage: Boolean = true,
) {
    fun toEntity() = User.create(
        username = this.username,
        password = this.password,
        nickname = this.nickname,
        phoneNumber = this.phoneNumber,
        agreedTermsOfService = this.agreedTermsOfService,
        agreedPrivacyPolicy = this.agreedPrivacyPolicy,
        agreedReceiveMessage = this.agreedReceiveMessage,
    )
}

data class ChangePasswordCommand(
    val password: String,
)

data class ChangeAgreedCommand(
    val agreedTermsOfService: Boolean = true,
    val agreedPrivacyPolicy: Boolean = true,
    val agreedReceiveMessage: Boolean = true,
) {
    fun changeAgreed(entity: User) {
        entity.changeAgreedTermsOfService(this.agreedTermsOfService)
        entity.changeAgreedPrivacyPolicy(this.agreedPrivacyPolicy)
        entity.changeAgreedReceiveMessage(this.agreedReceiveMessage)
    }
}

data class UserUpdateCommand(
    val nickname: String,
    val phoneNumber: String,
) {
    fun update(entity: User) {
        entity.changeNickname(this.nickname)
        entity.changePhoneNumber(this.phoneNumber)
    }
}


