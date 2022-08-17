package com.hyuuny.norja.reviews.interfaces

import com.hyuuny.norja.reviews.domain.ReviewCreateCommand
import io.swagger.v3.oas.annotations.media.Schema

class ReviewDto

data class ReviewCreateDto(

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

    @field:Schema(description = "내용", example = "방 너무 좋았어요 ㅎㅎ 잘 쉬고 갑니다!", required = true)
    val content: String,
) {
    fun toCommand() = ReviewCreateCommand(
        lodgingCompanyId = this.lodgingCompanyId,
        roomId = this.roomId,
        userId = this.userId,
        nickname = this.nickname,
        roomName = this.roomName,
        wholeScore = this.wholeScore,
        serviceScore = this.serviceScore,
        cleanlinessScore = this.cleanlinessScore,
        convenienceScore = this.convenienceScore,
        satisfactionScore = this.satisfactionScore,
        content = this.content,
    )
}