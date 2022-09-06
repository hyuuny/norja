package com.hyuuny.norja.reviews

import com.hyuuny.norja.ADMIN_EMAIL
import com.hyuuny.norja.ADMIN_PASSWORD
import com.hyuuny.norja.FixtureReview.Companion.aReview
import com.hyuuny.norja.common.BaseIntegrationTest
import com.hyuuny.norja.reviews.application.ReviewService
import com.hyuuny.norja.reviews.domain.Type
import com.hyuuny.norja.reviews.infrastructure.ReviewRepository
import com.hyuuny.norja.reviews.interfaces.BestReviewDto
import com.hyuuny.norja.reviews.interfaces.ChangeBestReviewDto
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

const val REVIEW_REQUEST_URL = "/api/v1/reviews"

class ReviewAdminRestControllerTest : BaseIntegrationTest() {

    @LocalServerPort
    val port: Int = 0

    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var reviewService: ReviewService

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @AfterEach
    fun afterEach() {
        this.deleteAllUsers()
        reviewRepository.deleteAll()
        this.deleteCache()
    }

    @Test
    fun `후기 조회 및 검색`() {
        val random = Random()
        IntStream.range(0, 11).forEach { value ->
            run {
                val command = aReview(
                    lodgingCompanyId = 1L,
                    roomId = value.toLong(),
                    wholeScore = random.nextInt(1, 6),
                    serviceScore = random.nextInt(1, 6),
                    cleanlinessScore = random.nextInt(1, 6),
                    convenienceScore = random.nextInt(1, 6),
                    satisfactionScore = random.nextInt(1, 6),
                )
                reviewService.createReview(command)
            }
        }

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .queryParam("lodgingCompanyId", 1)
            .`when`()
            .log().all()
            .get(REVIEW_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(11))
            .assertThat().body("page.totalPages", equalTo(2))
            .assertThat().body("page.number", equalTo(0))
    }

    @Test
    fun `후기 조회 및 검색 - 회원 아이디로 조회`() {
        val random = Random()
        IntStream.range(0, 11).forEach { value ->
            run {
                if (value % 2 == 0) {
                    val command = aReview(
                        lodgingCompanyId = 1L,
                        roomId = value.toLong(),
                        userId = 1L,
                        wholeScore = random.nextInt(1, 6),
                        serviceScore = random.nextInt(1, 6),
                        cleanlinessScore = random.nextInt(1, 6),
                        convenienceScore = random.nextInt(1, 6),
                        satisfactionScore = random.nextInt(1, 6),
                    )
                    reviewService.createReview(command)
                } else {
                    val command = aReview(
                        lodgingCompanyId = 2L,
                        roomId = value.toLong(),
                        userId = 3L,
                        content = "${value}번째 들렀다가도 정말좋아요!",
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

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .queryParam("userId", 3)
            .`when`()
            .log().all()
            .get(REVIEW_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(5))
            .assertThat().body("page.totalPages", equalTo(1))
            .assertThat().body("page.number", equalTo(0))
            .assertThat()
            .body("_embedded.reviews[0].roomName", equalTo("더블룸"))
            .body("_embedded.reviews[0].content", equalTo("9번째 들렀다가도 정말좋아요!"))
            .body("_embedded.reviews[1].roomName", equalTo("더블룸"))
            .body("_embedded.reviews[1].content", equalTo("7번째 들렀다가도 정말좋아요!"))
            .body("_embedded.reviews[2].roomName", equalTo("더블룸"))
            .body("_embedded.reviews[2].content", equalTo("5번째 들렀다가도 정말좋아요!"))
            .body("_embedded.reviews[3].roomName", equalTo("더블룸"))
            .body("_embedded.reviews[3].content", equalTo("3번째 들렀다가도 정말좋아요!"))
            .body("_embedded.reviews[4].roomName", equalTo("더블룸"))
            .body("_embedded.reviews[4].content", equalTo("1번째 들렀다가도 정말좋아요!"))
            .body("_embedded.reviews[0].userId", equalTo(3))
            .body("_embedded.reviews[1].userId", equalTo(3))
            .body("_embedded.reviews[2].userId", equalTo(3))
            .body("_embedded.reviews[3].userId", equalTo(3))
            .body("_embedded.reviews[4].userId", equalTo(3))
    }

    @Test
    fun `후기 조회 및 검색 - 평점높은순 정렬`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                if (value % 2 == 0) {
                    val command = aReview(lodgingCompanyId = 1L)
                    reviewService.createReview(command)
                } else {
                    val command = aReview(
                        lodgingCompanyId = 2L,
                        wholeScore = 3,
                        serviceScore = 3,
                        cleanlinessScore = 3,
                        convenienceScore = 3,
                        satisfactionScore = 3,
                    )
                    reviewService.createReview(command)
                }
            }
        }

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .queryParam("sort", "wholeScore,desc")
            .`when`()
            .log().all()
            .get(REVIEW_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(11))
            .assertThat().body("page.totalPages", equalTo(2))
            .assertThat().body("page.number", equalTo(0))
            .body("_embedded.reviews[0].wholeScore", equalTo(5))
            .body("_embedded.reviews[1].wholeScore", equalTo(5))
            .body("_embedded.reviews[2].wholeScore", equalTo(5))
            .body("_embedded.reviews[3].wholeScore", equalTo(5))
            .body("_embedded.reviews[4].wholeScore", equalTo(5))
            .body("_embedded.reviews[5].wholeScore", equalTo(5))
            .body("_embedded.reviews[6].wholeScore", equalTo(3))
            .body("_embedded.reviews[7].wholeScore", equalTo(3))
            .body("_embedded.reviews[8].wholeScore", equalTo(3))
            .body("_embedded.reviews[9].wholeScore", equalTo(3))
    }

    @Test
    fun `후기 조회 및 검색 - 평점낮은순 정렬`() {
        IntStream.range(0, 11).forEach { value ->
            run {
                if (value % 2 == 0) {
                    val command = aReview(lodgingCompanyId = 1L)
                    reviewService.createReview(command)
                } else {
                    val command = aReview(
                        lodgingCompanyId = 2L,
                        wholeScore = 3,
                        serviceScore = 3,
                        cleanlinessScore = 3,
                        convenienceScore = 3,
                        satisfactionScore = 3,
                    )
                    reviewService.createReview(command)
                }
            }
        }

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .queryParam("sort", "wholeScore,asc")
            .`when`()
            .log().all()
            .get(REVIEW_REQUEST_URL)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("page.size", equalTo(10))
            .assertThat().body("page.totalElements", equalTo(11))
            .assertThat().body("page.totalPages", equalTo(2))
            .assertThat().body("page.number", equalTo(0))
            .body("_embedded.reviews[0].wholeScore", equalTo(3))
            .body("_embedded.reviews[1].wholeScore", equalTo(3))
            .body("_embedded.reviews[2].wholeScore", equalTo(3))
            .body("_embedded.reviews[3].wholeScore", equalTo(3))
            .body("_embedded.reviews[4].wholeScore", equalTo(3))
            .body("_embedded.reviews[5].wholeScore", equalTo(5))
            .body("_embedded.reviews[6].wholeScore", equalTo(5))
            .body("_embedded.reviews[7].wholeScore", equalTo(5))
            .body("_embedded.reviews[8].wholeScore", equalTo(5))
            .body("_embedded.reviews[9].wholeScore", equalTo(5))
    }


    @Test
    fun `후기 상세 조회`() {
        val command = aReview()
        val savedReviewId = reviewService.createReview(command)

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .get("$REVIEW_REQUEST_URL/{id}", savedReviewId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("type", equalTo(Type.PHOTO.toString()))
            .assertThat().body(
                "lodgingCompanyId",
                equalTo(command.lodgingCompanyId.toInt())
            )
            .assertThat().body("roomId", equalTo(command.roomId.toInt()))
            .assertThat().body("userId", equalTo(command.userId.toInt()))
            .assertThat().body("nickname", equalTo(command.nickname))
            .assertThat().body("roomName", equalTo(command.roomName))
            .assertThat().body("content", equalTo(command.content))
            .assertThat().body("wholeScore", equalTo(command.wholeScore))
            .assertThat().body("serviceScore", equalTo(command.serviceScore))
            .assertThat().body("cleanlinessScore", equalTo(command.cleanlinessScore))
            .assertThat().body("convenienceScore", equalTo(command.convenienceScore))
            .assertThat().body("satisfactionScore", equalTo(command.satisfactionScore))
            .assertThat().body("best", equalTo(false))
            .assertThat().body("satisfactionScore", equalTo(command.satisfactionScore))
            .assertThat()
            .body("reviewPhotos[0].imageUrl", equalTo(command.reviewPhotos[0].imageUrl))
            .assertThat()
            .body("reviewPhotos[1].imageUrl", equalTo(command.reviewPhotos[2].imageUrl))
            .assertThat()
            .body("reviewPhotos[2].imageUrl", equalTo(command.reviewPhotos[1].imageUrl))
            .assertThat().body(CoreMatchers.containsString("createdAt"))
    }

    @Test
    fun `후기 삭제`() {
        val savedReviewId = reviewService.createReview(aReview())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .delete("$REVIEW_REQUEST_URL/{id}", savedReviewId)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }

    @Test
    fun `베스트 후기 선정`() {
        val firstSavedReviewId = reviewService.createReview(aReview())
        val secondSavedReviewId = reviewService.createReview(aReview())
        val thirdSavedReviewId = reviewService.createReview(aReview())

        val dto = ChangeBestReviewDto(
            mutableListOf(
                BestReviewDto(id = firstSavedReviewId, best = true),
                BestReviewDto(id = secondSavedReviewId, best = true),
                BestReviewDto(id = thirdSavedReviewId, best = true),
            )
        )

        given()
            .body(dto)
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .post("$REVIEW_REQUEST_URL/best")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .get("$REVIEW_REQUEST_URL/{id}", firstSavedReviewId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("best", equalTo(true))
    }

    @Test
    fun `베스트 후기 해제`() {
        val firstSavedReviewId = reviewService.createReview(aReview())
        val secondSavedReviewId = reviewService.createReview(aReview())
        val thirdSavedReviewId = reviewService.createReview(aReview())

        reviewService.changeBestReview(
            ChangeBestReviewDto(
                mutableListOf(
                    BestReviewDto(id = firstSavedReviewId, best = true),
                    BestReviewDto(id = secondSavedReviewId, best = true),
                    BestReviewDto(id = thirdSavedReviewId, best = true),
                )
            ).toCommand()
        )

        val dto = ChangeBestReviewDto(
            mutableListOf(
                BestReviewDto(id = firstSavedReviewId, best = false),
                BestReviewDto(id = secondSavedReviewId, best = false),
                BestReviewDto(id = thirdSavedReviewId, best = false),
            )
        )

        given()
            .body(dto)
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .log().all()
            .post("$REVIEW_REQUEST_URL/best")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())

        given()
            .header(
                HttpHeaders.AUTHORIZATION,
                this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD)
            )
            .contentType(ContentType.JSON)
            .`when`()
            .get("$REVIEW_REQUEST_URL/{id}", firstSavedReviewId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body(CoreMatchers.containsString("id"))
            .assertThat().body("best", equalTo(false))
    }

}