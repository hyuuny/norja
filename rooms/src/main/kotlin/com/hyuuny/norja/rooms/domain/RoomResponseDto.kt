package com.hyuuny.norja.rooms.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import io.swagger.v3.oas.annotations.media.Schema

@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@class")
@JsonInclude(Include.NON_NULL)
data class RoomResponseDto(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long = 0,

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long = 0,

    @field:Schema(description = "타입", example = "DOUBLE_ROOM", required = true)
    val type: Type = Type.DOUBLE_ROOM,

    @field:Schema(description = "이름", example = "더블룸", required = true)
    val name: String = "",

    @field:Schema(description = "객실 수", example = "10", required = true)
    val roomCount: Int = 10,

    @field:Schema(description = "남은 객실 수", example = "2")
    val remainingRoomCount: String? = null,

    @field:Schema(description = "기준 인원", example = "2")
    val standardPersonnel: Int = 2,

    @field:Schema(description = "최대 인원", example = "2")
    val maximumPersonnel: Int = 2,

    @field:Schema(description = "금액", example = "130000", required = true)
    val price: Long = 0,

    @field:Schema(description = "내용", example = "야경 뷰 최고!")
    val content: String? = null,

    @field:Schema(description = "이미지")
    val roomImages: List<RoomImageResponseDto>? = listOf(),

    @field:Schema(description = "시설")
    val roomFacilities: List<RoomFacilitiesResponseDto>? = listOf(),
) {
    constructor(entity: Room) : this(entity, null)

    constructor(entity: Room, remainingRoomCount: String?) : this(
        id = entity.id!!,
        lodgingCompanyId = entity.lodgingCompanyId,
        type = entity.type,
        name = entity.name,
        roomCount = entity.roomCount,
        remainingRoomCount = remainingRoomCount,
        standardPersonnel = entity.standardPersonnel,
        maximumPersonnel = entity.maximumPersonnel,
        price = entity.price,
        content = entity.content,
        roomImages = entity.roomImages!!.stream()
            .map(::RoomImageResponseDto)
            .sorted((Comparator.comparing(RoomImageResponseDto::priority)))
            .toList(),
        roomFacilities = entity.roomFacilities!!.stream()
            .map(::RoomFacilitiesResponseDto)
            .sorted((Comparator.comparing(RoomFacilitiesResponseDto::priority)))
            .toList(),
    )
}

data class RoomImageResponseDto(

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long = 0,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long = 100,

    @field:Schema(description = "이미지 URL", example = "image-url", required = true)
    val imageUrl: String = "",
) {
    constructor(entity: RoomImage) : this(
        roomId = entity.roomId,
        priority = entity.priority!!,
        imageUrl = entity.imageUrl
    )
}

data class RoomFacilitiesResponseDto(

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long = 0,

    @field:Schema(description = "시설명", example = "퀸 침대", required = true)
    val name: String = "",

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long = 100,

    @field:Schema(description = "아이콘이미지 URL", example = "icon-image-url", required = true)
    val iconImageUrl: String = "",
) {
    constructor(entity: RoomFacilities) : this(
        roomId = entity.roomId,
        name = entity.name,
        priority = entity.priority!!,
        iconImageUrl = entity.iconImageUrl,
    )
}
