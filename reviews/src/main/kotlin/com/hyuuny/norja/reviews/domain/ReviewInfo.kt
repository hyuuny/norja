package com.hyuuny.norja.reviews.domain

import java.time.LocalDateTime

class ReviewInfo(
    val id: Long,
    val type: Type,
    val lodgingCompanyId: Long,
    val roomId: Long,
    val userId: Long,
    val nickname: String,
    val roomName: String,
    val content: String,
    val wholeScore: Int,
    val serviceScore: Int,
    val cleanlinessScore: Int,
    val convenienceScore: Int,
    val satisfactionScore: Int,
    val best: Boolean,
    val reviewPhotos: List<ReviewPhotoInfo> = listOf(),
    val createdAt: LocalDateTime,
) {
    constructor(entity: Review) : this(
        id = entity.id!!,
        type = entity.type,
        lodgingCompanyId = entity.lodgingCompanyId,
        roomId = entity.roomId,
        userId = entity.userId,
        nickname = entity.nickname,
        roomName = entity.roomName,
        content = entity.content,
        wholeScore = entity.wholeScore,
        serviceScore = entity.serviceScore,
        cleanlinessScore = entity.cleanlinessScore,
        convenienceScore = entity.convenienceScore,
        satisfactionScore = entity.satisfactionScore,
        best = entity.best,
        reviewPhotos = entity.reviewPhotos!!.stream()
            .map(::ReviewPhotoInfo)
            .toList(),
        createdAt = entity.createdAt,
    )
}

data class ReviewPhotoInfo(
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

data class ReviewAverageScoreInfo(
    val wholeScore: Double,
    val serviceScore: Double,
    val cleanlinessScore: Double,
    val convenienceScore: Double,
    val satisfactionScore: Double,
) {
    constructor(searched: SearchedReviewAverageScore) : this(
        wholeScore = searched.wholeScore!!,
        serviceScore = searched.serviceScore!!,
        cleanlinessScore = searched.cleanlinessScore!!,
        convenienceScore = searched.convenienceScore!!,
        satisfactionScore = searched.satisfactionScore!!,
    )
}
