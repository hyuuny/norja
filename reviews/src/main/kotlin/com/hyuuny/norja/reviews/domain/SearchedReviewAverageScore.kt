package com.hyuuny.norja.reviews.domain

data class SearchedReviewAverageScore(
    val wholeScore: Double? = null,
    val serviceScore: Double? = null,
    val cleanlinessScore: Double? = null,
    val convenienceScore: Double? = null,
    val satisfactionScore: Double? = null,
)
