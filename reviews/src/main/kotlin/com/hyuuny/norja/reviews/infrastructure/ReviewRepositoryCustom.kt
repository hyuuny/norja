package com.hyuuny.norja.reviews.infrastructure

import com.hyuuny.norja.reviews.domain.Review
import com.hyuuny.norja.reviews.domain.ReviewAverageScoreResponseDto
import com.hyuuny.norja.reviews.domain.ReviewSearchQuery
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface ReviewRepositoryCustom {

    fun retrieveReview(searchQuery: ReviewSearchQuery, pageable: Pageable): PageImpl<Review>

    fun loadReviewAverageScore(lodgingCompanyId: Long): ReviewAverageScoreResponseDto

}