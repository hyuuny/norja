package com.hyuuny.norja.users.domain

import com.hyuuny.norja.users.domain.FixtureUser.Companion.aUser
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    fun `회원가입`() {
        val expectedUsername = "hyuuny123@knou.ac.kr"
        val expectedStatus = Status.ACTIVE
        val expectedNickname = "김성현"
        val expectedPhoneNumber = "010-1234-1234"
        val expectedAuth = "ROLE_USER"
        val expectedAgreedTermsOfService = true
        val expectedPrivacyPolicy = true
        val expectedReceiveMessage = true

        val newUser = aUser(username = expectedUsername)
        newUser.username shouldBe expectedUsername
        newUser.status shouldBe expectedStatus
        newUser.nickname shouldBe expectedNickname
        newUser.nickname shouldBe expectedNickname
        newUser.phoneNumber shouldBe expectedPhoneNumber
        newUser.auth shouldBe expectedAuth
        newUser.agreedTermsOfService shouldBe expectedAgreedTermsOfService
        newUser.agreedPrivacyPolicy shouldBe expectedPrivacyPolicy
        newUser.agreedReceiveMessage shouldBe expectedReceiveMessage
    }

    @Test
    fun `닉네임 변경`() {
        val expectedNickname = "개울가"
        val newUser = aUser()
        newUser.changeNickname(expectedNickname)
        newUser.nickname shouldBe expectedNickname
    }

    @Test
    fun `연락처 변경`() {
        val expectedPhoneNumber = "010-1234-4567"
        val newUser = aUser()
        newUser.changePhoneNumber(expectedPhoneNumber)
        newUser.phoneNumber shouldBe expectedPhoneNumber
    }

    @Test
    fun `서비스 약관 거부`() {
        val expectedAgreedTermsOfService = false
        val newUser = aUser(agreedTermsOfService = true)
        newUser.changeAgreedTermsOfService(expectedAgreedTermsOfService)
        newUser.agreedTermsOfService shouldBe expectedAgreedTermsOfService
    }

    @Test
    fun `개인정보 보호정책 거부`() {
        val expectedAgreedPrivacyPolicy = false
        val newUser = aUser(agreedPrivacyPolicy = true)
        newUser.changeAgreedPrivacyPolicy(expectedAgreedPrivacyPolicy)
        newUser.agreedPrivacyPolicy shouldBe expectedAgreedPrivacyPolicy
    }

    @Test
    fun `메시지 수신 거부`() {
        val expectedAgreedReceiveMessage = false
        val newUser = aUser(agreedPrivacyPolicy = true)
        newUser.changeAgreedReceiveMessage(expectedAgreedReceiveMessage)
        newUser.agreedReceiveMessage shouldBe expectedAgreedReceiveMessage
    }

    @Test
    fun `회원탈퇴`() {
        val newUser = aUser()
        newUser.leave()
        newUser.status shouldBe Status.LEAVE
    }

}

class FixtureUser {
    companion object {
        fun aUser(
            username: String = "hyuuny@knou.ac.kr",
            password: String = "secret",
            nickname: String = "김성현",
            phoneNumber: String = "010-1234-1234",
            agreedTermsOfService: Boolean = true,
            agreedPrivacyPolicy: Boolean = true,
            agreedReceiveMessage: Boolean = true,
        ) = User.create(
            username,
            password,
            nickname,
            phoneNumber,
            agreedTermsOfService,
            agreedPrivacyPolicy,
            agreedReceiveMessage
        )
    }
}