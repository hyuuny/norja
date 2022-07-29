package com.hyuuny.norja.lodgingcompanies.interfaces

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.lodgingcompanies.domain.*

class LodgingCompanyDto

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

data class ImageCreateDto(
    val priority: Long? = 100,
    val imageUrl: String,
) {
    val toCommand = ImageCreateCommand(priority = this.priority, imageUrl = this.imageUrl)
}

data class FacilitiesCreateDto(
    val name: String,
    val iconImageUrl: String,
    val priority: Long? = 100L,
) {
    val toCommand = FacilitiesCreateCommand(
        name = this.name,
        iconImageUrl = this.iconImageUrl,
        priority = this.priority
    )
}

data class LodgingCompanyUpdateDto(
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
    val toCommand = LodgingCompanyUpdateCommand(
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
