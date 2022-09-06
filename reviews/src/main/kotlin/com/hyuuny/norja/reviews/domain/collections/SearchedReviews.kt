package com.hyuuny.norja.reviews.domain.collections

import com.hyuuny.norja.reviews.domain.Review
import com.hyuuny.norja.reviews.domain.ReviewListingResponseDto

data class SearchedReviews(
    val reviews: List<Review>,
) {

    fun toPage() = this.reviews.stream()
        .map(::ReviewListingResponseDto)
        .toList()

}