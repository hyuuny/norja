package com.hyuuny.norja.lodgingcompanies.interfaces

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.lodgingcompanies.domain.*
import com.hyuuny.norja.rooms.interfaces.RoomResponse
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

@Relation(collectionRelation = "lodgingCompanies")
@JsonInclude(Include.NON_NULL)
data class LodgingCompanyResponse(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "체크인", example = "2022-08-08", required = true)
    val checkIn: String,

    @field:Schema(description = "체크아웃", example = "2022-08-10", required = true)
    val checkOut: String,

    @field:Schema(description = "타입", example = "HOTEL", required = true)
    val type: Type,

    @field:Schema(description = "상태", example = "OPEN", required = true)
    val status: Status,

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
    val searchTag: String? = null,

    @field:Schema(description = "객실")
    val rooms: List<RoomResponse>? = listOf(),

    @field:Schema(description = "이미지")
    val images: List<ImageResponse>? = listOf(),

    @field:Schema(description = "시설")
    val facilities: List<FacilitiesResponse>? = listOf(),

    @field:Schema(description = "등록일", example = "2022-08-08T21:51:00.797659")
    val createdAt: LocalDateTime,
) {

    constructor(info: LodgingCompanyAndRoomInfo) : this(
        id = info.id,
        type = info.type,
        status = info.status,
        name = info.name,
        thumbnail = info.thumbnail,
        businessNumber = info.businessNumber,
        tellNumber = info.tellNumber,
        address = info.address,
        searchTag = info.searchTag,
        checkIn = info.checkIn,
        checkOut = info.checkOut,
        rooms = info.rooms.stream()
            .map(::RoomResponse)
            .sorted(Comparator.comparing(RoomResponse::price))
            .toList(),
        images = info.images.stream()
            .map(::ImageResponse)
            .sorted((Comparator.comparing(ImageResponse::priority)))
            .toList(),
        facilities = info.facilities.stream()
            .map(::FacilitiesResponse)
            .sorted((Comparator.comparing(FacilitiesResponse::priority)))
            .toList(),
        createdAt = info.createdAt,
    )

}

data class ImageResponse(
    val lodgingCompanyId: Long,
    val priority: Long,
    val imageUrl: String,
) {
    constructor(info: ImageInfo) : this(
        lodgingCompanyId = info.lodgingCompanyId,
        priority = info.priority!!,
        imageUrl = info.imageUrl
    )
}

data class FacilitiesResponse(
    val lodgingCompanyId: Long?,
    val name: String,
    val priority: Long,
    val iconImageUrl: String?,
) {
    constructor(info: FacilitiesInfo) : this(
        lodgingCompanyId = info.lodgingCompanyId,
        name = info.name,
        priority = info.priority!!,
        iconImageUrl = info.iconImageUrl,
    )
}