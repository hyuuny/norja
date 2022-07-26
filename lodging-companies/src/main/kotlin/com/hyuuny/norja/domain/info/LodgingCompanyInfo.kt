package com.hyuuny.norja.domain.info

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.domain.LodgingCompany
import com.hyuuny.norja.domain.Status
import com.hyuuny.norja.domain.Type

data class LodgingCompanyInfo(
    val id: Long,
    val type: Type,
    val name: String,
    val status: Status,
    val thumbnail: String,
    val businessNumber: String,
    val tellNumber: String,
    val address: Address,
    val searchTag: String? = null,
    val images: List<ImageInfo> = listOf(),
    val facilities: List<FacilitiesInfo> = listOf(),
) {

    constructor(entity: LodgingCompany) : this(
        id = entity.id!!,
        type = entity.type,
        name = entity.name,
        status = entity.status,
        thumbnail = entity.thumbnail,
        businessNumber = entity.businessNumber,
        tellNumber = entity.tellNumber,
        address = entity.address,
        searchTag = entity.searchTag,
        images = entity.images!!.stream()
            .map(::ImageInfo)
            .toList(),
        facilities = entity.facilities!!.stream()
            .map(::FacilitiesInfo)
            .toList()
    )

}