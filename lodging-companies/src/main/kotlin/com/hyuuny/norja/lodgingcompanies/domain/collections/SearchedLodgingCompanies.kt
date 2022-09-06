package com.hyuuny.norja.lodgingcompanies.domain.collections

import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyListingResponseDto
import com.hyuuny.norja.lodgingcompanies.domain.SearchedLodgingCompanyListing

data class SearchedLodgingCompanies(
    val lodgingCompanyListings: List<SearchedLodgingCompanyListing>,
) {
    fun toPage() = this.lodgingCompanyListings.stream()
        .map(::LodgingCompanyListingResponseDto)
        .toList()
}
