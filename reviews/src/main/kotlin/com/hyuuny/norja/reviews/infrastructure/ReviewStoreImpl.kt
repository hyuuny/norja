package com.hyuuny.norja.reviews.infrastructure

import com.hyuuny.norja.reviews.domain.Review
import com.hyuuny.norja.reviews.domain.ReviewStore
import org.springframework.stereotype.Component

@Component
class ReviewStoreImpl(
    private val reviewRepository: ReviewRepository,
) : ReviewStore {

    override fun store(review: Review) = reviewRepository.save(review)

    override fun delete(id: Long) = reviewRepository.deleteById(id)
}