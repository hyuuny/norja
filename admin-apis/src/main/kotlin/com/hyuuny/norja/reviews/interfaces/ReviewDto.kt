package com.hyuuny.norja.reviews.interfaces

import com.hyuuny.norja.reviews.domain.BestReviewCommand
import com.hyuuny.norja.reviews.domain.ChangeBestReviewCommand
import io.swagger.v3.oas.annotations.media.Schema

class ReviewDto

data class ChangeBestReviewDto(

    @field:Schema(description = "베스트 후기 선정/해제 Ids", required = true)
    val reviews: MutableList<BestReviewDto>,
) {
    fun toCommand(): ChangeBestReviewCommand {
        return ChangeBestReviewCommand(
            reviews = this.reviews.stream()
                .map(BestReviewDto::toCommand)
                .toList()
        )
    }
}

data class BestReviewDto(

    @field:Schema(description = "후기 아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "베스트 여부", required = true)
    val best: Boolean,
) {
    fun toCommand() = BestReviewCommand(id = this.id, best = this.best)
}