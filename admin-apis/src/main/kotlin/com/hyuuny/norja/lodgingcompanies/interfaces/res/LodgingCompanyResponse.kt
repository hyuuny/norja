package com.hyuuny.norja.lodgingcompanies.interfaces.res

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.domain.Status
import com.hyuuny.norja.domain.Type
import com.hyuuny.norja.domain.info.LodgingCompanyInfo

@JsonInclude(Include.NON_NULL)
data class LodgingCompanyResponse(
    val id: Long,
    val type: Type,
    val status: Status,
    val name: String,
    val thumbnail: String,
    val businessNumber: String,
    val tellNumber: String,
    val address: Address,
    val searchTag: String? = null,
    val images: List<ImageResponse>? = listOf(),
    val facilities: List<FacilitiesResponse>? = listOf(),
) {

    constructor(info: LodgingCompanyInfo) : this(
        id = info.id,
        type = info.type,
        status = info.status,
        name = info.name,
        thumbnail = info.thumbnail,
        businessNumber = info.businessNumber,
        tellNumber = info.tellNumber,
        address = info.address,
        searchTag = info.searchTag,
        images = info.images.stream()
            .map(::ImageResponse)
            .sorted((Comparator.comparing(ImageResponse::priority)))
            .toList(),
        facilities = info.facilities.stream()
            .map(::FacilitiesResponse)
            .sorted((Comparator.comparing(FacilitiesResponse::priority)))
            .toList()
    )

}