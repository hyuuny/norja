package com.hyuuny.norja.domain.info

import com.hyuuny.norja.domain.Room
import com.hyuuny.norja.domain.Type

data class RoomInfo(
    val id: Long,
    val lodgingCompanyId: Long,
    val type: Type,
    val name: String,
    val standardPersonnel: Int = 2,
    val maximumPersonnel: Int = 2,
    val price: Long,
    val content: String? = null,
    val images: List<ImageInfo>? = listOf(),
    val facilities: List<FacilitiesInfo>? = listOf(),
) {
    constructor(entity: Room) : this(
        id = entity.id!!,
        lodgingCompanyId = entity.lodgingCompanyId,
        type = entity.type,
        name = entity.name,
        standardPersonnel = entity.standardPersonnel,
        maximumPersonnel = entity.maximumPersonnel,
        price = entity.price,
        content = entity.content,
        images = entity.images!!.stream()
            .map(::ImageInfo)
            .toList(),
        facilities = entity.facilities!!.stream()
            .map(::FacilitiesInfo)
            .toList()
    )
}
