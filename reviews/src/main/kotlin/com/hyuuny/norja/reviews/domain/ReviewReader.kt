package com.hyuuny.norja.reviews.domain

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface ReviewReader {

    fun getReview(id: Long): Review

    fun retrieveReview(searchQuery: ReviewSearchQuery, pageable: Pageable): PageImpl<SearchedReview>

    fun getReviewAverageScore(lodgingCompanyId: Long): SearchedReviewAverageScore

}