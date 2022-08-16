package com.hyuuny.norja.reviews.domain

interface ReviewStore {

    fun store(review: Review): Review

    fun delete(id: Long)

}