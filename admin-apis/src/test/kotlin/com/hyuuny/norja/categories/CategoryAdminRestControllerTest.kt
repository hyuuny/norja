package com.hyuuny.norja.categories

import com.hyuuny.norja.ADMIN_EMAIL
import com.hyuuny.norja.ADMIN_PASSWORD
import com.hyuuny.norja.FixtureCategory.Companion.aCategory
import com.hyuuny.norja.application.CategoryService
import com.hyuuny.norja.categories.interfaces.CategoryUpdateDto
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.infrastructure.CategoryRepository
import io.restassured.RestAssured
import io.restassured.RestAssured.given
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
import java.util.*
import java.util.stream.IntStream

const val CATEGORY_REQUEST_URL = "/api/v1/categories"

class CategoryAdminRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @AfterEach
    fun afterEach() = categoryRepository.deleteAll()

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var categoryService: CategoryService

    var dto = aCategory()


    @Test
    fun `카테고리 등록`() {
        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .body(dto)
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .post(CATEGORY_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `자녀 카테고리 등록`() {
        val savedParentCategoryId = categoryService.createCategory(dto.toCommand())
        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .body(aCategory(name = "호텔", level = 2))
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .post("$CATEGORY_REQUEST_URL/{parentCategoryId}/child", savedParentCategoryId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `카테고리 전체 조회`() {
        val random = Random()
        IntStream.range(0, 11).forEach { value ->
            run {
                val dto = aCategory(
                    name = "카테고리 $value",
                    level = random.nextInt(1, 3),
                    priority = value.times(10).toLong()
                )
                categoryService.createCategory(dto.toCommand())
            }
        }

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get(CATEGORY_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `자녀 카테고리 조회`() {
        val savedParentCategoryId = categoryService.createCategory(dto.toCommand())

        val firstChildDto = aCategory(name = "호텔", level = 2, priority = 500)
        val secondChildDto = aCategory(name = "모텔", level = 2, priority = 400)
        val thirdChildDto = aCategory(name = "글램핑/캠프", level = 2, priority = 800)
        categoryService.createChildCategory(savedParentCategoryId, firstChildDto.toCommand())
        categoryService.createChildCategory(savedParentCategoryId, secondChildDto.toCommand())
        categoryService.createChildCategory(savedParentCategoryId, thirdChildDto.toCommand())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$CATEGORY_REQUEST_URL/{parentCategoryId}/children", savedParentCategoryId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("[0].name", equalTo("모텔"))
            .body("[0].priority", equalTo(400))
            .body("[0].level", equalTo(2))
            .body("[0].parentId", equalTo(savedParentCategoryId.toInt()))
            .body("[1].name", equalTo("호텔"))
            .body("[1].priority", equalTo(500))
            .body("[1].level", equalTo(2))
            .body("[1].parentId", equalTo(savedParentCategoryId.toInt()))
            .body("[2].name", equalTo("글램핑/캠프"))
            .body("[2].priority", equalTo(800))
            .body("[2].level", equalTo(2))
            .body("[2].parentId", equalTo(savedParentCategoryId.toInt()))
    }

    @Test
    fun `카테고리 상세 조회`() {
        val savedCategoryId = categoryService.createCategory(dto.toCommand())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$CATEGORY_REQUEST_URL/{id}", savedCategoryId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("name", equalTo(dto.name))
            .assertThat().body("priority", equalTo(dto.priority.toInt()))
            .assertThat().body("level", equalTo(dto.level))
            .assertThat().body("iconImageUrl", equalTo(dto.iconImageUrl))
    }

    @Test
    fun `카테고리 수정`() {
        val savedCategoryId = categoryService.createCategory(dto.toCommand())

        val updateDto = CategoryUpdateDto(
            name = "해외호텔",
            priority = 500,
            level = 1,
            iconImageUrl = "modify-icon-image-url",
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
            .put("$CATEGORY_REQUEST_URL/{id}", savedCategoryId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("name", equalTo(updateDto.name))
            .assertThat().body("priority", equalTo(updateDto.priority.toInt()))
            .assertThat().body("level", equalTo(updateDto.level))
            .assertThat().body("iconImageUrl", equalTo(updateDto.iconImageUrl))
    }

    @Test
    fun `카테고리 삭제`() {
        val savedCategoryId = categoryService.createCategory(dto.toCommand())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .delete("$CATEGORY_REQUEST_URL/{id}", savedCategoryId)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }

}