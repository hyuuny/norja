package com.hyuuny.norja.lodgingcompanies.domain

import com.hyuuny.norja.address.domain.Address

data class LodgingCompanyCreateCommand(
    val categoryId: Long,
    val type: Type,
    val name: String,
    val thumbnail: String,
    val businessNumber: String,
    val tellNumber: String,
    val address: Address,
    val searchTag: String?,
    val images: MutableList<ImageCreateCommand> = mutableListOf(),
    val facilities: MutableList<FacilitiesCreateCommand> = mutableListOf(),
) {

    val toEntity: LodgingCompany
        get() {
            val newLodging = LodgingCompany.create(
                categoryId = this.categoryId,
                type = this.type,
                name = this.name,
                thumbnail = this.thumbnail,
                businessNumber = this.businessNumber,
                tellNumber = this.tellNumber,
                address = this.address,
                searchTag = this.searchTag,
            )

            this.images
                .map(ImageCreateCommand::toEntity)
                .forEach(newLodging::addImages)

            this.facilities
                .map(FacilitiesCreateCommand::toEntity)
                .forEach(newLodging::addFacilities)

            return newLodging
        }

}

data class ImageCreateCommand(
    val priority: Long?,
    val imageUrl: String,
) {
    val toEntity = Image.create(priority = this.priority, imageUrl = this.imageUrl)
}

data class FacilitiesCreateCommand(
    val name: String,
    val iconImageUrl: String,
    val priority: Long?,
) {
    val toEntity = Facilities.create(name = name, iconImageUrl = iconImageUrl, priority = priority)
}

data class LodgingCompanyUpdateCommand(
    val categoryId: Long,
    val type: Type,
    val name: String,
    val thumbnail: String,
    val businessNumber: String,
    val tellNumber: String,
    val address: Address,
    val searchTag: String?,
    val images: List<ImageCreateCommand> = listOf(),
    val facilities: List<FacilitiesCreateCommand> = listOf(),
) {

    fun update(entity: LodgingCompany) {
        entity.changeCategoryId(this.categoryId)
        entity.changeType(this.type)
        entity.changeName(this.name)
        entity.changeThumbnail(this.thumbnail)
        entity.changeBusinessNumber(this.businessNumber)
        entity.changeTellNumber(this.tellNumber)
        entity.changeAddress(this.address)
        entity.changeSearchTag(this.searchTag)

        entity.imagesClear()
        this.images
            .map(ImageCreateCommand::toEntity)
            .forEach(entity::addImages)

        entity.facilitiesClear()
        this.facilities
            .map(FacilitiesCreateCommand::toEntity)
            .forEach(entity::addFacilities)
    }

}