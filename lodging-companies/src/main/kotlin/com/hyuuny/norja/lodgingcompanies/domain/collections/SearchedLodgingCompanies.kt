package com.hyuuny.norja.lodgingcompanies.domain.collections

import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyListingInfo
import com.hyuuny.norja.lodgingcompanies.domain.SearchedLodgingCompanyListing
import kotlin.streams.toList

data class SearchedLodgingCompanies(
    val lodgingCompanyListings: List<SearchedLodgingCompanyListing>,
) {
    fun toPage() = this.lodgingCompanyListings.stream()
        .map(::LodgingCompanyListingInfo)
        .toList()
}
