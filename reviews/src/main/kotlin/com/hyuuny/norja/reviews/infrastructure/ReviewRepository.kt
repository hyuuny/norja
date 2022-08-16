package com.hyuuny.norja.reviews.infrastructure

import com.hyuuny.norja.reviews.domain.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long>, ReviewRepositoryCustom {
}