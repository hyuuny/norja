package com.hyuuny.norja.reservations.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDate
import java.time.LocalDateTime

@Relation(collectionRelation = "reservation")
@JsonInclude(Include.NON_NULL)
data class ReservationResponse(
    @field:Schema(description = "예약 아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "예약코드", example = "R20220808180500539", required = true)
    val code: String,

    @field:Schema(description = "유저 아이디", example = "1", required = true)
    val userId: Long,

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long,

    @field:Schema(description = "객실 수", example = "25", required = true)
    val roomCount: Int,

    @field:Schema(description = "상태", example = "COMPLETION", required = true)
    val status: Status,

    @field:Schema(description = "금액", example = "130000", required = true)
    val price: Long,

    @field:Schema(description = "체크인", example = "2022-08-08", required = true)
    val checkIn: LocalDate,

    @field:Schema(description = "체크아웃", example = "2022-08-10", required = true)
    val checkOut: LocalDate,

    @field:Schema(description = "등록일", example = "2022-08-08T21:51:00.797659")
    val createdAt: LocalDateTime,
) {
    constructor(entity: Reservation) : this(
        id = entity.id!!,
        code = entity.code,
        userId = entity.userId,
        roomId = entity.roomId,
        roomCount = entity.roomCount,
        status = entity.status,
        price = entity.price,
        checkIn = entity.checkIn,
        checkOut = entity.checkOut,
        createdAt = entity.createdAt,
    )
}

@Relation(collectionRelation = "reservations")
@JsonInclude(Include.NON_NULL)
class ReservationListingResponse(

    @field:Schema(description = "예약 아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "예약코드", example = "R20220808180500539", required = true)
    val code: String,

    @field:Schema(description = "유저 아이디", example = "1", required = true)
    val userId: Long,

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long,

    @field:Schema(description = "객실 수", example = "25", required = true)
    val roomCount: Int,

    @field:Schema(description = "상태", example = "COMPLETION", required = true)
    val status: Status,

    @field:Schema(description = "금액", example = "130000", required = true)
    val price: Long,

    @field:Schema(description = "체크인", example = "2022-08-08", required = true)
    val checkIn: LocalDate,

    @field:Schema(description = "체크아웃", example = "2022-08-10", required = true)
    val checkOut: LocalDate,

    @field:Schema(description = "등록일", example = "2022-08-08T21:51:00.797659")
    val createdAt: LocalDateTime,
) {
    constructor(info: SearchedReservationListing) : this(
        id = info.id!!,
        code = info.code!!,
        userId = info.userId!!,
        roomId = info.roomId!!,
        roomCount = info.roomCount!!,
        status = info.status!!,
        price = info.price!!,
        checkIn = info.checkIn!!,
        checkOut = info.checkOut!!,
        createdAt = info.createdAt!!,
    )
}
