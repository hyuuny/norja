package com.hyuuny.norja.rooms.interfaces

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.rooms.domain.RoomFacilitiesInfo
import com.hyuuny.norja.rooms.domain.RoomImageInfo
import com.hyuuny.norja.rooms.domain.RoomInfo
import com.hyuuny.norja.rooms.domain.Type

@JsonInclude(Include.NON_NULL)
data class RoomResponse(
    val id: Long,
    val lodgingCompanyId: Long,
    val type: Type,
    val name: String,
    val roomCount: Int = 10,
    val remainingRoomCount: Long? = null,
    val standardPersonnel: Int = 2,
    val maximumPersonnel: Int = 2,
    val price: Long,
    val content: String? = null,
    val roomImages: List<RoomImageResponse>? = listOf(),
    val roomFacilities: List<RoomFacilitiesResponse>? = listOf(),
) {
    constructor(info: RoomInfo) : this(
        id = info.id,
        lodgingCompanyId = info.lodgingCompanyId,
        type = info.type,
        name = info.name,
        roomCount = info.roomCount,
        remainingRoomCount = info.remainingRoomCount,
        standardPersonnel = info.standardPersonnel,
        maximumPersonnel = info.maximumPersonnel,
        price = info.price,
        content = info.content,
        roomImages = info.roomImages.stream()
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
        imageUrl = info.imageUrl
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