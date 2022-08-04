package com.hyuuny.reservation.domain

import com.hyuuny.norja.reservation.domain.Reservation
import com.hyuuny.norja.reservation.domain.Status
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
        val expectedStatus = Status.COMPLETION
        val expectedCheckIn = LocalDate.of(2022, 8, 4)
        val expectedCheckOut = LocalDate.of(2022, 8, 10)

        val newReservation = FixtureReservation.aReservation()

        newReservation.userId shouldBe expectedUserId
        newReservation.roomId shouldBe expectedRoomId
        newReservation.status shouldBe expectedStatus
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
        val newReservation = FixtureReservation.aReservation(checkOut = LocalDate.now().minusDays(1))
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
            checkIn: LocalDate = LocalDate.of(2022, 8, 4),
            checkOut: LocalDate = LocalDate.of(2022, 8, 10),
        ) =
            Reservation.create(userId, roomId, checkIn, checkOut)
    }
}