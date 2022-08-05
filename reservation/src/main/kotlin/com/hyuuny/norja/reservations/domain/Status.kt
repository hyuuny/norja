package com.hyuuny.norja.reservations.domain

enum class Status(val title: String) {
    COMPLETION("예약완료"),
    CANCELLATION("예약취소"),
}