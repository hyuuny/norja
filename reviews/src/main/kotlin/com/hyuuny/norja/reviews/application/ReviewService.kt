package com.hyuuny.norja.reviews.application

import com.hyuuny.norja.reviews.domain.*
import com.hyuuny.norja.reviews.domain.collections.SearchedReviews
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class ReviewService(
    private val reviewStore: ReviewStore,
    private val reviewReader: ReviewReader,
) {

    @Transactional
    fun createReview(command: ReviewCreateCommand): Long {
        val newReview = command.toEntity
        newReview.create()
        return reviewStore.store(newReview).id!!
    }

    fun retrieveReview(
        searchQuery: ReviewSearchQuery,
        pageable: Pageable
    ): PageImpl<ReviewListingResponse> {
        val searched = reviewReader.retrieveReview(searchQuery, pageable)
        val searchedReviews = SearchedReviews(searched.content)
        return PageImpl(searchedReviews.toPage(), pageable, searched.totalElements)
    }

    fun getAverageScore(lodgingCompanyId: Long): ReviewAverageScoreResponse =
        reviewReader.getReviewAverageScore(lodgingCompanyId)

    fun getReview(id: Long): ReviewResponse {
        val loadedReview = reviewReader.getReview(id)
        return ReviewResponse(loadedReview)
    }

    @Transactional
    fun changeBestReview(command: ChangeBestReviewCommand) = command.reviews.forEach {
        run {
            val loadedReview = reviewReader.getReview(it.id)
            loadedReview.changeBestReview(it.best)
        }
    }

    @Transactional
    fun deleteReview(id: Long) {
        val loadedReview = reviewReader.getReview(id)
        reviewStore.delete(loadedReview.id!!)
    }

}