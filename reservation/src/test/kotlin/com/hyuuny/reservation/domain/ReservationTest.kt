package com.hyuuny.reservation.domain

import com.hyuuny.norja.reservations.domain.Reservation
import com.hyuuny.norja.reservations.domain.Status
import com.hyuuny.norja.web.model.HttpStatusMessageException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ReservationTest {

    @Test
    fun `예약`() {
        val expectedUserId = 1L
        val expectedRoomId = 5L
        val expectedRoomCount = 2
        val expectedStatus = Status.COMPLETION
        val expectedPrice = 80_000
        val expectedCheckIn = LocalDate.now()
        val expectedCheckOut = LocalDate.now().plusDays(5)

        val newReservation = FixtureReservation.aReservation()

        newReservation.userId shouldBe expectedUserId
        newReservation.roomId shouldBe expectedRoomId
        newReservation.roomCount shouldBe expectedRoomCount
        newReservation.status shouldBe expectedStatus
        newReservation.price shouldBe expectedPrice
        newReservation.checkIn shouldBe expectedCheckIn
        newReservation.checkOut shouldBe expectedCheckOut
    }

    @Test
    fun `입실일이 현재보다 이전이면 예외`() {
        val expectedErrorMsg = "reservation.checkIn.notValid"
        val newReservation = FixtureReservation.aReservation(checkIn = LocalDate.now().minusDays(1))
        shouldThrow<HttpStatusMessageException> {
            newReservation.validate()
        }.shouldHaveMessage(expectedErrorMsg)
    }

    @Test
    fun `퇴실일이 입실일보다 이전이면 예외`() {
        val expectedErrorMsg = "reservation.checkOut.notValid"
        val newReservation =
            FixtureReservation.aReservation(checkOut = LocalDate.now().minusDays(1))
        shouldThrow<HttpStatusMessageException> {
            newReservation.validate()
        }.shouldHaveMessage(expectedErrorMsg)
    }

    @Test
    fun `예약 취소`() {
        val newReservation = FixtureReservation.aReservation()
        newReservation.cancel()
        newReservation.status shouldBe Status.CANCELLATION
    }

}

class FixtureReservation {
    companion object {
        fun aReservation(
            userId: Long = 1L,
            roomId: Long = 5L,
            roomCount: Int = 2,
            price: Long = 80_000,
            checkIn: LocalDate = LocalDate.now(),
            checkOut: LocalDate = LocalDate.now().plusDays(5),
        ) =
            Reservation.create(userId, roomId, roomCount, price, checkIn, checkOut)
    }
}