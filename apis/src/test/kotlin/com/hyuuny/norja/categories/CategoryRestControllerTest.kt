package com.hyuuny.norja.categories

import com.hyuuny.norja.FixtureCategory.Companion.aCategory
import com.hyuuny.norja.application.CategoryService
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
import org.springframework.http.HttpStatus
import java.util.stream.IntStream

const val CATEGORY_REQUEST_URL = "/api/v1/categories"

class CategoryRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @AfterEach
    fun tearDown() {
        deleteAllUsers()
        categoryRepository.deleteAll()
    }

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var categoryService: CategoryService


    @Test
    fun `카테고리 전체 조회`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                val command = aCategory(name = "카테고리 $value", priority = value.times(10).toLong())
                categoryService.createCategory(command)
            }
        }

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get(CATEGORY_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .body("[0].name", equalTo("카테고리 0"))
            .body("[0].priority", equalTo(0))
            .body("[1].name", equalTo("카테고리 1"))
            .body("[1].priority", equalTo(10))
            .body("[2].name", equalTo("카테고리 2"))
            .body("[2].priority", equalTo(20))
            .body("[3].name", equalTo("카테고리 3"))
            .body("[3].priority", equalTo(30))
            .body("[4].name", equalTo("카테고리 4"))
            .body("[4].priority", equalTo(40))
            .body("[5].name", equalTo("카테고리 5"))
            .body("[5].priority", equalTo(50))
            .body("[6].name", equalTo("카테고리 6"))
            .body("[6].priority", equalTo(60))
            .body("[7].name", equalTo("카테고리 7"))
            .body("[7].priority", equalTo(70))
            .body("[8].name", equalTo("카테고리 8"))
            .body("[8].priority", equalTo(80))
            .body("[9].name", equalTo("카테고리 9"))
            .body("[9].priority", equalTo(90))
            .body("[10].name", equalTo("카테고리 10"))
            .body("[10].priority", equalTo(100))
    }

    @Test
    fun `자녀 카테고리 조회`() {
        val command = aCategory(name = "국내숙소", level = 1)
        val savedParentCategoryId = categoryService.createCategory(command)

        val firstChildCommand = aCategory(name = "호텔", level = 2, priority = 500)
        val secondChildCommand = aCategory(name = "모텔", level = 2, priority = 400)
        val thirdChildCommand = aCategory(name = "글램핑/캠프", level = 2, priority = 800)
        categoryService.createChildCategory(savedParentCategoryId, firstChildCommand)
        categoryService.createChildCategory(savedParentCategoryId, secondChildCommand)
        categoryService.createChildCategory(savedParentCategoryId, thirdChildCommand)

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$CATEGORY_REQUEST_URL/{id}/children", savedParentCategoryId)
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
        val command = aCategory(name = "국내숙소", level = 1)
        val savedParentCategoryId = categoryService.createCategory(command)

        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$CATEGORY_REQUEST_URL/{id}", savedParentCategoryId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("name", equalTo(command.name))
            .assertThat().body("priority", equalTo(command.priority.toInt()))
            .assertThat().body("level", equalTo(command.level))
            .assertThat().body("iconImageUrl", equalTo(command.iconImageUrl))
    }

    @Test
    fun `카테고리 상세 조회 - 잘못된 아이디 예외`() {
        given()
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .get("$CATEGORY_REQUEST_URL/{id}", 999999)
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

}