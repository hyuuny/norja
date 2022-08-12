package com.hyuuny.norja.users

import com.hyuuny.norja.ADMIN_EMAIL
import com.hyuuny.norja.ADMIN_PASSWORD
import com.hyuuny.norja.FixtureUser.Companion.aUser
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.users.domain.Status
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.util.stream.IntStream

const val USER_REQUEST_URL = "/api/v1/users"

class UserAdminRestControllerTest : BaseIntegrationTest() {

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
    fun `회원 조회 및 검색`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                userService.singUp(aUser(username = "hyuuny${value}@knou.ac.kr"))
            }
        }

        RestAssured.given()
            .contentType(ContentType.JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .`when`()
            .log().all()
            .get(USER_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(13)) // TestRunner 2명 포함
            .assertThat().body("page.totalPages", equalTo(2))
            .assertThat().body("page.number", equalTo(0))
    }

    @Test
    fun `회원 조회 및 검색 - 이메일 검색`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                userService.singUp(aUser(username = "hyuuny${value}@knou.ac.kr"))
            }
        }

        RestAssured.given()
            .contentType(ContentType.JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .queryParam("username", 1)
            .`when`()
            .log().all()
            .get(USER_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(2)) // TestRunner 2명 포함
            .assertThat().body("page.totalPages", equalTo(1))
            .assertThat().body("page.number", equalTo(0))
    }

    @Test
    fun `회원 조회 및 검색 - ID 검색`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                userService.singUp(aUser(username = "hyuuny${value}@knou.ac.kr"))
            }
        }

        RestAssured.given()
            .contentType(ContentType.JSON)
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .queryParam("id", 1)
            .`when`()
            .log().all()
            .get(USER_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(1)) // TestRunner 2명 포함
            .assertThat().body("page.totalPages", equalTo(1))
            .assertThat().body("page.number", equalTo(0))
    }

    @Test
    fun `회원 상세 조회`() {
        val command = aUser()
        val savedUserId = userService.singUp(command)

        RestAssured.given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .get("$USER_REQUEST_URL/{id}", savedUserId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("username", equalTo(command.username))
            .assertThat().body("status", equalTo(Status.ACTIVE.toString()))
            .assertThat().body("nickname", equalTo(command.nickname))
            .assertThat().body("phoneNumber", equalTo(command.phoneNumber))
            .assertThat().body("agreedTermsOfService", equalTo(true))
            .assertThat().body("agreedPrivacyPolicy", equalTo(true))
            .assertThat().body("agreedReceiveMessage", equalTo(true))
            .assertThat().body(CoreMatchers.containsString("createdAt"))
    }


}