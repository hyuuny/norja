package com.hyuuny.norja.lodgingcompanies.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.rooms.domain.RoomInfo
import com.hyuuny.norja.rooms.domain.RoomResponse
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

@Relation(collectionRelation = "lodgingCompanies")
@JsonInclude(Include.NON_NULL)
data class LodgingCompanyResponse(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

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

    @field:Schema(description = "이미지")
    val images: List<ImageResponse>? = listOf(),

    @field:Schema(description = "시설")
    val facilities: List<FacilitiesResponse>? = listOf(),
) {

    constructor(entity: LodgingCompany) : this(
        id = entity.id!!,
        type = entity.type,
        status = entity.status,
        name = entity.name,
        thumbnail = entity.thumbnail,
        businessNumber = entity.businessNumber,
        tellNumber = entity.tellNumber,
        address = entity.address,
        searchTag = entity.searchTag,
        images = entity.images!!.stream()
            .map(::ImageResponse)
            .sorted((Comparator.comparing(ImageResponse::priority)))
            .toList(),
        facilities = entity.facilities!!.stream()
            .map(::FacilitiesResponse)
            .sorted((Comparator.comparing(FacilitiesResponse::priority)))
            .toList()
    )

}

data class ImageResponse(

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long,

    @field:Schema(description = "이미지 URL", example = "image-url", required = true)
    val imageUrl: String,
) {
    constructor(entity: Image) : this(
        lodgingCompanyId = entity.lodgingCompany?.id!!,
        priority = entity.priority!!,
        imageUrl = entity.imageUrl
    )
}

data class FacilitiesResponse(

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long,

    @field:Schema(description = "시설명", example = "주차가능", required = true)
    val name: String,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long,

    @field:Schema(description = "아이콘이미지 URL", example = "icon-image-url", required = true)
    val iconImageUrl: String?,
) {
    constructor(entity: Facilities) : this(
        lodgingCompanyId = entity.lodgingCompany?.id!!,
        name = entity.name,
        priority = entity.priority!!,
        iconImageUrl = entity.iconImageUrl,
    )
}

@Relation(collectionRelation = "lodgingCompanies")
@JsonInclude(Include.NON_NULL)
data class LodgingCompanyAndRoomResponse(

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

    @field:Schema(description = "후기 평점", example = "4.8")
    val reviewAverageScore: Double,

    @field:Schema(description = "후개 개수", example = "21")
    val reviewCount: Long,

    @field:Schema(description = "객실")
    val rooms: List<RoomResponse>? = listOf(),

    @field:Schema(description = "이미지")
    val images: List<ImageResponse>? = listOf(),

    @field:Schema(description = "시설")
    val facilities: List<FacilitiesResponse>? = listOf(),

    @field:Schema(description = "등록일", example = "2022-08-08T21:51:00.797659")
    val createdAt: LocalDateTime,
) {
    constructor(
        searched: SearchedLodgingCompany,
        rooms: List<RoomInfo>,
        checkIn: String,
        checkOut: String
    ) : this(
        id = searched.id!!,
        type = searched.type,
        status = searched.status,
        name = searched.name,
        thumbnail = searched.thumbnail,
        businessNumber = searched.businessNumber,
        tellNumber = searched.tellNumber,
        address = searched.address,
        searchTag = searched.searchTag,
        reviewAverageScore = searched.reviewAverageScore!!,
        reviewCount = searched.reviewCount!!,
        checkIn = checkIn,
        checkOut = checkOut,
        rooms = rooms.stream()
            .map(::RoomResponse)
            .sorted(Comparator.comparing(RoomResponse::price))
            .toList(),
        images = searched.images,
        facilities = searched.facilities,
        createdAt = searched.createdAt,
    )

}

@Relation(collectionRelation = "lodgingCompanies")
@JsonInclude(Include.NON_NULL)
data class LodgingCompanyListingResponse(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "타입", example = "HOTEL", required = true)
    val type: Type,

    @field:Schema(description = "상태", example = "OPEN", required = true)
    val status: Status,

    @field:Schema(description = "숙박업체명", example = "스테이 호텔", required = true)
    val name: String,

    @field:Schema(description = "썸네일 URL", example = "thumbnail-url", required = true)
    val thumbnail: String,

    @field:Schema(description = "주소", required = true)
    val address: Address,

    @field:Schema(description = "가격", example = "80000", required = true)
    val price: Long,

    @field:Schema(description = "검색태그", example = "스테이, 강남")
    val searchTag: String,

    @field:Schema(description = "후기 평점", example = "4.8")
    val reviewAverageScore: Double,

    @field:Schema(description = "후개 개수", example = "21")
    val reviewCount: Long,

    @field:Schema(description = "등록일", example = "2022-08-08T21:51:00.797659")
    val createdAt: LocalDateTime,
) {
    constructor(searched: SearchedLodgingCompanyListing) : this(
        id = searched.id!!,
        type = searched.type,
        status = searched.status,
        name = searched.name!!,
        thumbnail = searched.thumbnail!!,
        address = searched.address!!,
        price = searched.price!!,
        searchTag = searched.searchTag!!,
        reviewAverageScore = searched.reviewAverageScore!!,
        reviewCount = searched.reviewCount!!,
        createdAt = searched.createdAt!!,
    )
}

