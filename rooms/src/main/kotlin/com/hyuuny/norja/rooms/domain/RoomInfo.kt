package com.hyuuny.norja.rooms.domain

data class RoomInfo(
    val id: Long,
    val lodgingCompanyId: Long,
    val type: Type,
    val name: String,
    val standardPersonnel: Int = 2,
    val maximumPersonnel: Int = 2,
    val price: Long,
    val content: String? = null,
    val roomImages: List<RoomImageInfo> = listOf(),
    val roomFacilities: List<RoomFacilitiesInfo> = listOf(),
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
        roomImages = entity.roomImages!!.stream()
            .map(::RoomImageInfo)
            .toList(),
        roomFacilities = entity.roomFacilities!!.stream()
            .map(::RoomFacilitiesInfo)
            .toList()
    )
}

data class RoomImageInfo(
    val roomId: Long,
    val priority: Long?,
    val imageUrl: String,
) {
    constructor(entity: RoomImage) : this(
        roomId = entity.room?.id!!,
        priority = entity.priority,
        imageUrl = entity.imageUrl,
    )
}

data class RoomFacilitiesInfo(
    val roomId: Long,
    val name: String,
    val priority: Long?,
    val iconImageUrl: String,
) {
    constructor(entity: RoomFacilities) : this(
        roomId = entity.room?.id!!,
        name = entity.name,
        priority = entity.priority,
        iconImageUrl = entity.iconImageUrl,
    )
}
