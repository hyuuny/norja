package com.hyuuny.norja.reservations.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import com.hyuuny.norja.utlis.CodeGenerator
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.http.HttpStatus
import java.time.LocalDate
import javax.persistence.Entity

@Entity
class Reservation private constructor(
    val code: String,
    val userId: Long,
    val roomId: Long,
    status: Status,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
) : BaseEntity() {

    companion object {
        fun create(userId: Long, roomId: Long, checkIn: LocalDate, checkOut: LocalDate) =
            Reservation(
                code = CodeGenerator.createCode(),
                status = Status.COMPLETION,
                userId = userId,
                roomId = roomId,
                checkIn = checkIn,
                checkOut = checkOut,
            )
    }

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