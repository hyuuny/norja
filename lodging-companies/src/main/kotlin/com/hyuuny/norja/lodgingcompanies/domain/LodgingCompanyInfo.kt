package com.hyuuny.norja.lodgingcompanies.domain

import com.hyuuny.norja.address.domain.Address
import java.time.LocalDateTime
import kotlin.streams.toList

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
    val createdAt: LocalDateTime,
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
            .toList(),
        createdAt = entity.createdAt,
    )

}

data class ImageInfo(
    val lodgingCompanyId: Long,
    val priority: Long?,
    val imageUrl: String,
) {

    constructor(entity: Image) : this(
        lodgingCompanyId = entity.lodgingCompany?.id!!,
        priority = entity.priority,
        imageUrl = entity.imageUrl
    )

}

data class FacilitiesInfo(
    val lodgingCompanyId: Long,
    val name: String,
    val priority: Long?,
    val iconImageUrl: String?,
) {

    constructor(entity: Facilities) : this(
        lodgingCompanyId = entity.lodgingCompany?.id!!,
        name = entity.name,
        priority = entity.priority,
        iconImageUrl = entity.iconImageUrl,
    )

}