package com.hyuuny.norja.lodgingcompanies.domain

import com.hyuuny.norja.address.domain.Address
import java.time.LocalDateTime

data class SearchedLodgingCompanyListing(
    val id: Long? = null,
    val type: Type = Type.HOTEL,
    val status: Status = Status.OPEN,
    val name: String? = null,
    val thumbnail: String? = null,
    val address: Address? = null,
    val price: Long? = 0,
    val searchTag: String? = null,
    val reviewAverageScore: Double? = null,
    val reviewCount: Long? = null,
    val createdAt: LocalDateTime? = null,
)