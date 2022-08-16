package com.hyuuny.norja.reviews.domain

import java.time.LocalDateTime

data class ReviewListingInfo(
    val id: Long,
    val type: Type,
    val lodgingCompanyId: Long,
    val roomId: Long,
    val userId: Long,
    val nickname: String,
    val roomName: String,
    val wholeScore: Int,
    val content: String,
    val best: Boolean = false,
    val reviewPhotos: List<ReviewPhotoListingInfo> = listOf(),
    val createdAt: LocalDateTime,
) {

    constructor(searched: SearchedReview) : this(
        id = searched.id,
        type = searched.type,
        lodgingCompanyId = searched.lodgingCompanyId,
        roomId = searched.roomId,
        userId = searched.userId,
        nickname = searched.nickname,
        roomName = searched.roomName,
        wholeScore = searched.wholeScore,
        content = searched.content,
        best = searched.best,
        reviewPhotos = searched.reviewPhotos!!.stream()
            .map(::ReviewPhotoListingInfo)
            .toList(),
        createdAt = searched.createdAt,
    )

}

data class ReviewPhotoListingInfo(
    val reviewId: Long,
    val priority: Long?,
    val imageUrl: String,
) {
    constructor(entity: ReviewPhoto) : this(
        reviewId = entity.review?.id!!,
        priority = entity.priority,
        imageUrl = entity.imageUrl,
    )
}

