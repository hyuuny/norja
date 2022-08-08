package com.hyuuny.norja.lodgingcompanies.interfaces

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.lodgingcompanies.domain.*
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation

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

    constructor(info: LodgingCompanyInfo) : this(
        id = info.id,
        type = info.type,
        status = info.status,
        name = info.name,
        thumbnail = info.thumbnail,
        businessNumber = info.businessNumber,
        tellNumber = info.tellNumber,
        address = info.address,
        searchTag = info.searchTag,
        images = info.images.stream()
            .map(::ImageResponse)
            .sorted((Comparator.comparing(ImageResponse::priority)))
            .toList(),
        facilities = info.facilities.stream()
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
    constructor(info: ImageInfo) : this(
        lodgingCompanyId = info.lodgingCompanyId,
        priority = info.priority!!,
        imageUrl = info.imageUrl
    )
}

data class FacilitiesResponse(

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long?,

    @field:Schema(description = "시설명", example = "주차가능", required = true)
    val name: String,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long,

    @field:Schema(description = "아이콘이미지 URL", example = "icon-image-url", required = true)
    val iconImageUrl: String?,
) {
    constructor(info: FacilitiesInfo) : this(
        lodgingCompanyId = info.lodgingCompanyId,
        name = info.name,
        priority = info.priority!!,
        iconImageUrl = info.iconImageUrl,
    )
}