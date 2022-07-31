package com.hyuuny.norja.rooms.interfaces

import com.fasterxml.jackson.annotation.JsonInclude
import com.hyuuny.norja.rooms.domain.RoomFacilitiesInfo
import com.hyuuny.norja.rooms.domain.RoomImageInfo
import com.hyuuny.norja.rooms.domain.RoomInfo
import com.hyuuny.norja.rooms.domain.Type
import kotlin.streams.toList

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RoomResponse(
    val id: Long,
    val lodgingCompanyId: Long,
    val type: Type,
    val name: String,
    val standardPersonnel: Int = 2,
    val maximumPersonnel: Int = 2,
    val price: Long,
    val content: String? = null,
    val roomImage: List<RoomImageResponse> = listOf(),
    val roomFacilities: List<RoomFacilitiesResponse> = listOf(),
) {
    constructor(info: RoomInfo) : this(
        id = info.id,
        lodgingCompanyId = info.lodgingCompanyId,
        type = info.type,
        name = info.name,
        standardPersonnel = info.standardPersonnel,
        maximumPersonnel = info.maximumPersonnel,
        price = info.price,
        content = info.content,
        roomImage = info.roomImages.stream()
            .map(::RoomImageResponse)
            .sorted((Comparator.comparing(RoomImageResponse::priority)))
            .toList(),
        roomFacilities = info.roomFacilities.stream()
            .map(::RoomFacilitiesResponse)
            .sorted((Comparator.comparing(RoomFacilitiesResponse::priority)))
            .toList(),
    )
}

data class RoomImageResponse(
    val roomId: Long,
    val priority: Long,
    val imageUrl: String,
) {
    constructor(info: RoomImageInfo) : this(
        roomId = info.roomId,
        priority = info.priority!!,
        imageUrl = info.imageUrl,
    )
}

data class RoomFacilitiesResponse(
    val roomId: Long,
    val name: String,
    val priority: Long,
    val iconImageUrl: String,
) {
    constructor(info: RoomFacilitiesInfo) : this(
        roomId = info.roomId,
        name = info.name,
        priority = info.priority!!,
        iconImageUrl = info.iconImageUrl,
    )
}
