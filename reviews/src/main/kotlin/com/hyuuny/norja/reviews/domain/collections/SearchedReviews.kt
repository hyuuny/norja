package com.hyuuny.norja.reviews.domain.collections

import com.hyuuny.norja.reviews.domain.Review
import com.hyuuny.norja.reviews.domain.ReviewListingResponse

data class SearchedReviews(
    val reviews: List<Review>,
) {

    fun toPage() = this.reviews.stream()
        .map(::ReviewListingResponse)
        .toList()

}