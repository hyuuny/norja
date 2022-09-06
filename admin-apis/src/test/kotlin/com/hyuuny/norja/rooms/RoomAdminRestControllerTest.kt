package com.hyuuny.norja.rooms

import com.hyuuny.norja.ADMIN_EMAIL
import com.hyuuny.norja.ADMIN_PASSWORD
import com.hyuuny.norja.FixtureLodgingCompany.Companion.aLodgingCompanyDto
import com.hyuuny.norja.FixtureRoom
import com.hyuuny.norja.application.CategoryService
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.domain.CategoryCreateCommand
import com.hyuuny.norja.infrastructure.CategoryRepository
import com.hyuuny.norja.lodgingcompanies.application.LodgingCompanyService
import com.hyuuny.norja.lodgingcompanies.infrastructure.LodgingCompanyRepository
import com.hyuuny.norja.rooms.application.RoomService
import com.hyuuny.norja.rooms.domain.Type
import com.hyuuny.norja.rooms.infrastructure.RoomRepository
import com.hyuuny.norja.rooms.interfaces.RoomCreateDto
import com.hyuuny.norja.rooms.interfaces.RoomFacilitiesCreateDto
import com.hyuuny.norja.rooms.interfaces.RoomImageCreateDto
import com.hyuuny.norja.rooms.interfaces.RoomUpdateDto
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
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

const val ROOM_REQUEST_URL = "/api/v1/rooms"

class RoomAdminRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(
            aLodgingCompanyDto(
                categoryService.createCategory(
                    CategoryCreateCommand(
                        name = "국내호텔",
                        priority = 100,
                        level = 1,
                        iconImageUrl = "icon-image-url",
                    )
                )
            ).toCommand()
        )
    }

    @AfterEach
    fun afterEach() {
        roomRepository.deleteAll()
        lodgingCompanyRepository.deleteAll()
        categoryRepository.deleteAll()
        this.deleteCache()
    }

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var categoryService: CategoryService

    @Autowired
    lateinit var lodgingCompanyRepository: LodgingCompanyRepository

    @Autowired
    lateinit var lodgingCompanyService: LodgingCompanyService

    @Autowired
    lateinit var roomRepository: RoomRepository

    @Autowired
    lateinit var roomService: RoomService

    var savedLodgingCompanyId: Long = 0


    @Test
    fun `숙박업체 객실 등록`() {
        val dto = FixtureRoom.aRoom(savedLodgingCompanyId)

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .body(dto)
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .post(ROOM_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `객실 등록 - 타입 중복되면 예외`() {
        val dto = FixtureRoom.aRoom(savedLodgingCompanyId)
        val savedRoomId = roomService.createRoom(dto.toCommand())

        val duplicateTypeDto = RoomCreateDto(
            lodgingCompanyId = savedLodgingCompanyId,
            type = Type.DOUBLE_ROOM,
            name = "일반실",
            roomCount = 20,
            standardPersonnel = 2,
            maximumPersonnel = 2,
            price = 80_000,
            content = "코로나 19로 인한 조식 중단",
            images = mutableListOf(),
            facilities = mutableListOf()
        )

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .body(duplicateTypeDto)
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .post(ROOM_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `객실 상세 조회`() {
        val dto = FixtureRoom.aRoom(savedLodgingCompanyId)
        val savedRoomId = roomService.createRoom(dto.toCommand())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$ROOM_REQUEST_URL/{id}", savedRoomId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("lodgingCompanyId", IsEqual.equalTo(dto.lodgingCompanyId.toInt()))
            .assertThat().body("type", IsEqual.equalTo(dto.type.toString()))
            .assertThat().body("name", IsEqual.equalTo(dto.name))
            .assertThat().body("roomCount", IsEqual.equalTo(dto.roomCount))
            .assertThat().body("standardPersonnel", IsEqual.equalTo(dto.standardPersonnel))
            .assertThat().body("maximumPersonnel", IsEqual.equalTo(dto.maximumPersonnel))
            .assertThat().body("price", IsEqual.equalTo(dto.price.toInt()))
            .assertThat().body("content", IsEqual.equalTo(dto.content))
            .assertThat().body("roomImages[0].imageUrl", IsEqual.equalTo(dto.images[0].imageUrl))
            .assertThat().body("roomImages[1].imageUrl", IsEqual.equalTo(dto.images[2].imageUrl))
            .assertThat().body("roomImages[2].imageUrl", IsEqual.equalTo(dto.images[1].imageUrl))
            .assertThat().body("roomFacilities[0].name", IsEqual.equalTo(dto.facilities[0].name))
            .assertThat().body("roomFacilities[1].name", IsEqual.equalTo(dto.facilities[2].name))
            .assertThat().body("roomFacilities[2].name", IsEqual.equalTo(dto.facilities[1].name))
    }

    @Test
    fun `객실 상세 조회 - 잘못된 아이디 예외`() {
        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$ROOM_REQUEST_URL/{id}", 999999)
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `객실 수정`() {
        val dto = FixtureRoom.aRoom(savedLodgingCompanyId)
        val savedRoomId = roomService.createRoom(dto.toCommand())

        val updateDto = RoomUpdateDto(
            lodgingCompanyId = savedLodgingCompanyId,
            type = Type.FAMILY_ROOM,
            name = "패밀리룸",
            roomCount = 28,
            standardPersonnel = 6,
            maximumPersonnel = 6,
            price = 250_000,
            content = "방이 상당히 커요",
            images = mutableListOf(
                RoomImageCreateDto(100L, "image1-url"),
                RoomImageCreateDto(300L, "image3-url"),
                RoomImageCreateDto(200L, "image2-url"),
                RoomImageCreateDto(400L, "image4-url"),
            ),
            facilities = mutableListOf(
                RoomFacilitiesCreateDto("6인", "icon1-url", 100L),
                RoomFacilitiesCreateDto("테이블 비치", "icon2-url", 200L),
            )
        )

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .body(updateDto)
            .`when`()
            .log().all()
            .put("$ROOM_REQUEST_URL/{id}", savedRoomId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat()
            .body("lodgingCompanyId", IsEqual.equalTo(updateDto.lodgingCompanyId.toInt()))
            .assertThat().body("type", IsEqual.equalTo(updateDto.type.toString()))
            .assertThat().body("name", IsEqual.equalTo(updateDto.name))
            .assertThat().body("roomCount", IsEqual.equalTo(updateDto.roomCount))
            .assertThat().body("standardPersonnel", IsEqual.equalTo(updateDto.standardPersonnel))
            .assertThat().body("maximumPersonnel", IsEqual.equalTo(updateDto.maximumPersonnel))
            .assertThat().body("price", IsEqual.equalTo(updateDto.price.toInt()))
            .assertThat().body("content", IsEqual.equalTo(updateDto.content))
            .assertThat()
            .body("roomImages[0].imageUrl", IsEqual.equalTo(updateDto.images[0].imageUrl))
            .assertThat()
            .body("roomImages[1].imageUrl", IsEqual.equalTo(updateDto.images[2].imageUrl))
            .assertThat()
            .body("roomImages[2].imageUrl", IsEqual.equalTo(updateDto.images[1].imageUrl))
            .assertThat()
            .body("roomImages[3].imageUrl", IsEqual.equalTo(updateDto.images[3].imageUrl))
            .assertThat()
            .body("roomFacilities[0].name", IsEqual.equalTo(updateDto.facilities[0].name))
            .assertThat()
            .body("roomFacilities[1].name", IsEqual.equalTo(updateDto.facilities[1].name))
    }

    @Test
    fun `객실 삭제`() {
        val dto = FixtureRoom.aRoom(savedLodgingCompanyId)
        val savedRoomId = roomService.createRoom(dto.toCommand())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .delete("$ROOM_REQUEST_URL/{id}", savedRoomId)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }
}