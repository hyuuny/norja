package com.hyuuny.norja.rooms

import com.hyuuny.norja.FixtureRoom
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.rooms.application.RoomService
import com.hyuuny.norja.rooms.domain.Type
import com.hyuuny.norja.rooms.infrastructure.RoomRepository
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus

const val ROOM_REQUEST_URL = "/api/v1/rooms"

class RoomRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @AfterEach
    fun afterEach() {
        roomRepository.deleteAll()
    }

    @Autowired
    lateinit var roomRepository: RoomRepository

    @Autowired
    lateinit var roomService: RoomService

    @Test
    fun `객실 상세 조회`() {
        val command = FixtureRoom.aRoom(1)
        val savedRoomId = roomService.createRoom(command)

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("$ROOM_REQUEST_URL/{id}", savedRoomId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("name", IsEqual.equalTo(command.name))
            .assertThat().body("lodgingCompanyId", IsEqual.equalTo(1))
            .assertThat().body("type", IsEqual.equalTo(Type.DOUBLE_ROOM.toString()))
            .assertThat().body("name", IsEqual.equalTo(command.name))
            .assertThat().body("roomCount", IsEqual.equalTo(command.roomCount))
            .assertThat().body("standardPersonnel", IsEqual.equalTo(command.standardPersonnel))
            .assertThat().body("maximumPersonnel", IsEqual.equalTo(command.maximumPersonnel))
            .assertThat().body("price", IsEqual.equalTo(command.price.toInt()))
            .assertThat().body("content", IsEqual.equalTo(command.content))
            .assertThat().body("roomImages[0].imageUrl", IsEqual.equalTo(command.images[0].imageUrl))
            .assertThat().body("roomImages[1].imageUrl", IsEqual.equalTo(command.images[2].imageUrl))
            .assertThat().body("roomImages[2].imageUrl", IsEqual.equalTo(command.images[1].imageUrl))
            .assertThat().body("roomFacilities[0].name", IsEqual.equalTo(command.facilities[0].name))
            .assertThat().body("roomFacilities[1].name", IsEqual.equalTo(command.facilities[2].name))
            .assertThat().body("roomFacilities[2].name", IsEqual.equalTo(command.facilities[1].name))
    }

    @Test
    fun `객실 상세 조회 - 잘못된 아이디 예외`() {
        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$ROOM_REQUEST_URL/{id}", 999999)
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }


}