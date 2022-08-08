package com.hyuuny.norja.lodgingcompanies.interfaces

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.lodgingcompanies.domain.*
import io.swagger.v3.oas.annotations.media.Schema

class LodgingCompanyDto

data class LodgingCompanyCreateDto(

    @field:Schema(description = "타입", example = "HOTEL", required = true)
    val type: Type,

    @field:Schema(description = "숙박업체명", example = "스테이 호텔", required = true)
    val name: String,

    @field:Schema(description = "썸네일 URL", example = "thumbnail-url", required = true)
    val thumbnail: String,

    @field:Schema(description = "사업자등록번호", example = "1233445678", required = true)
    val businessNumber: String,

    @field:Schema(description = "전화번호", example = "070-1234-1234", required = true)
    val tellNumber: String,

    @field:Schema(description = "주소", required = true)
    val address: Address,

    @field:Schema(description = "검색태그", example = "스테이, 강남")
    val searchTag: String?,

    @field:Schema(description = "이미지")
    val images: MutableList<ImageCreateDto> = mutableListOf(),

    @field:Schema(description = "시설")
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

    @field:Schema(description = "우선순위", example = "100")
    val priority: Long? = 100,

    @field:Schema(description = "이미지 URL", example = "image-url", required = true)
    val imageUrl: String,
) {
    fun toCommand() = ImageCreateCommand(priority = this.priority, imageUrl = this.imageUrl)
}

data class FacilitiesCreateDto(

    @field:Schema(description = "시설명", example = "주차가능", required = true)
    val name: String,

    @field:Schema(description = "아이콘이미지 URL", example = "icon-image-url", required = true)
    val iconImageUrl: String,

    @field:Schema(description = "우선순위", example = "100")
    val priority: Long? = 100L,
) {
    fun toCommand() = FacilitiesCreateCommand(
        name = this.name,
        iconImageUrl = this.iconImageUrl,
        priority = this.priority
    )
}

data class LodgingCompanyUpdateDto(

    @field:Schema(description = "타입", example = "HOTEL", required = true)
    val type: Type,

    @field:Schema(description = "숙박업체명", example = "스테이 호텔", required = true)
    val name: String,

    @field:Schema(description = "썸네일 URL", example = "thumbnail-url", required = true)
    val thumbnail: String,

    @field:Schema(description = "사업자등록번호", example = "1233445678", required = true)
    val businessNumber: String,

    @field:Schema(description = "전화번호", example = "070-1234-1234", required = true)
    val tellNumber: String,

    @field:Schema(description = "주소", required = true)
    val address: Address,

    @field:Schema(description = "검색태그", example = "스테이, 강남")
    val searchTag: String?,

    @field:Schema(description = "이미지")
    val images: MutableList<ImageCreateDto> = mutableListOf(),

    @field:Schema(description = "시설")
    val facilities: MutableList<FacilitiesCreateDto> = mutableListOf(),
) {
    fun toCommand() = LodgingCompanyUpdateCommand(
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
