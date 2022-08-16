package com.hyuuny.norja.reviews.infrastructure

import com.hyuuny.norja.reviews.domain.ReviewReader
import com.hyuuny.norja.reviews.domain.ReviewSearchQuery
import com.hyuuny.norja.reviews.domain.SearchedReview
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ReviewReaderImpl(
    private val reviewRepository: ReviewRepository,
) : ReviewReader {

    override fun getReview(id: Long) = reviewRepository.findByIdOrNull(id)
        ?: throw HttpStatusMessageException(HttpStatus.BAD_REQUEST, "review.id.notFound", id)

    override fun retrieveReview(
        searchQuery: ReviewSearchQuery,
        pageable: Pageable
    ): PageImpl<SearchedReview> = reviewRepository.retrieveReview(searchQuery, pageable)

    override fun getReviewAverageScore(lodgingCompanyId: Long) =
        reviewRepository.loadReviewAverageScore(lodgingCompanyId)

}