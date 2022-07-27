package com.hyuuny.norja.lodgingcompanies.interfaces.req

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.domain.Type
import com.hyuuny.norja.domain.command.LodgingCompanyCreateCommand

data class LodgingCompanyCreateDto(
    val type: Type,
    val name: String,
    val thumbnail: String,
    val businessNumber: String,
    val tellNumber: String,
    val address: Address,
    val searchTag: String?,
    val images: MutableList<ImageCreateDto> = mutableListOf(),
    val facilities: MutableList<FacilitiesCreateDto> = mutableListOf(),
) {
    fun toCommand() = LodgingCompanyCreateCommand(
        type = this.type,
        name = this.name,
        thumbnail = this.thumbnail,
        businessNumber = this.businessNumber,
        tellNumber = this.tellNumber,
        address = this.address,
        searchTag = this.searchTag,
        images = this.images.stream()
            .map(ImageCreateDto::toCommand)
            .toList().toMutableList(),
        facilities = this.facilities.stream()
            .map(FacilitiesCreateDto::toCommand)
            .toList().toMutableList()
    )
}
