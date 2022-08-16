package com.hyuuny.norja.reviews.domain

data class ReviewSearchQuery(
    val lodgingCompanyId: Long? = null,
    val roomId: Long? = null,
    val userId: Long? = null,
    val type: Type? = null,
    val wholeScore: Int? = null,
    val best: Boolean? = null,
)
