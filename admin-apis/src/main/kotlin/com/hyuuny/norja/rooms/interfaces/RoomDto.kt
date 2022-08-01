package com.hyuuny.norja.rooms.interfaces

import com.hyuuny.norja.rooms.domain.*

class RoomDto

data class RoomCreateDto(
    val lodgingCompanyId: Long,
    val type: Type,
    val name: String,
    val roomCount: Int = 10,
    val standardPersonnel: Int = 2,
    val maximumPersonnel: Int = 2,
    val price: Long,
    val content: String? = null,
    val images: MutableList<RoomImageCreateDto> = mutableListOf(),
    val facilities: MutableList<RoomFacilitiesCreateDto> = mutableListOf(),
) {
    fun toCommand() = RoomCreateCommand(
        lodgingCompanyId = this.lodgingCompanyId,
        type = this.type,
        name = this.name,
        roomCount = this.roomCount,
        standardPersonnel = this.standardPersonnel,
        maximumPersonnel = this.maximumPersonnel,
        price = this.price,
        content = this.content,
        images = this.images.stream()
            .map(RoomImageCreateDto::toCommand)
            .toList().toMutableList(),
        facilities = this.facilities.stream()
            .map(RoomFacilitiesCreateDto::toCommand)
            .toList().toMutableList(),
    )
}

data class RoomImageCreateDto(
    val priority: Long?,
    val imageUrl: String,
) {
    val toCommand = RoomImageCreateCommand(priority = this.priority, imageUrl = this.imageUrl)
}

data class RoomFacilitiesCreateDto(
    val name: String,
    val iconImageUrl: String,
    val priority: Long? = 100L,
) {
    val toCommand = RoomFacilitiesCreateCommand(
        name = this.name,
        iconImageUrl = this.iconImageUrl,
        priority = this.priority
    )
}

data class RoomUpdateDto(
    val lodgingCompanyId: Long,
    val type: Type,
    val name: String,
    val roomCount: Int = 10,
    val standardPersonnel: Int = 2,
    val maximumPersonnel: Int = 2,
    val price: Long,
    val content: String? = null,
    val images: MutableList<RoomImageCreateDto> = mutableListOf(),
    val facilities: MutableList<RoomFacilitiesCreateDto> = mutableListOf(),
) {
    val toCommand = RoomUpdateCommand(
        lodgingCompanyId = this.lodgingCompanyId,
        type = this.type,
        name = this.name,
        roomCount = this.roomCount,
        standardPersonnel = this.standardPersonnel,
        maximumPersonnel = this.maximumPersonnel,
        price = this.price,
        content = this.content,
        images = this.images.stream()
            .map(RoomImageCreateDto::toCommand)
            .toList().toMutableList(),
        facilities = this.facilities.stream()
            .map(RoomFacilitiesCreateDto::toCommand)
            .toList().toMutableList(),
    )
}