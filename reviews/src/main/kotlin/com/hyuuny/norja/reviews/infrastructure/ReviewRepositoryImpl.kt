package com.hyuuny.norja.reviews.infrastructure

import com.hyuuny.norja.jpa.support.CustomQueryDslRepository
import com.hyuuny.norja.reviews.domain.*
import com.hyuuny.norja.reviews.domain.QReview.review
import com.querydsl.core.types.ExpressionUtils
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
    ): PageImpl<SearchedReview> {
        return applyPageImpl(
            pageable, queryFactory.select(
                fields(
                    SearchedReview::class.java,
                    ExpressionUtils.`as`(review, "review"),
                )

            )
                .from(review)
                .where(
                    lodgingCompanyIdEq(searchQuery.lodgingCompanyId),
                    roomIdEq(searchQuery.roomId),
                    userIdEq(searchQuery.userId),
                    typeEq(searchQuery.type),
                    wholeScoreEq(searchQuery.wholeScore),
                    bestEq(searchQuery.best),
                )
        )
    }

    override fun loadReviewAverageScore(lodgingCompanyId: Long): SearchedReviewAverageScore {
        return queryFactory.select(
            fields(
                SearchedReviewAverageScore::class.java,
                review.wholeScore.avg(),
                review.serviceScore.avg(),
                review.cleanlinessScore.avg(),
                review.convenienceScore.avg(),
                review.satisfactionScore.avg(),
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

    private fun userIdEq(userId: Long?) = if (isEmpty(userId)) null else review.roomId.eq(userId)

    private fun typeEq(type: Type?) = if (isEmpty(type)) null else review.type.eq(type)

    private fun wholeScoreEq(wholeScore: Int?) =
        if (isEmpty(wholeScore)) null else review.wholeScore.eq(wholeScore)

    private fun bestEq(best: Boolean?) = if (isEmpty(best)) null else review.best.eq(best)

}