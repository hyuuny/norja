package com.hyuuny.norja.reservations

import com.hyuuny.norja.ADMIN_EMAIL
import com.hyuuny.norja.ADMIN_PASSWORD
import com.hyuuny.norja.FixtureRoom
import com.hyuuny.norja.FixtureUser.Companion.aUser
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.reservations.application.ReservationService
import com.hyuuny.norja.reservations.domain.ReservationCreateCommand
import com.hyuuny.norja.reservations.domain.Status
import com.hyuuny.norja.reservations.infrastructure.ReservationRepository
import com.hyuuny.norja.reservations.interfaces.ReservationCreateDto
import com.hyuuny.norja.rooms.application.RoomService
import com.hyuuny.norja.rooms.domain.RoomCreateCommand
import com.hyuuny.norja.rooms.domain.RoomFacilitiesCreateCommand
import com.hyuuny.norja.rooms.domain.RoomImageCreateCommand
import com.hyuuny.norja.rooms.domain.Type
import com.hyuuny.norja.rooms.infrastructure.RoomRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.stream.LongStream

const val RESERVATION_REQUEST_URL = "/api/v1/reservations"

class ReservationRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        savedRoomId = roomService.createRoom(FixtureRoom.aRoom(1))

        val command = aUser()
        savedUserId = userService.singUp(command)
    }

    @AfterEach
    fun afterEach() {
        deleteAllUsers()
        reservationRepository.deleteAll()
        roomRepository.deleteAll()
    }

    @Autowired
    lateinit var roomRepository: RoomRepository

    @Autowired
    lateinit var roomService: RoomService

    @Autowired
    lateinit var reservationRepository: ReservationRepository

    @Autowired
    lateinit var reservationService: ReservationService

    var savedRoomId: Long = 0

    var savedUserId: Long = 0

    val username = aUser().username

    val password = aUser().password

    @Test
    fun `예약 검색 및 조회`() {
        val roomDto = RoomCreateCommand(
            lodgingCompanyId = 1,
            type = Type.FAMILY_ROOM,
            name = "일반실",
            roomCount = 25,
            standardPersonnel = 2,
            maximumPersonnel = 2,
            price = 130_000,
            content = "코로나 19로 인한 조식 중단",
            images = mutableListOf(
                RoomImageCreateCommand(100L, "image1-url"),
                RoomImageCreateCommand(300L, "image3-url"),
                RoomImageCreateCommand(200L, "image2-url"),
            ),
            facilities = mutableListOf(
                RoomFacilitiesCreateCommand("2인", "icon1-url", 100L),
                RoomFacilitiesCreateCommand("퀸 침대", "icon2-url", 300L),
                RoomFacilitiesCreateCommand("정수기 비치", "icon3-url", 200L),
            )
        )
        val savedRoomId = roomService.createRoom(roomDto)
        LongStream.range(1, 12).forEach { i ->
            run {
                if (i.toInt() % 2 == 0) {
                    val reservationCommand = ReservationCreateCommand(
                        userId = 3,
                        roomId = savedRoomId,
                        roomCount = roomDto.roomCount,
                        price = roomDto.price,
                        checkIn = LocalDate.now().plusDays(i),
                        checkOut = LocalDate.now().plusDays(i + 5)
                    )
                    reservationService.createReservation(reservationCommand)
                } else {
                    val reservationCommand = ReservationCreateCommand(
                        userId = 5,
                        roomId = savedRoomId,
                        roomCount = roomDto.roomCount,
                        price = roomDto.price,
                        checkIn = LocalDate.now().plusDays(i),
                        checkOut = LocalDate.now().plusDays(i + 5)
                    )
                    reservationService.createReservation(reservationCommand)
                }
            }
        }

        RestAssured.given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .queryParam("userId", 5)
            .`when`()
            .log().all()
            .get(RESERVATION_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(6))
            .assertThat().body("page.totalPages", equalTo(1))
            .assertThat().body("page.number", equalTo(0))
            .assertThat()
            .body("_embedded.reservations[0].roomId", equalTo(savedRoomId.toInt()))
            .body("_embedded.reservations[0].roomCount", equalTo(roomDto.roomCount))
            .body("_embedded.reservations[0].status", equalTo(Status.COMPLETION.toString()))
            .body("_embedded.reservations[0].price", equalTo(roomDto.price.toInt()))
            .body("_embedded.reservations[0].userId", equalTo(5))
    }

    @Test
    fun `객실 예약`() {
        val dto = ReservationCreateDto(
            userId = savedUserId,
            roomId = savedRoomId,
            roomCount = 25,
            price = 130_000,
            checkIn = LocalDate.now(),
            checkOut = LocalDate.now().plusDays(5)
        )

        RestAssured.given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(username, password)
            )
            .contentType(ContentType.JSON)
            .body(dto)
            .`when`()
            .post(RESERVATION_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `객실 예약 - 마감 예외`() {
        val roomCommand = RoomCreateCommand(
            lodgingCompanyId = 1,
            type = Type.SINGLE_ROOM,
            name = "일반실",
            roomCount = 5,
            standardPersonnel = 2,
            maximumPersonnel = 2,
            price = 130_000,
            content = "코로나 19로 인한 조식 중단",
            images = mutableListOf(
                RoomImageCreateCommand(100L, "image1-url"),
                RoomImageCreateCommand(300L, "image3-url"),
                RoomImageCreateCommand(200L, "image2-url"),
            ),
            facilities = mutableListOf(
                RoomFacilitiesCreateCommand("2인", "icon1-url", 100L),
                RoomFacilitiesCreateCommand("퀸 침대", "icon2-url", 300L),
                RoomFacilitiesCreateCommand("정수기 비치", "icon3-url", 200L),
            )
        )
        savedRoomId = roomService.createRoom(roomCommand)

        LongStream.range(0, 5).forEach { i ->
            run {
                reservationService.createReservation(
                    ReservationCreateCommand(
                        userId = i,
                        roomId = savedRoomId,
                        roomCount = roomCommand.roomCount,
                        price = roomCommand.price,
                        checkIn = LocalDate.now(),
                        checkOut = LocalDate.now().plusDays(5)
                    )
                )
            }
        }

        RestAssured.given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(username, password)
            )
            .contentType(ContentType.JSON)
            .body(
                ReservationCreateCommand(
                    userId = savedUserId,
                    roomId = savedRoomId,
                    roomCount = roomCommand.roomCount,
                    price = roomCommand.price,
                    checkIn = LocalDate.now().plusDays(2),
                    checkOut = LocalDate.now().plusDays(5)
                )
            )
            .`when`()
            .post(RESERVATION_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `예약 상세 조회`() {
        val command = ReservationCreateCommand(
            userId = savedUserId,
            roomId = savedRoomId,
            roomCount = 25,
            price = 130_000,
            checkIn = LocalDate.now(),
            checkOut = LocalDate.now().plusDays(5)
        )
        val savedReservationId = reservationService.createReservation(command)

        RestAssured.given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(username, password)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .get("$RESERVATION_REQUEST_URL/{id}", savedReservationId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("userId", equalTo(command.userId.toInt()))
            .assertThat().body("roomId", equalTo(command.roomId.toInt()))
            .assertThat().body("status", equalTo(Status.COMPLETION.toString()))
            .assertThat().body("checkIn", equalTo(command.checkIn.toString()))
            .assertThat().body("checkOut", equalTo(command.checkOut.toString()))
            .assertThat().body(CoreMatchers.containsString("createdAt"))
    }

    @Test
    fun `예약 상세 조회- 잘못된 아이디 예외`() {
        RestAssured.given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(username, password)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$RESERVATION_REQUEST_URL/{id}", 999999)
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `예약 취소`() {
        val command = ReservationCreateCommand(
            userId = savedUserId,
            roomId = savedRoomId,
            roomCount = 25,
            price = 130_000,
            checkIn = LocalDate.now(),
            checkOut = LocalDate.now().plusDays(5)
        )
        val savedReservationId = reservationService.createReservation(command)

        RestAssured.given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(username, password)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .delete("$RESERVATION_REQUEST_URL/cancellation/{id}", savedReservationId)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `예약 취소 - 잘못된 아이디 예외`() {
        RestAssured.given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(username, password)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .delete("$RESERVATION_REQUEST_URL/cancellation/{id}", 999999)
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

}