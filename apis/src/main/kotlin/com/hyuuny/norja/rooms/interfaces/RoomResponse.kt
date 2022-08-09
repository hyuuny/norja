package com.hyuuny.norja.rooms.interfaces

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.rooms.domain.RoomFacilitiesInfo
import com.hyuuny.norja.rooms.domain.RoomImageInfo
import com.hyuuny.norja.rooms.domain.RoomInfo
import com.hyuuny.norja.rooms.domain.Type
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(Include.NON_NULL)
data class RoomResponse(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long,

    @field:Schema(description = "타입", example = "DOUBLE_ROOM", required = true)
    val type: Type,

    @field:Schema(description = "이름", example = "더블룸", required = true)
    val name: String,

    @field:Schema(description = "객실 수", example = "10", required = true)
    val roomCount: Int = 10,

    @field:Schema(description = "남은 객실 수", example = "2")
    val remainingRoomCount: Long? = 0,

    @field:Schema(description = "기준 인원", example = "2")
    val standardPersonnel: Int = 2,

    @field:Schema(description = "최대 인원", example = "2")
    val maximumPersonnel: Int = 2,

    @field:Schema(description = "금액", example = "130000", required = true)
    val price: Long,

    @field:Schema(description = "내용", example = "야경 뷰 최고!")
    val content: String? = null,

    @field:Schema(description = "이미지")
    val roomImages: List<RoomImageResponse>? = listOf(),

    @field:Schema(description = "시설")
    val roomFacilities: List<RoomFacilitiesResponse>? = listOf(),
) {
    constructor(info: RoomInfo) : this(
        id = info.id,
        lodgingCompanyId = info.lodgingCompanyId,
        type = info.type,
        name = info.name,
        roomCount = info.roomCount,
        remainingRoomCount = info.remainingRoomCount,
        standardPersonnel = info.standardPersonnel,
        maximumPersonnel = info.maximumPersonnel,
        price = info.price,
        content = info.content,
        roomImages = info.roomImages.stream()
            .map(::RoomImageResponse)
            .sorted((Comparator.comparing(RoomImageResponse::priority)))
            .toList(),
        roomFacilities = info.roomFacilities.stream()
            .map(::RoomFacilitiesResponse)
            .sorted((Comparator.comparing(RoomFacilitiesResponse::priority)))
            .toList(),
    )
}

data class RoomImageResponse(

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long,

    @field:Schema(description = "이미지 URL", example = "image-url", required = true)
    val imageUrl: String,
) {
    constructor(info: RoomImageInfo) : this(
        roomId = info.roomId,
        priority = info.priority!!,
        imageUrl = info.imageUrl
    )
}

data class RoomFacilitiesResponse(

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long,

    @field:Schema(description = "시설명", example = "퀸 침대", required = true)
    val name: String,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long,

    @field:Schema(description = "아이콘이미지 URL", example = "icon-image-url", required = true)
    val iconImageUrl: String,
) {
    constructor(info: RoomFacilitiesInfo) : this(
        roomId = info.roomId,
        name = info.name,
        priority = info.priority!!,
        iconImageUrl = info.iconImageUrl,
    )
}