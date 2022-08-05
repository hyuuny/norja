package com.hyuuny.norja.reservations

import com.hyuuny.norja.FixtureReservation.Companion.aReservation
import com.hyuuny.norja.FixtureRoom
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.reservations.application.ReservationService
import com.hyuuny.norja.reservations.domain.ReservationCreateCommand
import com.hyuuny.norja.reservations.domain.Status
import com.hyuuny.norja.reservations.infrastructure.ReservationRepository
import com.hyuuny.norja.rooms.application.RoomService
import com.hyuuny.norja.rooms.domain.Type
import com.hyuuny.norja.rooms.infrastructure.RoomRepository
import com.hyuuny.norja.rooms.interfaces.RoomCreateDto
import com.hyuuny.norja.rooms.interfaces.RoomFacilitiesCreateDto
import com.hyuuny.norja.rooms.interfaces.RoomImageCreateDto
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.stream.LongStream

const val RESERVATION_REQUEST_URL = "/api/v1/reservations"

class ReservationAdminRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        savedRoomId = roomService.createRoom(FixtureRoom.aRoom(1).toCommand())
    }

    @AfterEach
    fun afterEach() {
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


    @Test
    fun `예약 검색 및 조회`() {
        val roomDto = RoomCreateDto(
            lodgingCompanyId = 1,
            type = Type.FAMILY_ROOM,
            name = "일반실",
            roomCount = 25,
            standardPersonnel = 2,
            maximumPersonnel = 2,
            price = 130_000,
            content = "코로나 19로 인한 조식 중단",
            images = mutableListOf(
                RoomImageCreateDto(100L, "image1-url"),
                RoomImageCreateDto(300L, "image3-url"),
                RoomImageCreateDto(200L, "image2-url"),
            ),
            facilities = mutableListOf(
                RoomFacilitiesCreateDto("2인", "icon1-url", 100L),
                RoomFacilitiesCreateDto("퀸 침대", "icon2-url", 300L),
                RoomFacilitiesCreateDto("정수기 비치", "icon3-url", 200L),
            )
        )
        val savedRoomId = roomService.createRoom(roomDto.toCommand())
        LongStream.range(1, 12).forEach { i ->
            run {
                val reservationCommand = ReservationCreateCommand(
                    userId = 1,
                    roomId = savedRoomId,
                    roomCount = roomDto.roomCount,
                    price = roomDto.price,
                    checkIn = LocalDate.now().plusDays(i),
                    checkOut = LocalDate.now().plusDays(i + 5)
                )
                reservationService.createReservation(reservationCommand)
            }
        }

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get(RESERVATION_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(11))
            .assertThat().body("page.totalPages", equalTo(2))
            .assertThat().body("page.number", equalTo(0))
            .assertThat()
            .body(
                "_embedded.reservationListingResponseList[0].roomId",
                equalTo(savedRoomId.toInt())
            )
            .body(
                "_embedded.reservationListingResponseList[0].roomCount",
                equalTo(roomDto.roomCount)
            )
            .body(
                "_embedded.reservationListingResponseList[0].status",
                equalTo(Status.COMPLETION.toString())
            )
            .body(
                "_embedded.reservationListingResponseList[0].price",
                equalTo(roomDto.price.toInt())
            )
    }

    @Test
    fun `예약 검색 및 조회 - 회원 아이디로 조회`() {
        val roomDto = RoomCreateDto(
            lodgingCompanyId = 1,
            type = Type.FAMILY_ROOM,
            name = "일반실",
            roomCount = 25,
            standardPersonnel = 2,
            maximumPersonnel = 2,
            price = 130_000,
            content = "코로나 19로 인한 조식 중단",
            images = mutableListOf(
                RoomImageCreateDto(100L, "image1-url"),
                RoomImageCreateDto(300L, "image3-url"),
                RoomImageCreateDto(200L, "image2-url"),
            ),
            facilities = mutableListOf(
                RoomFacilitiesCreateDto("2인", "icon1-url", 100L),
                RoomFacilitiesCreateDto("퀸 침대", "icon2-url", 300L),
                RoomFacilitiesCreateDto("정수기 비치", "icon3-url", 200L),
            )
        )
        val savedRoomId = roomService.createRoom(roomDto.toCommand())
        LongStream.range(1, 5).forEach { i ->
            run {
                val reservationCommand = ReservationCreateCommand(
                    userId = i,
                    roomId = savedRoomId,
                    roomCount = roomDto.roomCount,
                    price = roomDto.price,
                    checkIn = LocalDate.now(),
                    checkOut = LocalDate.now().plusDays(5)
                )
                reservationService.createReservation(reservationCommand)
            }
        }

        RestAssured.given()
            .contentType(ContentType.JSON)
            .queryParam("userId", 1)
            .`when`()
            .log().all()
            .get(RESERVATION_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(1))
            .assertThat().body("page.totalPages", equalTo(1))
            .assertThat().body("page.number", equalTo(0))
            .assertThat()
            .body("_embedded.reservationListingResponseList[0].userId", equalTo(1))
    }

    @Test
    fun `예약 검색 및 조회 - 체크인으로 조회`() {
        val roomDto = RoomCreateDto(
            lodgingCompanyId = 1,
            type = Type.FAMILY_ROOM,
            name = "일반실",
            roomCount = 25,
            standardPersonnel = 2,
            maximumPersonnel = 2,
            price = 130_000,
            content = "코로나 19로 인한 조식 중단",
            images = mutableListOf(
                RoomImageCreateDto(100L, "image1-url"),
                RoomImageCreateDto(300L, "image3-url"),
                RoomImageCreateDto(200L, "image2-url"),
            ),
            facilities = mutableListOf(
                RoomFacilitiesCreateDto("2인", "icon1-url", 100L),
                RoomFacilitiesCreateDto("퀸 침대", "icon2-url", 300L),
                RoomFacilitiesCreateDto("정수기 비치", "icon3-url", 200L),
            )
        )
        val savedRoomId = roomService.createRoom(roomDto.toCommand())
        LongStream.range(1, 5).forEach { i ->
            run {
                val reservationCommand = ReservationCreateCommand(
                    userId = i,
                    roomId = savedRoomId,
                    roomCount = roomDto.roomCount,
                    price = roomDto.price,
                    checkIn = LocalDate.now().plusDays(i),
                    checkOut = LocalDate.now().plusDays(i + 5)
                )
                reservationService.createReservation(reservationCommand)
            }
        }

        RestAssured.given()
            .contentType(ContentType.JSON)
            .queryParam("checkIn", LocalDate.now().plusDays(2).toString())
            .`when`()
            .log().all()
            .get(RESERVATION_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(1))
            .assertThat().body("page.totalPages", equalTo(1))
            .assertThat().body("page.number", equalTo(0))
            .assertThat()
            .body(
                "_embedded.reservationListingResponseList[0].checkIn",
                equalTo(LocalDate.now().plusDays(2).toString())
            )
    }

    @Test
    fun `예약 검색 및 조회 - 체크아웃으로 조회`() {
        val roomDto = RoomCreateDto(
            lodgingCompanyId = 1,
            type = Type.FAMILY_ROOM,
            name = "일반실",
            roomCount = 25,
            standardPersonnel = 2,
            maximumPersonnel = 2,
            price = 130_000,
            content = "코로나 19로 인한 조식 중단",
            images = mutableListOf(
                RoomImageCreateDto(100L, "image1-url"),
                RoomImageCreateDto(300L, "image3-url"),
                RoomImageCreateDto(200L, "image2-url"),
            ),
            facilities = mutableListOf(
                RoomFacilitiesCreateDto("2인", "icon1-url", 100L),
                RoomFacilitiesCreateDto("퀸 침대", "icon2-url", 300L),
                RoomFacilitiesCreateDto("정수기 비치", "icon3-url", 200L),
            )
        )
        val savedRoomId = roomService.createRoom(roomDto.toCommand())
        LongStream.range(1, 5).forEach { i ->
            run {
                val reservationCommand = ReservationCreateCommand(
                    userId = i,
                    roomId = savedRoomId,
                    roomCount = roomDto.roomCount,
                    price = roomDto.price,
                    checkIn = LocalDate.now().plusDays(i),
                    checkOut = LocalDate.now().plusDays(i + 5)
                )
                reservationService.createReservation(reservationCommand)
            }
        }

        RestAssured.given()
            .contentType(ContentType.JSON)
            .queryParam("checkOut", LocalDate.now().plusDays(2 + 5).toString())
            .`when`()
            .log().all()
            .get(RESERVATION_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(1))
            .assertThat().body("page.totalPages", equalTo(1))
            .assertThat().body("page.number", equalTo(0))
            .assertThat()
            .body(
                "_embedded.reservationListingResponseList[0].checkOut",
                equalTo(LocalDate.now().plusDays(7).toString())
            )
    }

    @Test
    fun `예약 상세 조회`() {
        val command = aReservation(savedRoomId)
        val savedReservationId = reservationService.createReservation(command)

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$RESERVATION_REQUEST_URL/{id}", savedReservationId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("userId", equalTo(command.userId.toInt()))
            .assertThat().body("roomId", equalTo(command.roomId.toInt()))
            .assertThat().body("roomCount", equalTo(command.roomCount))
            .assertThat().body("status", equalTo(Status.COMPLETION.toString()))
            .assertThat().body("price", equalTo(command.price.toInt()))
            .assertThat().body("checkIn", equalTo(command.checkIn.toString()))
            .assertThat().body("checkOut", equalTo(command.checkOut.toString()))
    }
}