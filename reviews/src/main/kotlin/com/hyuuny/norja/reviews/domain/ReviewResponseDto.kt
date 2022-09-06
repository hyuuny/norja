package com.hyuuny.norja.reviews.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@class")
@Relation(collectionRelation = "reviews")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReviewResponseDto(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long = 0,

    @field:Schema(description = "타입", example = "TEXT", required = true)
    val type: Type = Type.TEXT,

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long = 0,

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long = 0,

    @field:Schema(description = "회원 아이디", example = "1", required = true)
    val userId: Long = 0,

    @field:Schema(description = "회원 닉네임", example = "김성현", required = true)
    val nickname: String = "",

    @field:Schema(description = "객실명", example = "더블룸", required = true)
    val roomName: String = "",

    @field:Schema(description = "내용", example = "방 너무 좋았어요 ㅎㅎ 잘 쉬고 갑니다!", required = true)
    val content: String = "",

    @field:Schema(description = "전체 점수", example = "5", required = true)
    val wholeScore: Int = 5,

    @field:Schema(description = "서비스 점수", example = "5", required = true)
    val serviceScore: Int = 5,

    @field:Schema(description = "청결도 점수", example = "5", required = true)
    val cleanlinessScore: Int = 5,

    @field:Schema(description = "편의성 점수", example = "5", required = true)
    val convenienceScore: Int = 5,

    @field:Schema(description = "만족도 점수", example = "5", required = true)
    val satisfactionScore: Int = 5,

    @field:Schema(description = "베스트 여부")
    val best: Boolean = false,

    @field:Schema(description = "후기 이미지")
    val reviewPhotos: List<ReviewPhotoResponseDto> = listOf(),

    @field:JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @field:JsonSerialize(using = LocalDateTimeSerializer::class)
    @field:JsonFormat(
        shape = Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        timezone = "Asia/Seoul"
    )
    @field:Schema(description = "등록일", example = "2022-08-16T13:51:00.797659")
    val createdAt: LocalDateTime = LocalDateTime.now(),
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
            .map(::ReviewPhotoResponseDto)
            .sorted((Comparator.comparing(ReviewPhotoResponseDto::priority)))
            .toList(),
        createdAt = entity.createdAt,
    )
}

data class ReviewPhotoResponseDto(

    @field:Schema(description = "후기 아이디", example = "1", required = true)
    val reviewId: Long = 0,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long = 100,

    @field:Schema(description = "이미지 URL", example = "image-url", required = true)
    val imageUrl: String = "",
) {
    constructor(entity: ReviewPhoto) : this(
        reviewId = entity.review?.id!!,
        priority = entity.priority!!,
        imageUrl = entity.imageUrl,
    )
}

data class ReviewAverageScoreResponseDto(

    @field:Schema(description = "전체 점수", example = "5")
    val wholeScore: Double? = null,

    @field:Schema(description = "서비스 점수", example = "5")
    val serviceScore: Double? = null,

    @field:Schema(description = "청결도 점수", example = "5")
    val cleanlinessScore: Double? = null,

    @field:Schema(description = "편의성 점수", example = "5")
    val convenienceScore: Double? = null,

    @field:Schema(description = "만족도 점수", example = "5")
    val satisfactionScore: Double? = null,
)

@Relation(collectionRelation = "reviews")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ReviewListingResponseDto(

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
    val reviewPhotos: List<ReviewPhotoListingResponseDto> = listOf(),

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
            .map(::ReviewPhotoListingResponseDto)
            .sorted((Comparator.comparing(ReviewPhotoListingResponseDto::priority)))
            .toList(),
        createdAt = entity.createdAt,
    )
}

data class ReviewPhotoListingResponseDto(
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
