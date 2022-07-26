package com.hyuuny.norja.domain.command

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.domain.LodgingCompany
import com.hyuuny.norja.domain.Type

data class LodgingCompanyCreateCommand(
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
                type = this.type,
                name = this.name,
                thumbnail = this.thumbnail,
                businessNumber = this.businessNumber,
                tellNumber = this.tellNumber,
                address = this.address,
                searchTag = this.searchTag,
            )

            this.images.stream()
                .map(ImageCreateCommand::toEntity)
                .forEach(newLodging::addImages)

            this.facilities.stream()
                .map(FacilitiesCreateCommand::toEntity)
                .forEach(newLodging::addFacilities)

            return newLodging
        }

}
