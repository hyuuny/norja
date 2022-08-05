package com.hyuuny.norja.lodgingcompanies.interfaces

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.lodgingcompanies.domain.*
import com.hyuuny.norja.rooms.interfaces.RoomResponse
import java.time.LocalDate
import java.time.LocalDateTime

@JsonInclude(Include.NON_NULL)
data class LodgingCompanyResponse(
    val id: Long,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val type: Type,
    val status: Status,
    val name: String,
    val thumbnail: String,
    val businessNumber: String,
    val tellNumber: String,
    val address: Address,
    val searchTag: String? = null,
    val rooms: List<RoomResponse>? = listOf(),
    val images: List<ImageResponse>? = listOf(),
    val facilities: List<FacilitiesResponse>? = listOf(),
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