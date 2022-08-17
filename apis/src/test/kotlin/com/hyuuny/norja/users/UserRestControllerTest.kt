package com.hyuuny.norja.users

import com.hyuuny.norja.FixtureUser
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.users.domain.Status.ACTIVE
import com.hyuuny.norja.users.interfaces.*
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

const val USER_REQUEST_URL = "/api/v1/users"

class UserRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @AfterEach
    fun afterEach() {
        deleteAllUsers()
    }


    @Test
    fun `회원가입`() {
        given()
            .contentType(ContentType.JSON)
            .body(
                SignUpDto(
                    username = "hello@knou.ac.kr",
                    password = "a123456A",
                    nickname = "김성현",
                    phoneNumber = "010-1234-4567",
                )
            )
            .`when`()
            .post("$USER_REQUEST_URL/sign-up")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `회원가입 - 중복계정예외`() {
        userService.singUp(
            SignUpDto(
                username = "hyuuny@knou.ac.kr",
                password = "a123456A",
                nickname = "김성현",
                phoneNumber = "010-1234-4567",
            ).toCommand()
        )

        given()
            .contentType(ContentType.JSON)
            .body(
                SignUpDto(
                    username = "hyuuny@knou.ac.kr",
                    password = "a123456A",
                    nickname = "김성현",
                    phoneNumber = "010-1234-4567",
                )
            )
            .`when`()
            .post("$USER_REQUEST_URL/sign-up")
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `회원 상세 조회`() {
        val command = FixtureUser.aUser()
        val savedUserId = userService.singUp(command)

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(command.username, command.password)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .get("$USER_REQUEST_URL/{id}", savedUserId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `비밀번호 변경`() {
        val command = FixtureUser.aUser()
        val savedUserId = userService.singUp(command)
        val dto = ChangePasswordDto(password = "c123456C")

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(command.username, command.password)
            )
            .contentType(ContentType.JSON)
            .body(dto)
            .post("$USER_REQUEST_URL/{id}/change-password", savedUserId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())

        given()
            .contentType(ContentType.JSON)
            .body(CredentialsDto("hyuuny@knou.ac.kr", "c123456C"))
            .`when`()
            .post("$AUTH_REQUEST_URL/auth")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `동의여부 변경`() {
        val command = FixtureUser.aUser()
        val savedUserId = userService.singUp(command)
        val dto = ChangeAgreedDto(
            agreedTermsOfService = false,
            agreedPrivacyPolicy = false,
            agreedReceiveMessage = false
        )

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(command.username, command.password)
            )
            .contentType(ContentType.JSON)
            .body(dto)
            .post("$USER_REQUEST_URL/{id}/change-agreed", savedUserId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `회원 정보 변경`() {
        val command = FixtureUser.aUser()
        val savedUserId = userService.singUp(command)
        val dto = UserUpdateDto(nickname = "현이킴", phoneNumber = "010-8936-7130")

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(command.username, command.password)
            )
            .contentType(ContentType.JSON)
            .body(dto)
            .put("$USER_REQUEST_URL/{id}", savedUserId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("username", equalTo(command.username))
            .assertThat().body("status", equalTo(ACTIVE.toString()))
            .assertThat().body("nickname", equalTo(dto.nickname))
            .assertThat().body("phoneNumber", equalTo(dto.phoneNumber))
            .assertThat().body("agreedTermsOfService", equalTo(true))
            .assertThat().body("agreedPrivacyPolicy", equalTo(true))
            .assertThat().body("agreedReceiveMessage", equalTo(true))
            .assertThat().body(CoreMatchers.containsString("createdAt"))
    }

    @Test
    fun `회원 탈퇴`() {
        val command = FixtureUser.aUser()
        val savedUserId = userService.singUp(command)

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(command.username, command.password)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .delete("$USER_REQUEST_URL/{id}", savedUserId)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }
}