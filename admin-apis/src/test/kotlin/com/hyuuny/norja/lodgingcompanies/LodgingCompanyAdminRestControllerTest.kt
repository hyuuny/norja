package com.hyuuny.norja.lodgingcompanies

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.application.LodgingCompanyService
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.domain.LodgingCompanyRepository
import com.hyuuny.norja.domain.Status
import com.hyuuny.norja.domain.Type
import com.hyuuny.norja.lodgingcompanies.interfaces.req.FacilitiesCreateDto
import com.hyuuny.norja.lodgingcompanies.interfaces.req.ImageCreateDto
import com.hyuuny.norja.lodgingcompanies.interfaces.req.LodgingCompanyCreateDto
import com.hyuuny.norja.lodgingcompanies.interfaces.req.LodgingCompanyUpdateDto
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

const val LODGING_COMPANY_REQUEST_URL = "/api/v1/lodging-companies"

class LodgingCompanyAdminRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @Autowired
    lateinit var lodgingCompanyRepository: LodgingCompanyRepository

    @Autowired
    lateinit var lodgingCompanyService: LodgingCompanyService

    @AfterEach
    fun afterEach() = lodgingCompanyRepository.deleteAll()

    @Test
    fun `숙박 업체 등록`() {
        val dto = createDto()

        given()
            .body(dto)
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .post("$LODGING_COMPANY_REQUEST_URL")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    private fun createDto() = LodgingCompanyCreateDto(
        type = Type.HOTEL,
        name = "스테이 호텔",
        thumbnail = "thumbnail-url",
        businessNumber = "1231212345",
        tellNumber = "07012341234",
        address = Address("01234", "서울특별시 강남구 테헤란로 123", "3층"),
        searchTag = "스테이, 강남",
        images = mutableListOf(
            ImageCreateDto(1L, "image1-url"),
            ImageCreateDto(7L, "image7-url"),
            ImageCreateDto(5L, "image5-url")
        ),
        facilities = mutableListOf(
            FacilitiesCreateDto("주차가능", "parking-url", 100L),
            FacilitiesCreateDto("PC 2대", "parking-url", 300L),
            FacilitiesCreateDto("넷플릭스 이용 가능", "parking-url", 200L)
        ),
    )

    @Test
    fun `숙박 업체 상세 조회`() {
        val dto = createDto()
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(dto.toCommand())

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$LODGING_COMPANY_REQUEST_URL/{id}", savedLodgingCompanyId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(containsString("id"))
            .assertThat().body("name", equalTo(dto.name))
            .assertThat().body("status", equalTo(Status.OPEN.toString()))
            .assertThat().body("thumbnail", equalTo(dto.thumbnail))
            .assertThat().body("businessNumber", equalTo(dto.businessNumber))
            .assertThat().body("tellNumber", equalTo(dto.tellNumber))
            .assertThat().body("address.zipcode", equalTo(dto.address.zipcode))
            .assertThat().body("address.address", equalTo(dto.address.address))
            .assertThat().body("address.detailAddress", equalTo(dto.address.detailAddress))
            .assertThat().body("images[0].imageUrl", equalTo(dto.images[0].imageUrl))
            .assertThat().body("images[1].imageUrl", equalTo(dto.images[2].imageUrl))
            .assertThat().body("images[2].imageUrl", equalTo(dto.images[1].imageUrl))
            .assertThat().body("facilities[0].name", equalTo(dto.facilities[0].name))
            .assertThat().body("facilities[1].name", equalTo(dto.facilities[2].name))
            .assertThat().body("facilities[2].name", equalTo(dto.facilities[1].name))
    }

    @Test
    fun `숙박 업체 수정`() {
        val dto = createDto()
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(dto.toCommand())

        val updateDto = LodgingCompanyUpdateDto(
            type = Type.MOTEL,
            name = "바닷가 모텔",
            thumbnail = "thumthum-url",
            businessNumber = "2548654932",
            tellNumber = "0321231524",
            address = Address("45215", "강릉 삼척로 456", "1층"),
            searchTag = "바닷갈, 바닷길, 강릉",
            images = mutableListOf(
                ImageCreateDto(1L, "image1-url"),
                ImageCreateDto(2L, "image2-url")
            ),
        )

        given()
            .contentType(ContentType.JSON)
            .body(updateDto)
            .`when`()
            .log().all()
            .put("$LODGING_COMPANY_REQUEST_URL/{id}", savedLodgingCompanyId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(containsString("id"))
            .assertThat().body("name", equalTo(updateDto.name))
            .assertThat().body("type", equalTo(updateDto.type.name))
            .assertThat().body("status", equalTo(Status.OPEN.toString()))
            .assertThat().body("thumbnail", equalTo(updateDto.thumbnail))
            .assertThat().body("businessNumber", equalTo(updateDto.businessNumber))
            .assertThat().body("tellNumber", equalTo(updateDto.tellNumber))
            .assertThat().body("address.zipcode", equalTo(updateDto.address.zipcode))
            .assertThat().body("address.address", equalTo(updateDto.address.address))
            .assertThat().body("address.detailAddress", equalTo(updateDto.address.detailAddress))
            .assertThat().body("images[0].imageUrl", equalTo(updateDto.images[0].imageUrl))
            .assertThat().body("images[1].imageUrl", equalTo(updateDto.images[1].imageUrl))
    }

    @Test
    fun `휴가 처리`() {
        val dto = createDto()
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(dto.toCommand())

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .put("$LODGING_COMPANY_REQUEST_URL/vacation/{id}", savedLodgingCompanyId)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `숙박 업체 삭제`() {
        val dto = createDto()
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(dto.toCommand())

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .delete("$LODGING_COMPANY_REQUEST_URL/{id}", savedLodgingCompanyId)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }
}