package com.hyuuny.norja.lodgingcompanies

import com.hyuuny.norja.ADMIN_EMAIL
import com.hyuuny.norja.ADMIN_PASSWORD
import com.hyuuny.norja.FixtureLodgingCompany.Companion.aLodgingCompanyDto
import com.hyuuny.norja.FixtureReview
import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.application.CategoryService
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.domain.CategoryCreateCommand
import com.hyuuny.norja.infrastructure.CategoryRepository
import com.hyuuny.norja.lodgingcompanies.application.LodgingCompanyService
import com.hyuuny.norja.lodgingcompanies.domain.FacilitiesCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.ImageCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.Status
import com.hyuuny.norja.lodgingcompanies.domain.Type.*
import com.hyuuny.norja.lodgingcompanies.infrastructure.LodgingCompanyRepository
import com.hyuuny.norja.lodgingcompanies.interfaces.ImageCreateDto
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyUpdateDto
import com.hyuuny.norja.reviews.application.ReviewService
import com.hyuuny.norja.reviews.infrastructure.ReviewRepository
import com.hyuuny.norja.rooms.application.RoomService
import com.hyuuny.norja.rooms.domain.RoomCreateCommand
import com.hyuuny.norja.rooms.domain.RoomFacilitiesCreateCommand
import com.hyuuny.norja.rooms.domain.RoomImageCreateCommand
import com.hyuuny.norja.rooms.domain.Type
import com.hyuuny.norja.rooms.infrastructure.RoomRepository
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
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.*
import java.util.stream.IntStream

const val LODGING_COMPANY_REQUEST_URL = "/api/v1/lodging-companies"

class LodgingCompanyAdminRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        savedCategoryId = categoryService.createCategory(
            CategoryCreateCommand(
                name = "국내호텔",
                priority = 100,
                level = 1,
                iconImageUrl = "icon-image-url",
            )
        )
    }

    @AfterEach
    fun afterEach() {
        reviewRepository.deleteAll()
        roomRepository.deleteAll()
        lodgingCompanyRepository.deleteAll()
        categoryRepository.deleteAll()
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
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var reviewService: ReviewService

    @Autowired
    lateinit var roomRepository: RoomRepository

    @Autowired
    lateinit var roomService: RoomService


    var savedCategoryId = 0L


    @Test
    fun `숙박 업체 검색 및 조회`() {
        val random = Random()
        IntStream.range(1, 12).forEach { i ->
            run {
                // 숙박 업체 등록
                val command = LodgingCompanyCreateCommand(
                    categoryId = savedCategoryId,
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

                val roomCommand = RoomCreateCommand(
                    lodgingCompanyId = savedLodgingCompanyId,
                    type = Type.DOUBLE_ROOM,
                    name = "일반실",
                    standardPersonnel = 2,
                    maximumPersonnel = 2,
                    price = (1000 * i).toLong(),
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

                IntStream.range(1, 6).forEach {
                    run {
                        val command = FixtureReview.aReview(
                            lodgingCompanyId = savedLodgingCompanyId,
                            wholeScore = random.nextInt(1, 6),
                            serviceScore = random.nextInt(1, 6),
                            cleanlinessScore = random.nextInt(1, 6),
                            convenienceScore = random.nextInt(1, 6),
                            satisfactionScore = random.nextInt(1, 6),
                        )
                        reviewService.createReview(command)
                    }
                }
            }
        }

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .params(
                mutableMapOf(
                    "checkIn" to LocalDate.now().toString(),
                    "checkOut" to LocalDate.now().plusDays(5).toString()
                )
            )
            .`when`()
            .log().all()
            .get(LODGING_COMPANY_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalPages", equalTo(2))
            .assertThat().body("page.number", equalTo(0))
            .assertThat()
            .body("_embedded.lodgingCompanies[0].type", equalTo(HOTEL.toString()))
            .assertThat()
            .body("_embedded.lodgingCompanies[0].status", equalTo(Status.OPEN.toString()))
            .assertThat().body("_embedded.lodgingCompanies[0].thumbnail", equalTo("thumbnail-url"))
            .assertThat()
            .body("_embedded.lodgingCompanies[0].price", equalTo(11000))
    }

    @Test
    fun `숙박 업체 검색 및 조회 - 카테고리 아이디 검색`() {
        val firstCategoryId =
            categoryService.createCategory(CategoryCreateCommand("국내호텔", 100, 1, "icon-image1-url"))
        val secondCategoryId =
            categoryService.createCategory(CategoryCreateCommand("해외호텔", 200, 1, "icon-image2-url"))

        val random = Random()
        var savedLodgingCompanyId = 0L
        IntStream.range(1, 12).forEach { i ->
            run {
                if (i % 2 == 0) {
                    // 숙박 업체 등록
                    val command = LodgingCompanyCreateCommand(
                        categoryId = firstCategoryId,
                        type = MOTEL,
                        name = "궁전모텔",
                        thumbnail = "thumbnail-url",
                        businessNumber = "1231212345",
                        tellNumber = "07012341234",
                        address = Address("01234", "서울특별시 강남구 테헤란로 123", "3층"),
                        searchTag = "한옥느낌, 인프라",
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
                    savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(command)

                    val roomCommand = RoomCreateCommand(
                        lodgingCompanyId = savedLodgingCompanyId,
                        type = Type.DOUBLE_ROOM,
                        name = "일반실",
                        standardPersonnel = 2,
                        maximumPersonnel = 2,
                        price = (1000 * i).toLong(),
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
                } else {
                    val command = LodgingCompanyCreateCommand(
                        categoryId = secondCategoryId,
                        type = HOTEL,
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
                    savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(command)

                    val roomCommand = RoomCreateCommand(
                        lodgingCompanyId = savedLodgingCompanyId,
                        type = Type.DOUBLE_ROOM,
                        name = "일반실",
                        standardPersonnel = 2,
                        maximumPersonnel = 2,
                        price = (1000 * i).toLong(),
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

                IntStream.range(1, 6).forEach {
                    run {
                        val command = FixtureReview.aReview(
                            lodgingCompanyId = savedLodgingCompanyId,
                            wholeScore = random.nextInt(1, 6),
                            serviceScore = random.nextInt(1, 6),
                            cleanlinessScore = random.nextInt(1, 6),
                            convenienceScore = random.nextInt(1, 6),
                            satisfactionScore = random.nextInt(1, 6),
                            reviewPhotos = mutableListOf(),
                        )
                        reviewService.createReview(command)
                    }
                }
            }
        }

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .params(
                mutableMapOf(
                    "categoryId" to firstCategoryId,
                    "checkIn" to LocalDate.now().toString(),
                    "checkOut" to LocalDate.now().plusDays(5).toString()
                )
            )
            .`when`()
            .log().all()
            .get(LODGING_COMPANY_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(5))
            .assertThat().body("page.totalPages", equalTo(1))
            .assertThat().body("page.number", equalTo(0))
            .assertThat()
            .body("_embedded.lodgingCompanies[0].type", equalTo(MOTEL.toString()))
            .assertThat()
            .body("_embedded.lodgingCompanies[0].status", equalTo(Status.OPEN.toString()))
            .assertThat().body(
                "_embedded.lodgingCompanies[0].categoryId",
                equalTo(firstCategoryId.toInt())
            )
            .assertThat().body("_embedded.lodgingCompanies[0].name", equalTo("궁전모텔"))
            .assertThat().body("_embedded.lodgingCompanies[0].thumbnail", equalTo("thumbnail-url"))
            .assertThat().body("_embedded.lodgingCompanies[0].searchTag", equalTo("한옥느낌, 인프라"))
            .assertThat().body("_embedded.lodgingCompanies[0].reviewCount", equalTo(5))
    }

    @Test
    fun `숙박 업체 검색 및 조회 - 타입 검색`() {
        val random = Random()
        var savedLodgingCompanyId = 0L
        IntStream.range(1, 12).forEach { i ->
            run {
                if (i % 2 == 0) {
                    // 숙박 업체 등록
                    val command = LodgingCompanyCreateCommand(
                        categoryId = savedCategoryId,
                        type = MOTEL,
                        name = "궁전모텔",
                        thumbnail = "thumbnail-url",
                        businessNumber = "1231212345",
                        tellNumber = "07012341234",
                        address = Address("01234", "서울특별시 강남구 테헤란로 123", "3층"),
                        searchTag = "한옥느낌, 인프라",
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
                    savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(command)

                    val roomCommand = RoomCreateCommand(
                        lodgingCompanyId = savedLodgingCompanyId,
                        type = Type.DOUBLE_ROOM,
                        name = "일반실",
                        standardPersonnel = 2,
                        maximumPersonnel = 2,
                        price = (1000 * i).toLong(),
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
                } else {
                    val command = LodgingCompanyCreateCommand(
                        categoryId = savedCategoryId,
                        type = POOL_VILLA,
                        name = "제주 풀빌라[POOL_VILLA]",
                        thumbnail = "pool-villa-url",
                        businessNumber = "1231212345",
                        tellNumber = "07012341234",
                        address = Address("63032", "제주시 한림읍 문교길 12", "1층"),
                        searchTag = "제주, 한림, 풀빌라",
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
                    savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(command)

                    val roomCommand = RoomCreateCommand(
                        lodgingCompanyId = savedLodgingCompanyId,
                        type = Type.DOUBLE_ROOM,
                        name = "일반실",
                        standardPersonnel = 2,
                        maximumPersonnel = 2,
                        price = (1000 * i).toLong(),
                        content = "넓디 넓은 방입니다.",
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

                IntStream.range(1, 6).forEach {
                    run {
                        val command = FixtureReview.aReview(
                            lodgingCompanyId = savedLodgingCompanyId,
                            wholeScore = random.nextInt(1, 6),
                            serviceScore = random.nextInt(1, 6),
                            cleanlinessScore = random.nextInt(1, 6),
                            convenienceScore = random.nextInt(1, 6),
                            satisfactionScore = random.nextInt(1, 6),
                            reviewPhotos = mutableListOf(),
                        )
                        reviewService.createReview(command)
                    }
                }
            }
        }

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .params(
                mutableMapOf(
                    "type" to POOL_VILLA,
                    "checkIn" to LocalDate.now().toString(),
                    "checkOut" to LocalDate.now().plusDays(5).toString()
                )
            )
            .`when`()
            .log().all()
            .get(LODGING_COMPANY_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(6))
            .assertThat().body("page.totalPages", equalTo(1))
            .assertThat().body("page.number", equalTo(0))
            .assertThat()
            .body("_embedded.lodgingCompanies[0].type", equalTo(POOL_VILLA.toString()))
            .assertThat()
            .body("_embedded.lodgingCompanies[0].status", equalTo(Status.OPEN.toString()))
            .assertThat()
            .body("_embedded.lodgingCompanies[0].categoryId", equalTo(savedCategoryId.toInt()))
            .assertThat().body("_embedded.lodgingCompanies[0].name", equalTo("제주 풀빌라[POOL_VILLA]"))
            .assertThat().body("_embedded.lodgingCompanies[0].thumbnail", equalTo("pool-villa-url"))
            .assertThat().body("_embedded.lodgingCompanies[0].searchTag", equalTo("제주, 한림, 풀빌라"))
            .assertThat().body("_embedded.lodgingCompanies[0].reviewCount", equalTo(5))
    }

    @Test
    fun `숙박 업체 등록`() {
        val dto = aLodgingCompanyDto(savedCategoryId)

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .body(dto)
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .post("$LODGING_COMPANY_REQUEST_URL")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `숙박 업체 상세 조회`() {
        val dto = aLodgingCompanyDto(savedCategoryId)
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(dto.toCommand())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$LODGING_COMPANY_REQUEST_URL/{id}", savedLodgingCompanyId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(containsString("id"))
            .assertThat().body("name", equalTo(dto.name))
            .assertThat().body("categoryId", equalTo(dto.categoryId.toInt()))
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
        val dto = aLodgingCompanyDto(savedCategoryId)
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(dto.toCommand())

        val newCategoryId = categoryService.createCategory(
            CategoryCreateCommand(
                name = "해외호텔",
                priority = 100,
                level = 1,
                iconImageUrl = "icon-image2-url",
            )
        )
        val updateDto = LodgingCompanyUpdateDto(
            categoryId = newCategoryId,
            type = HOTEL,
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
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
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
            .assertThat().body("categoryId", equalTo(updateDto.categoryId.toInt()))
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
        val dto = aLodgingCompanyDto(savedCategoryId)
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(dto.toCommand())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
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
        val dto = aLodgingCompanyDto(savedCategoryId)
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(dto.toCommand())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .delete("$LODGING_COMPANY_REQUEST_URL/{id}", savedLodgingCompanyId)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }
}