package com.hyuuny.norja.lodgingcompanies.domain

import com.hyuuny.norja.address.domain.Address
import java.time.LocalDateTime

data class LodgingCompanyListingInfo(
    val id: Long,
    val type: Type,
    val status: Status,
    val name: String,
    val thumbnail: String,
    val address: Address,
    val price: Long,
    val searchTag: String,
    val createdAt: LocalDateTime,
) {
    constructor(searched: SearchedLodgingCompanyListing) : this(
        id = searched.id!!,
        type = searched.type,
        status = searched.status,
        name = searched.name!!,
        thumbnail = searched.thumbnail!!,
        address = searched.address!!,
        price = searched.price!!,
        searchTag = searched.searchTag!!,
        createdAt = searched.createdAt!!,
    )
}