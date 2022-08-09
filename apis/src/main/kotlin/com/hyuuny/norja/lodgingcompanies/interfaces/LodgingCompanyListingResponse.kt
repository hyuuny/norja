package com.hyuuny.norja.lodgingcompanies.interfaces

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyListingInfo
import com.hyuuny.norja.lodgingcompanies.domain.Status
import com.hyuuny.norja.lodgingcompanies.domain.Type
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

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

    @field:Schema(description = "등록일", example = "2022-08-08T21:51:00.797659")
    val createdAt: LocalDateTime,
) {
    constructor(info: LodgingCompanyListingInfo) : this(
        id = info.id,
        type = info.type,
        status = info.status,
        name = info.name,
        thumbnail = info.thumbnail,
        address = info.address,
        price = info.price,
        searchTag = info.searchTag,
        createdAt = info.createdAt,
    )
}
