package com.hyuuny.norja.reservations.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDate
import java.time.LocalDateTime

@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@class")
@Relation(collectionRelation = "reservation")
@JsonInclude(Include.NON_NULL)
data class ReservationResponseDto(
    @field:Schema(description = "예약 아이디", example = "1", required = true)
    val id: Long = 0,

    @field:Schema(description = "예약코드", example = "R20220808180500539", required = true)
    val code: String = "",

    @field:Schema(description = "유저 아이디", example = "1", required = true)
    val userId: Long = 0,

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long = 0,

    @field:Schema(description = "객실 수", example = "25", required = true)
    val roomCount: Int = 0,

    @field:Schema(description = "상태", example = "COMPLETION", required = true)
    val status: Status = Status.COMPLETION,

    @field:Schema(description = "금액", example = "130000", required = true)
    val price: Long = 0,

    @field:JsonDeserialize(using = LocalDateDeserializer::class)
    @field:JsonSerialize(using = LocalDateSerializer::class)
    @field:JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @field:Schema(description = "체크인", example = "2022-08-08", required = true)
    val checkIn: LocalDate = LocalDate.now(),

    @field:JsonDeserialize(using = LocalDateDeserializer::class)
    @field:JsonSerialize(using = LocalDateSerializer::class)
    @field:JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @field:Schema(description = "체크아웃", example = "2022-08-10", required = true)
    val checkOut: LocalDate = LocalDate.now(),

    @field:JsonDeserialize(using = LocalDateTimeDeserializer::class)
    @field:JsonSerialize(using = LocalDateTimeSerializer::class)
    @field:JsonFormat(
        shape = Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        timezone = "Asia/Seoul"
    )
    @field:Schema(description = "등록일", example = "2022-08-08T21:51:00.797659")
    val createdAt: LocalDateTime = LocalDateTime.now(),
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
class ReservationListingResponseDto(

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
