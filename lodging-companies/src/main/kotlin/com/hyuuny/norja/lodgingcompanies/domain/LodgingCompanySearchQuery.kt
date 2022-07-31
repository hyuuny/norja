package com.hyuuny.norja.lodgingcompanies.domain

data class LodgingCompanySearchQuery(
    val searchTag: String? = null,
    val address: String? = null,
)