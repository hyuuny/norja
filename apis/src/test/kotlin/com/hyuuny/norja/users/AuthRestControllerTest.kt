package com.hyuuny.norja.users

import com.hyuuny.norja.FixtureUser
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.users.interfaces.CredentialsDto
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus

const val AUTH_REQUEST_URL = "/api/v1"

class AuthRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        userService.singUp(FixtureUser.aUser())
    }

    @AfterEach
    fun afterEach() {
        deleteAllUsers()
    }

    @Test
    fun `로그인 성공`() {
        val dto = CredentialsDto("hyuuny@knou.ac.kr", "a123456A")

        given()
            .contentType(ContentType.JSON)
            .body(dto)
            .`when`()
            .post("$AUTH_REQUEST_URL/auth")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }


}