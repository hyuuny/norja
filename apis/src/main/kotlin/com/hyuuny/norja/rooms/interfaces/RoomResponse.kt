package com.hyuuny.norja.rooms.interfaces

import com.hyuuny.norja.rooms.domain.RoomFacilitiesInfo
import com.hyuuny.norja.rooms.domain.RoomImageInfo
import com.hyuuny.norja.rooms.domain.Type

data class RoomResponse(
    val id: Long,
    val lodgingCompanyId: Long,
    val type: Type,
    val name: String,
    val standardPersonnel: Int = 2,
    val maximumPersonnel: Int = 2,
    val price: Long,
    val content: String? = null,
    val images: List<RoomImageResponse>? = listOf(),
    val facilities: List<RoomFacilitiesResponse>? = listOf(),
)

data class RoomImageResponse(
    val roomId: Long,
    val priority: Long?,
    val imageUrl: String,
) {
    constructor(info: RoomImageInfo) : this(
        roomId = info.roomId,
        priority = info.priority,
        imageUrl = info.imageUrl
    )
}

data class RoomFacilitiesResponse(
    val roomId: Long,
    val name: String,
    val priority: Long?,
    val iconImageUrl: String,
) {
    constructor(info: RoomFacilitiesInfo) : this(
        roomId = info.roomId,
        name = info.name,
        priority = info.priority,
        iconImageUrl = info.iconImageUrl,
    )
}