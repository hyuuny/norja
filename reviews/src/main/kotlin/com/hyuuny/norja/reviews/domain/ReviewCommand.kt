package com.hyuuny.norja.reviews.domain

class ReviewCommand

data class ReviewCreateCommand(
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
    val reviewPhotos: MutableList<ReviewPhotoCreateCommand> = mutableListOf(),
) {
    val toEntity: Review
        get() {
            val newReview = Review.create(
                lodgingCompanyId = this.lodgingCompanyId,
                roomId = roomId,
                userId = this.userId,
                nickname = this.nickname,
                roomName = this.roomName,
                content = this.content,
                wholeScore = this.wholeScore,
                serviceScore = this.serviceScore,
                cleanlinessScore = this.cleanlinessScore,
                convenienceScore = this.convenienceScore,
                satisfactionScore = this.satisfactionScore,
            )

            this.reviewPhotos.stream()
                .map(ReviewPhotoCreateCommand::toEntity)
                .forEach(newReview::addPhoto)

            return newReview
        }
}

data class ReviewPhotoCreateCommand(
    val priority: Long?,
    val imageUrl: String,
) {
    val toEntity = ReviewPhoto.create(priority = this.priority, imageUrl = this.imageUrl)
}

data class ChangeBestReviewCommand(
    val reviews: MutableList<BestReviewCommand>
)

data class BestReviewCommand(
    val id: Long,
    val best: Boolean,
)