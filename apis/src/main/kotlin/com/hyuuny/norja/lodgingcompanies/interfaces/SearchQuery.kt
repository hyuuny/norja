package com.hyuuny.norja.lodgingcompanies.interfaces

import com.hyuuny.norja.lodgingcompanies.domain.DateSearchQuery
import java.time.LocalDate

data class SearchQuery(
    val checkIn: LocalDate,
    val checkOut: LocalDate,
) {
    fun toDateSearchQuery() = DateSearchQuery(checkIn = this.checkIn, checkOut = this.checkOut)
}
