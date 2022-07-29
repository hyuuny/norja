package com.hyuuny.norja.domain.command

import com.hyuuny.norja.domain.Room
import com.hyuuny.norja.domain.Type

data class RoomUpdateCommand(
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
    fun update(entity: Room) {
        entity.changeLodgingCompanyId(this.lodgingCompanyId)
        entity.changeType(this.type)
        entity.changeName(this.name)
        entity.changeStandardPersonnel(this.standardPersonnel)
        entity.changeMaximumPersonnel(this.maximumPersonnel)
        entity.changePrice(this.price)
        entity.changeContent(this.content)

        entity.imagesClear()
        this.images.stream()
            .map(ImageCreateCommand::toEntity)
            .forEach(entity::addImages)

        entity.facilitiesClear()
        this.facilities.stream()
            .map(FacilitiesCreateCommand::toEntity)
            .forEach(entity::addFacilities)
    }
}
