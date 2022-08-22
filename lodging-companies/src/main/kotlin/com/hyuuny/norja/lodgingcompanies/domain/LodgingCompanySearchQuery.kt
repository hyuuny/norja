package com.hyuuny.norja.lodgingcompanies.domain

data class LodgingCompanySearchQuery(
    val checkIn: String? = null,
    val checkOut: String? = null,
    val categoryId: Long? = null,
    val type: Type? = null,
    val searchTag: String? = null,
    val address: String? = null,
)