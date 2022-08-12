package com.hyuuny.norja.users

import com.hyuuny.norja.ADMIN_EMAIL
import com.hyuuny.norja.ADMIN_PASSWORD
import com.hyuuny.norja.FixtureUser.Companion.aUser
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.users.interfaces.CredentialsDto
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus

const val AUTH_REQUEST_URL = "/api/v1"

class AuthAdminRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        userService.singUp(aUser())
    }

    @AfterEach
    fun afterEach() {
        userRepository.deleteAll()
    }


    @Test
    fun `로그인 성공`() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(CredentialsDto(ADMIN_EMAIL, ADMIN_PASSWORD))
            .`when`()
            .post("$AUTH_REQUEST_URL/auth")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

}