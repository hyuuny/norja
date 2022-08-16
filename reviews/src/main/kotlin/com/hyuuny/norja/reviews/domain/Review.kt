package com.hyuuny.norja.reviews.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import com.hyuuny.norja.reviews.domain.Type.PHOTO
import com.hyuuny.norja.reviews.domain.Type.TEXT
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.Lob
import javax.persistence.OneToMany

@Entity
class Review private constructor(
    type: Type = TEXT,
    val lodgingCompanyId: Long,
    val roomId: Long,
    val userId: Long,
    val nickname: String,
    val roomName: String,
    val wholeScore: Int,
    val serviceScore: Int,
    val cleanlinessScore: Int,
    val convenienceScore: Int,
    val satisfactionScore: Int,
    @Lob val content: String,
    best: Boolean = false,
    reviewPhotos: MutableList<ReviewPhoto>? = mutableListOf(),
) : BaseEntity() {
    companion object {
        fun create(
            lodgingCompanyId: Long,
            roomId: Long,
            userId: Long,
            nickname: String,
            roomName: String,
            content: String,
            wholeScore: Int,
            serviceScore: Int,
            cleanlinessScore: Int,
            convenienceScore: Int,
            satisfactionScore: Int,
            reviewPhotos: MutableList<ReviewPhoto>? = mutableListOf(),
        ) = Review(
            lodgingCompanyId = lodgingCompanyId,
            roomId = roomId,
            userId = userId,
            nickname = nickname,
            roomName = roomName,
            content = content,
            wholeScore = wholeScore,
            serviceScore = serviceScore,
            cleanlinessScore = cleanlinessScore,
            convenienceScore = convenienceScore,
            satisfactionScore = satisfactionScore,
            reviewPhotos = reviewPhotos,
        )
    }

    var type = type
        private set

    @OneToMany(mappedBy = "review", cascade = [ALL], fetch = LAZY, orphanRemoval = true)
    var reviewPhotos = reviewPhotos
        private set

    var best = best
        private set

    fun create() {
        this.type = if (this.reviewPhotos!!.isNotEmpty()) PHOTO else TEXT
    }

    fun addPhoto(reviewPhoto: ReviewPhoto) = reviewPhoto.assignReview(this)

    fun changeBestReview(best: Boolean) {
        this.best = best
    }

}