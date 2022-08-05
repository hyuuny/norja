package com.hyuuny.norja.lodgingcompanies.domain

import java.time.LocalDate

data class LodgingCompanySearchQuery(
    val checkIn: LocalDate? = null,
    val checkOut: LocalDate? = null,
    val searchTag: String? = null,
    val address: String? = null,
)