package com.hyuuny.norja.rooms.domain

data class SearchedRoom(
    val room: Room? = null,
    val reservedRoomCount: Long = 0,
) {

    fun remainingRoomCount() = this.room!!.roomCount.minus(this.reservedRoomCount)

}
