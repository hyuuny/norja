package com.hyuuny.norja.reviews.domain.collections

import com.hyuuny.norja.reviews.domain.Review
import com.hyuuny.norja.reviews.domain.ReviewListingResponseDto

data class SearchedReviews(
    val reviews: List<Review>,
) {

    fun toPage(): List<ReviewListingResponseDto> = this.reviews.stream()
        .map { ReviewListingResponseDto(it) }
        .toList()

}