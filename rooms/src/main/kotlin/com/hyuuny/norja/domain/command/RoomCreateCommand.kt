package com.hyuuny.norja.domain.command

import com.hyuuny.norja.domain.Room
import com.hyuuny.norja.domain.Type

data class RoomCreateCommand(
    val lodgingCompanyId: Long,
    val type: Type,
    val name: String,
    val standardPersonnel: Int = 2,
    val maximumPersonnel: Int = 2,
    val price: Long,
    val content: String? = null,
    val images: MutableList<ImageCreateCommand> = mutableListOf(),
    val facilities: MutableList<FacilitiesCreateCommand> = mutableListOf(),
) {
    val toEntity: Room
        get() {
            val newRoom = Room.create(
                lodgingCompanyId = this.lodgingCompanyId,
                type = this.type,
                name = this.name,
                standardPersonnel = this.standardPersonnel,
                maximumPersonnel = this.maximumPersonnel,
                price = this.price,
                content = this.content,
            )

            this.images.stream()
                .map(ImageCreateCommand::toEntity)
                .forEach(newRoom::addImages)

            this.facilities.stream()
                .map(FacilitiesCreateCommand::toEntity)
                .forEach(newRoom::addFacilities)

            return newRoom
        }
}
