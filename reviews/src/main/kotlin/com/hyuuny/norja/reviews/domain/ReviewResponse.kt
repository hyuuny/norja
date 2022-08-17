package com.hyuuny.norja.reviews.domain

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

@Relation(collectionRelation = "reviews")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReviewResponse(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "타입", example = "TEXT", required = true)
    val type: Type,

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long,

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long,

    @field:Schema(description = "회원 아이디", example = "1", required = true)
    val userId: Long,

    @field:Schema(description = "회원 닉네임", example = "김성현", required = true)
    val nickname: String,

    @field:Schema(description = "객실명", example = "더블룸", required = true)
    val roomName: String,

    @field:Schema(description = "내용", example = "방 너무 좋았어요 ㅎㅎ 잘 쉬고 갑니다!", required = true)
    val content: String,

    @field:Schema(description = "전체 점수", example = "5", required = true)
    val wholeScore: Int,

    @field:Schema(description = "서비스 점수", example = "5", required = true)
    val serviceScore: Int,

    @field:Schema(description = "청결도 점수", example = "5", required = true)
    val cleanlinessScore: Int,

    @field:Schema(description = "편의성 점수", example = "5", required = true)
    val convenienceScore: Int,

    @field:Schema(description = "만족도 점수", example = "5", required = true)
    val satisfactionScore: Int,

    @field:Schema(description = "베스트 여부")
    val best: Boolean,

    @field:Schema(description = "후기 이미지")
    val reviewPhotos: List<ImageResponse> = listOf(),

    @field:Schema(description = "등록일", example = "2022-08-16T13:51:00.797659")
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
            .map(::ImageResponse)
            .sorted((Comparator.comparing(ImageResponse::priority)))
            .toList(),
        createdAt = entity.createdAt,
    )
}

data class ImageResponse(
    val reviewId: Long,
    val priority: Long,
    val imageUrl: String,
) {
    constructor(entity: ReviewPhoto) : this(
        reviewId = entity.review?.id!!,
        priority = entity.priority!!,
        imageUrl = entity.imageUrl,
    )
}

data class ReviewAverageScoreResponse(
    val wholeScore: Double? = null,
    val serviceScore: Double? = null,
    val cleanlinessScore: Double? = null,
    val convenienceScore: Double? = null,
    val satisfactionScore: Double? = null,
)

@Relation(collectionRelation = "reviews")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReviewListingResponse(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "타입", example = "TEXT", required = true)
    val type: Type,

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long,

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long,

    @field:Schema(description = "회원 아이디", example = "1", required = true)
    val userId: Long,

    @field:Schema(description = "회원 닉네임", example = "김성현", required = true)
    val nickname: String,

    @field:Schema(description = "객실명", example = "더블룸", required = true)
    val roomName: String,

    @field:Schema(description = "내용", example = "방 너무 좋았어요 ㅎㅎ 잘 쉬고 갑니다!", required = true)
    val content: String,

    @field:Schema(description = "전체 점수", example = "5", required = true)
    val wholeScore: Int,

    @field:Schema(description = "베스트 여부")
    val best: Boolean,

    @field:Schema(description = "후기 이미지")
    val reviewPhotos: List<ImageListingResponse> = listOf(),

    @field:Schema(description = "등록일", example = "2022-08-16T13:51:00.797659")
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
        wholeScore = entity.wholeScore,
        content = entity.content,
        best = entity.best,
        reviewPhotos = entity.reviewPhotos!!.stream()
            .map(::ImageListingResponse)
            .sorted((Comparator.comparing(ImageListingResponse::priority)))
            .toList(),
        createdAt = entity.createdAt,
    )
}

data class ImageListingResponse(
    val reviewId: Long,
    val priority: Long,
    val imageUrl: String,
) {
    constructor(entity: ReviewPhoto) : this(
        reviewId = entity.review?.id!!,
        priority = entity.priority!!,
        imageUrl = entity.imageUrl,
    )
}
