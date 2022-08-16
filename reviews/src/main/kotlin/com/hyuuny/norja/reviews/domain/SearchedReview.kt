package com.hyuuny.norja.reviews.domain

data class SearchedReview(
    val review: Review? = null,
) {
    val id = review!!.id!!

    val type = review!!.type

    val lodgingCompanyId = review!!.lodgingCompanyId

    val roomId = review!!.roomId

    val userId = review!!.userId

    val nickname = review!!.nickname

    val roomName = review!!.roomName

    val wholeScore = review!!.wholeScore

    val content = review!!.content

    val best = review!!.best

    val reviewPhotos = review!!.reviewPhotos

    val createdAt = review!!.createdAt
}
