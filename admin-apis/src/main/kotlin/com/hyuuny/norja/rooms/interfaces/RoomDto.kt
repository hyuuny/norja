package com.hyuuny.norja.rooms.interfaces

import com.hyuuny.norja.rooms.domain.*
import io.swagger.v3.oas.annotations.media.Schema

class RoomDto

data class RoomCreateDto(

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long,

    @field:Schema(description = "타입", example = "DOUBLE_ROOM", required = true)
    val type: Type,

    @field:Schema(description = "이름", example = "더블룸", required = true)
    val name: String,

    @field:Schema(description = "객실 수", example = "10")
    val roomCount: Int = 10,

    @field:Schema(description = "기준 인원", example = "2")
    val standardPersonnel: Int = 2,

    @field:Schema(description = "최대 인원", example = "2")
    val maximumPersonnel: Int = 2,

    @field:Schema(description = "금액", example = "130000", required = true)
    val price: Long,

    @field:Schema(description = "내용", example = "야경 뷰 최고!")
    val content: String? = null,

    @field:Schema(description = "이미지")
    val images: MutableList<RoomImageCreateDto> = mutableListOf(),

    @field:Schema(description = "시설")
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

    @field:Schema(description = "우선순위", example = "100")
    val priority: Long? = 100,

    @field:Schema(description = "이미지 URL", example = "image-url", required = true)
    val imageUrl: String,
) {
    fun toCommand() = RoomImageCreateCommand(priority = this.priority, imageUrl = this.imageUrl)
}

data class RoomFacilitiesCreateDto(

    @field:Schema(description = "객실명", example = "더블룸", required = true)
    val name: String,

    @field:Schema(description = "아이콘이미지 URL", example = "icon-image-url", required = true)
    val iconImageUrl: String,

    @field:Schema(description = "우선순위", example = "100")
    val priority: Long? = 100L,
) {
    fun toCommand() = RoomFacilitiesCreateCommand(
        name = this.name,
        iconImageUrl = this.iconImageUrl,
        priority = this.priority
    )
}

data class RoomUpdateDto(

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long,

    @field:Schema(description = "타입", example = "DOUBLE_ROOM", required = true)
    val type: Type,

    @field:Schema(description = "이름", example = "더블룸", required = true)
    val name: String,

    @field:Schema(description = "객실 수", example = "10")
    val roomCount: Int = 10,

    @field:Schema(description = "기준 인원", example = "2")
    val standardPersonnel: Int = 2,

    @field:Schema(description = "최대 인원", example = "2")
    val maximumPersonnel: Int = 2,

    @field:Schema(description = "금액", example = "130000", required = true)
    val price: Long,

    @field:Schema(description = "내용", example = "야경 뷰 최고!")
    val content: String? = null,

    @field:Schema(description = "이미지")
    val images: MutableList<RoomImageCreateDto> = mutableListOf(),

    @field:Schema(description = "시설")
    val facilities: MutableList<RoomFacilitiesCreateDto> = mutableListOf(),
) {
    fun toCommand() = RoomUpdateCommand(
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