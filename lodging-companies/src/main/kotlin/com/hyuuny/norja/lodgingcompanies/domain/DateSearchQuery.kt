package com.hyuuny.norja.lodgingcompanies.domain

import java.time.LocalDate

data class DateSearchQuery(
    val checkIn: LocalDate,
    val checkOut: LocalDate,
)
