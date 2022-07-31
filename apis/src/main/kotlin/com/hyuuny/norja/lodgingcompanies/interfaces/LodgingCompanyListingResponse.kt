package com.hyuuny.norja.lodgingcompanies.interfaces

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyListingInfo
import com.hyuuny.norja.lodgingcompanies.domain.Status
import com.hyuuny.norja.lodgingcompanies.domain.Type
import java.time.LocalDateTime

@JsonInclude(Include.NON_NULL)
data class LodgingCompanyListingResponse(
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
    constructor(info: LodgingCompanyListingInfo) : this(
        id = info.id,
        type = info.type,
        status = info.status,
        name = info.name,
        thumbnail = info.thumbnail,
        address = info.address,
        price = info.price,
        searchTag = info.searchTag,
        createdAt = info.createdAt,
    )
}
