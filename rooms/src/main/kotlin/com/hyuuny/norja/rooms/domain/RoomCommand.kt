package com.hyuuny.norja.rooms.domain

class RoomCommand

data class RoomCreateCommand(
    val lodgingCompanyId: Long,
    val type: Type,
    val name: String,
    val standardPersonnel: Int = 2,
    val maximumPersonnel: Int = 2,
    val price: Long,
    val content: String? = null,
    val images: MutableList<RoomImageCreateCommand> = mutableListOf(),
    val facilities: MutableList<RoomFacilitiesCreateCommand> = mutableListOf(),
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
                .map(RoomImageCreateCommand::toEntity)
                .forEach(newRoom::addImages)

            this.facilities.stream()
                .map(RoomFacilitiesCreateCommand::toEntity)
                .forEach(newRoom::addFacilities)

            return newRoom
        }
}

data class RoomImageCreateCommand(
    val priority: Long?,
    val imageUrl: String,
) {
    val toEntity = RoomImage.create(priority = this.priority, imageUrl = this.imageUrl)
}

data class RoomFacilitiesCreateCommand(
    val name: String,
    val iconImageUrl: String,
    val priority: Long?,
) {
    val toEntity =
        RoomFacilities.create(name = name, iconImageUrl = iconImageUrl, priority = priority)
}

data class RoomUpdateCommand(
    val lodgingCompanyId: Long,
    val type: Type,
    val name: String,
    val standardPersonnel: Int = 2,
    val maximumPersonnel: Int = 2,
    val price: Long,
    val content: String? = null,
    val images: MutableList<RoomImageCreateCommand> = mutableListOf(),
    val facilities: MutableList<RoomFacilitiesCreateCommand> = mutableListOf(),
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
            .map(RoomImageCreateCommand::toEntity)
            .forEach(entity::addImages)

        entity.facilitiesClear()
        this.facilities.stream()
            .map(RoomFacilitiesCreateCommand::toEntity)
            .forEach(entity::addFacilities)
    }
}
