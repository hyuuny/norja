package com.hyuuny.norja.lodgingcompanies

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.application.LodgingCompanyService
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.domain.Status
import com.hyuuny.norja.domain.Type
import com.hyuuny.norja.domain.command.FacilitiesCreateCommand
import com.hyuuny.norja.domain.command.ImageCreateCommand
import com.hyuuny.norja.domain.command.LodgingCompanyCreateCommand
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus

const val LODGING_COMPANY_REQUEST_URL = "/api/v1/lodging-companies"

class LodgingCompanyRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @Autowired
    lateinit var lodgingCompanyService: LodgingCompanyService


    @Test
    fun `숙박 업체 상세 조회`() {
        val command = LodgingCompanyCreateCommand(
            type = Type.HOTEL,
            name = "스테이 호텔",
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
            .assertThat().body("status", equalTo(Status.OPEN.toString()))
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

}