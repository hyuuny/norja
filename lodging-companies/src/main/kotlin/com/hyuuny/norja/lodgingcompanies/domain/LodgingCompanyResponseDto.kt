package com.hyuuny.norja.lodgingcompanies.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.rooms.domain.RoomResponseDto
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@class")
@Relation(collectionRelation = "lodgingCompanies")
@JsonInclude(Include.NON_NULL)
data class LodgingCompanyResponseDto(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long = 0,

    @field:Schema(description = "카테고리 아이디", example = "1", required = true)
    val categoryId: Long = 0,

    @field:Schema(description = "타입", example = "HOTEL", required = true)
    val type: Type = Type.HOTEL,

    @field:Schema(description = "상태", example = "OPEN", required = true)
    val status: Status = Status.OPEN,

    @field:Schema(description = "숙박업체명", example = "스테이 호텔", required = true)
    val name: String = "",

    @field:Schema(description = "썸네일 URL", example = "thumbnail-url", required = true)
    val thumbnail: String = "",

    @field:Schema(description = "사업자등록번호", example = "1233445678", required = true)
    val businessNumber: String = "",

    @field:Schema(description = "전화번호", example = "070-1234-1234", required = true)
    val tellNumber: String = "",

    @field:Schema(description = "주소", required = true)
    val address: Address = Address("", "", ""),

    @field:Schema(description = "검색태그", example = "스테이, 강남")
    val searchTag: String? = null,

    @field:Schema(description = "이미지")
    val images: List<ImageResponseDto>? = listOf(),

    @field:Schema(description = "시설")
    val facilities: List<FacilitiesResponseDto>? = listOf(),
) {

    companion object {
        operator fun invoke(lodgingCompany: LodgingCompany) = with(lodgingCompany) {
            LodgingCompanyResponseDto(
                id = id!!,
                categoryId = categoryId,
                type = type,
                status = status,
                name = name,
                thumbnail = thumbnail,
                businessNumber = businessNumber,
                tellNumber = tellNumber,
                address = address,
                searchTag = searchTag,
                images = images!!.map { ImageResponseDto(it) }
                    .sortedBy(ImageResponseDto::priority)
                    .toList(),
                facilities = facilities!!.map { FacilitiesResponseDto(it) }
                    .sortedBy(FacilitiesResponseDto::priority)
                    .toList()
            )
        }
    }

}

data class ImageResponseDto(

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long = 0,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long = 100,

    @field:Schema(description = "이미지 URL", example = "image-url", required = true)
    val imageUrl: String = "",
) {

    companion object {
        operator fun invoke(image: Image) = with(image) {
            ImageResponseDto(
                lodgingCompanyId = lodgingCompany?.id!!,
                priority = priority!!,
                imageUrl = imageUrl
            )
        }
    }

}

data class FacilitiesResponseDto(

    @field:Schema(description = "숙박업체 아이디", example = "1", required = true)
    val lodgingCompanyId: Long = 0,

    @field:Schema(description = "시설명", example = "주차가능", required = true)
    val name: String = "",

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long = 100,

    @field:Schema(description = "아이콘이미지 URL", example = "icon-image-url", required = true)
    val iconImageUrl: String = "",
) {

    companion object {
        operator fun invoke(facilities: Facilities) = with(facilities) {
            FacilitiesResponseDto(
                lodgingCompanyId = lodgingCompany?.id!!,
                name = name,
                priority = priority!!,
                iconImageUrl = iconImageUrl,
            )
        }
    }
}

@Relation(collectionRelation = "lodgingCompanies")
@JsonInclude(Include.NON_NULL)
data class LodgingCompanyAndRoomResponseDto(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "카테고리 아이디", example = "1", required = true)
    val categoryId: Long,

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
    val rooms: List<RoomResponseDto>? = listOf(),

    @field:Schema(description = "이미지")
    val images: List<ImageResponseDto>? = listOf(),

    @field:Schema(description = "시설")
    val facilities: List<FacilitiesResponseDto>? = listOf(),

    @field:JsonFormat(
        shape = Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        timezone = "Asia/Seoul"
    )
    @field:Schema(description = "등록일", example = "2022-08-08T21:51:00.797659")
    val createdAt: LocalDateTime,
) {

    companion object {
        operator fun invoke(
            searched: SearchedLodgingCompany,
            rooms: List<RoomResponseDto>,
            checkIn: String,
            checkOut: String
        ) = with(searched) {
            LodgingCompanyAndRoomResponseDto(
                id = id!!,
                categoryId = categoryId,
                type = type,
                status = status,
                name = name,
                thumbnail = thumbnail,
                businessNumber = businessNumber,
                tellNumber = tellNumber,
                address = address,
                searchTag = searchTag,
                reviewAverageScore = reviewAverageScore!!,
                reviewCount = reviewCount!!,
                checkIn = checkIn,
                checkOut = checkOut,
                rooms = rooms,
                images = images,
                facilities = facilities,
                createdAt = createdAt,
            )
        }
    }

}

@Relation(collectionRelation = "lodgingCompanies")
@JsonInclude(Include.NON_NULL)
data class LodgingCompanyListingResponseDto(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "카테고리 아이디", example = "1", required = true)
    val categoryId: Long,

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

    companion object {
        operator fun invoke(searched: SearchedLodgingCompanyListing) = with(searched) {
            LodgingCompanyListingResponseDto(
                id = id!!,
                categoryId = categoryId!!,
                type = type,
                status = status,
                name = name!!,
                thumbnail = thumbnail!!,
                address = address!!,
                price = price!!,
                searchTag = searchTag!!,
                reviewAverageScore = reviewAverageScore!!,
                reviewCount = reviewCount!!,
                createdAt = createdAt!!,
            )
        }
    }

}

