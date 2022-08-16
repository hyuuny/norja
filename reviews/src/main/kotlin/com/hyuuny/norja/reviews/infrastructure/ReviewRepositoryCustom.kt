package com.hyuuny.norja.reviews.infrastructure

import com.hyuuny.norja.reviews.domain.ReviewSearchQuery
import com.hyuuny.norja.reviews.domain.SearchedReview
import com.hyuuny.norja.reviews.domain.SearchedReviewAverageScore
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface ReviewRepositoryCustom {

    fun retrieveReview(searchQuery: ReviewSearchQuery, pageable: Pageable): PageImpl<SearchedReview>

    fun loadReviewAverageScore(lodgingCompanyId: Long): SearchedReviewAverageScore

}