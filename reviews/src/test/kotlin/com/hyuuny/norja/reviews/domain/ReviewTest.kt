package com.hyuuny.norja.reviews.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ReviewTest {

    @Test
    fun `후기 등록`() {
        val expectedLodgingCompanyId = 1L
        val expectedRoomId = 1L
        val expectedUserId = 1L
        val expectedType = Type.TEXT
        val expectedNickname = "김성현"
        val expectedRoomName = "일반실"
        val expectedContent = "잘 쉬고가요! 너무 좋았네요 ㅎㅎ"
        val expectedScore = 5
        val expectedReviewPhotos = mutableListOf(
            ReviewPhoto.create(100, "review-image1-url"),
            ReviewPhoto.create(200, "review-image2-url"),
            ReviewPhoto.create(300, "review-image3-url"),
        )

        val newReview = FixtureReview.aReview()

        newReview.lodgingCompanyId shouldBe expectedLodgingCompanyId
        newReview.roomId shouldBe expectedRoomId
        newReview.userId shouldBe expectedUserId
        newReview.type shouldBe expectedType
        newReview.nickname shouldBe expectedNickname
        newReview.roomName shouldBe expectedRoomName
        newReview.content shouldBe expectedContent
        newReview.wholeScore shouldBe expectedScore
        newReview.reviewPhotos?.get(0)?.priority shouldBe expectedReviewPhotos[0].priority
        newReview.reviewPhotos?.get(1)?.priority shouldBe expectedReviewPhotos[1].priority
        newReview.reviewPhotos?.get(2)?.priority shouldBe expectedReviewPhotos[2].priority
        newReview.reviewPhotos?.get(0)?.imageUrl shouldBe expectedReviewPhotos[0].imageUrl
        newReview.reviewPhotos?.get(1)?.imageUrl shouldBe expectedReviewPhotos[1].imageUrl
        newReview.reviewPhotos?.get(2)?.imageUrl shouldBe expectedReviewPhotos[2].imageUrl
    }

    @Test
    fun `베스트 리뷰 선정`() {
        val expectedBest = true
        val newReview = FixtureReview.aReview()
        newReview.changeBestReview(expectedBest)
        newReview.best shouldBe expectedBest
    }

}

class FixtureReview {
    companion object {
        fun aReview(
            lodgingCompanyId: Long = 1L,
            roomId: Long = 1L,
            userId: Long = 1L,
            nickname: String = "김성현",
            roomName: String = "일반실",
            content: String = "잘 쉬고가요! 너무 좋았네요 ㅎㅎ",
            wholeScore: Int = 5,
            serviceScore: Int = 5,
            cleanlinessScore: Int = 5,
            convenienceScore: Int = 5,
            satisfactionScore: Int = 5,
            reviewPhotos: MutableList<ReviewPhoto> = mutableListOf(
                ReviewPhoto.create(100, "review-image1-url"),
                ReviewPhoto.create(200, "review-image2-url"),
                ReviewPhoto.create(300, "review-image3-url"),
            )
        ) = Review.create(
            lodgingCompanyId,
            roomId,
            userId,
            nickname,
            roomName,
            content,
            wholeScore,
            serviceScore,
            cleanlinessScore,
            convenienceScore,
            satisfactionScore,
            reviewPhotos,
        )
    }

}