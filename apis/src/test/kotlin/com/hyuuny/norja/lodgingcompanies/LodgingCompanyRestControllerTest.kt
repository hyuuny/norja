package com.hyuuny.norja.lodgingcompanies

import com.hyuuny.norja.FixtureLodgingCompany.Companion.aLodgingCompanyCommand
import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.lodgingcompanies.application.LodgingCompanyService
import com.hyuuny.norja.lodgingcompanies.domain.FacilitiesCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.ImageCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.Status.OPEN
import com.hyuuny.norja.lodgingcompanies.domain.Type.HOTEL
import com.hyuuny.norja.rooms.application.RoomService
import com.hyuuny.norja.rooms.domain.*
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import java.util.stream.IntStream

const val LODGING_COMPANY_REQUEST_URL = "/api/v1/lodging-companies"

class LodgingCompanyRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @AfterEach
    fun afterEach() {
        roomRepository.deleteAll()
        lodgingCompanyRepository.deleteAll()
    }

    @Autowired
    lateinit var lodgingCompanyService: LodgingCompanyService

    @Autowired
    lateinit var lodgingCompanyRepository: RoomRepository

    @Autowired
    lateinit var roomRepository: RoomRepository

    @Autowired
    lateinit var roomService: RoomService


    @Test
    fun `숙박 업체 상세 조회`() {
        val command = aLodgingCompanyCommand()
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(command)

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$LODGING_COMPANY_REQUEST_URL/{id}", savedLodgingCompanyId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(containsString("id"))
            .assertThat().body("name", equalTo(command.name))
            .assertThat().body("status", equalTo(OPEN.toString()))
            .assertThat().body("thumbnail", equalTo(command.thumbnail))
            .assertThat().body("businessNumber", equalTo(command.businessNumber))
            .assertThat().body("tellNumber", equalTo(command.tellNumber))
            .assertThat().body("address.zipcode", equalTo(command.address.zipcode))
            .assertThat().body("address.address", equalTo(command.address.address))
            .assertThat().body("address.detailAddress", equalTo(command.address.detailAddress))
            .assertThat().body("images[0].imageUrl", equalTo(command.images[0].imageUrl))
            .assertThat().body("images[1].imageUrl", equalTo(command.images[2].imageUrl))
            .assertThat().body("images[2].imageUrl", equalTo(command.images[1].imageUrl))
            .assertThat().body("facilities[0].name", equalTo(command.facilities[0].name))
            .assertThat().body("facilities[1].name", equalTo(command.facilities[2].name))
            .assertThat().body("facilities[2].name", equalTo(command.facilities[1].name))
    }

    @Test
    fun `숙박 업체 상세 조회 - 잘못된 아이디 예외`() {
        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$LODGING_COMPANY_REQUEST_URL/{id}", 999999)
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun `숙박 업체 검색 및 조회`() {


        IntStream.range(0, 11).forEach { i ->
            run {
                // 숙박 업체 등록
                val command = LodgingCompanyCreateCommand(
                    type = HOTEL,
                    name = "스테이 호텔 $i",
                    thumbnail = "thumbnail-url",
                    businessNumber = "1231212345",
                    tellNumber = "07012341234",
                    address = Address("01234", "서울특별시 강남구 테헤란로 123", "3층"),
                    searchTag = "스테이, 강남",
                    images = mutableListOf(
                        ImageCreateCommand(1L, "image1-url"),
                        ImageCreateCommand(7L, "image7-url"),
                        ImageCreateCommand(5L, "image5-url")
                    ),
                    facilities = mutableListOf(
                        FacilitiesCreateCommand("주차가능", "parking-url", 100L),
                        FacilitiesCreateCommand("PC 2대", "parking-url", 300L),
                        FacilitiesCreateCommand("넷플릭스 이용 가능", "parking-url", 200L)
                    ),
                )
                val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(command)

                IntStream.range(1, 4).forEach { value ->
                    run {
                        val roomCommand = RoomCreateCommand(
                            lodgingCompanyId = savedLodgingCompanyId,
                            type = Type.DOUBLE_ROOM,
                            name = "일반실",
                            standardPersonnel = 2,
                            maximumPersonnel = 2,
                            price = (1000 * value).toLong(),
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
                        roomService.createRoom(roomCommand)
                    }
                }
            }
        }

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get(LODGING_COMPANY_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(11))
            .assertThat().body("page.totalPages", equalTo(2))
            .assertThat().body("page.number", equalTo(0))
            .assertThat().body("_embedded.lodgingCompanyListingResponseList[0].type", equalTo(HOTEL.toString()))
            .assertThat().body("_embedded.lodgingCompanyListingResponseList[0].status", equalTo(OPEN.toString()))
            .assertThat().body("_embedded.lodgingCompanyListingResponseList[0].thumbnail", equalTo("thumbnail-url"))
            .assertThat().body("_embedded.lodgingCompanyListingResponseList[0].price", equalTo(1000))
    }
}
