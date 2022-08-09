package com.hyuuny.norja.lodgingcompanies.interfaces

import com.hyuuny.norja.lodgingcompanies.domain.DateSearchQuery

data class SearchQuery(
    val checkIn: String,
    val checkOut: String,
) {
    fun toDateSearchQuery() = DateSearchQuery(checkIn = this.checkIn, checkOut = this.checkOut)
}
