package com.hyuuny.norja.reservations.domain.collections

import com.hyuuny.norja.reservations.domain.ReservationListingInfo
import com.hyuuny.norja.reservations.domain.SearchedReservationListing

data class SearchedReservations(
    val reservations: List<SearchedReservationListing>,
) {
    fun toPage() = this.reservations.stream()
        .map(::ReservationListingInfo)
        .toList()
}
