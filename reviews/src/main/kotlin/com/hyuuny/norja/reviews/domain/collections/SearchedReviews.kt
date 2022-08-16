package com.hyuuny.norja.reviews.domain.collections

import com.hyuuny.norja.reviews.domain.ReviewListingInfo
import com.hyuuny.norja.reviews.domain.SearchedReview

data class SearchedReviews(
    val reviews: List<SearchedReview>,
) {

    fun toPage() = this.reviews.stream()
        .map(::ReviewListingInfo)
        .toList()

}