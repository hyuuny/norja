package com.hyuuny.norja.reservations.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import com.hyuuny.norja.utlis.CodeGenerator
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.http.HttpStatus
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class Reservation private constructor(
    val code: String,
    val userId: Long,
    val roomId: Long,
    val roomCount: Int,
    status: Status,
    val price: Long,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
) : BaseEntity() {

    companion object {
        fun create(
            userId: Long,
            roomId: Long,
            roomCount: Int,
            price: Long,
            checkIn: LocalDate,
            checkOut: LocalDate
        ) =
            Reservation(
                code = CodeGenerator.createCode(),
                status = Status.COMPLETION,
                userId = userId,
                roomId = roomId,
                roomCount = roomCount,
                price = price,
                checkIn = checkIn,
                checkOut = checkOut,
            )
    }

    @Enumerated(EnumType.STRING)
    var status = status
        private set

    fun cancel() {
        this.status = Status.CANCELLATION
    }

    fun validate() {
        if (LocalDate.now().isAfter(this.checkIn)) {
            throw HttpStatusMessageException(HttpStatus.BAD_REQUEST, "reservation.checkIn.notValid")
        }

        if (this.checkIn.isAfter(this.checkOut)) {
            throw HttpStatusMessageException(
                HttpStatus.BAD_REQUEST,
                "reservation.checkOut.notValid"
            )
        }
    }

}