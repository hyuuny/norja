package com.hyuuny.norja.domain.command

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.domain.LodgingCompany
import com.hyuuny.norja.domain.Type

data class LodgingCompanyUpdateCommand(
    val type: Type,
    val name: String,
    val thumbnail: String,
    val businessNumber: String,
    val tellNumber: String,
    val address: Address,
    val searchTag: String?,
    val images: List<ImageCreateCommand>? = listOf(),
    val facilities: List<FacilitiesCreateCommand>? = listOf(),
) {

    fun update(entity: LodgingCompany) {
        entity.changeType(this.type)
        entity.changeName(this.name)
        entity.changeThumbnail(this.thumbnail)
        entity.changeBusinessNumber(this.businessNumber)
        entity.changeTellNumber(this.tellNumber)
        entity.changeAddress(this.address)
        entity.changeSearchTag(this.searchTag)

        entity.imagesClear()
        images?.stream()
            ?.map(ImageCreateCommand::toEntity)
            ?.forEach(entity::addImages)

        entity.facilitiesClear()
        facilities?.stream()
            ?.map(FacilitiesCreateCommand::toEntity)
            ?.forEach(entity::addFacilities)
    }

}