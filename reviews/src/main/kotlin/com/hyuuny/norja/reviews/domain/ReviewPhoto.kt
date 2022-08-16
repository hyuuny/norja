package com.hyuuny.norja.reviews.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne

@Entity
class ReviewPhoto private constructor(
    review: Review? = null,
    val priority: Long?,
    val imageUrl: String,
) : BaseEntity() {
    companion object {
        fun create(priority: Long? = 100, imageUrl: String) = ReviewPhoto(
            priority = priority,
            imageUrl = imageUrl,
        )
    }

    @ManyToOne(optional = true, fetch = LAZY)
    var review = review
        private set

    fun assignReview(review: Review) {
        this.review?.let {
            this.review?.reviewPhotos?.remove(this)
        }
        this.review = review
        this.review?.reviewPhotos?.add(this)
    }


}