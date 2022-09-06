package com.hyuuny.norja.reviews.infrastructure

import com.hyuuny.norja.jpa.support.CustomQueryDslRepository
import com.hyuuny.norja.reviews.domain.QReview.review
import com.hyuuny.norja.reviews.domain.Review
import com.hyuuny.norja.reviews.domain.ReviewAverageScoreResponseDto
import com.hyuuny.norja.reviews.domain.ReviewSearchQuery
import com.hyuuny.norja.reviews.domain.Type
import com.querydsl.core.types.Projections.fields
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.ObjectUtils.isEmpty

class ReviewRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomQueryDslRepository(Review::class.java), ReviewRepositoryCustom {

    override fun retrieveReview(
        searchQuery: ReviewSearchQuery,
        pageable: Pageable
    ): PageImpl<Review> = applyPageImpl(
        pageable, queryFactory
            .selectFrom(review)
            .where(
                lodgingCompanyIdEq(searchQuery.lodgingCompanyId),
                roomIdEq(searchQuery.roomId),
                userIdEq(searchQuery.userId),
                typeEq(searchQuery.type),
                wholeScoreEq(searchQuery.wholeScore),
                bestEq(searchQuery.best),
            )
    )

    override fun loadReviewAverageScore(lodgingCompanyId: Long): ReviewAverageScoreResponseDto {
        return queryFactory.select(
            fields(
                ReviewAverageScoreResponseDto::class.java,
                review.wholeScore.avg().`as`("wholeScore"),
                review.serviceScore.avg().`as`("serviceScore"),
                review.cleanlinessScore.avg().`as`("cleanlinessScore"),
                review.convenienceScore.avg().`as`("convenienceScore"),
                review.satisfactionScore.avg().`as`("satisfactionScore"),
            )
        )
            .from(review)
            .where(
                review.lodgingCompanyId.eq(lodgingCompanyId),
            )
            .fetchFirst()
    }

    private fun lodgingCompanyIdEq(lodgingCompanyId: Long?) =
        if (isEmpty(lodgingCompanyId)) null else review.lodgingCompanyId.eq(lodgingCompanyId)

    private fun roomIdEq(roomId: Long?) = if (isEmpty(roomId)) null else review.roomId.eq(roomId)

    private fun userIdEq(userId: Long?) = if (isEmpty(userId)) null else review.userId.eq(userId)

    private fun typeEq(type: Type?) = if (isEmpty(type)) null else review.type.eq(type)

    private fun wholeScoreEq(wholeScore: Int?) =
        if (isEmpty(wholeScore)) null else review.wholeScore.eq(wholeScore)

    private fun bestEq(best: Boolean?) = if (isEmpty(best)) null else review.best.eq(best)

}