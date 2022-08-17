package com.hyuuny.norja.rooms.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import org.springframework.util.ObjectUtils.isEmpty
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne

@Entity
class RoomImage private constructor(
    room: Room? = null,
    val priority: Long?,
    val imageUrl: String,
) : BaseEntity() {

    companion object {
        fun create(priority: Long? = 100, imageUrl: String) = RoomImage(
            priority = priority,
            imageUrl = imageUrl,
        )
    }

    val roomId
        get() = this.room?.id!!

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    var room = room
        private set

    fun assignRoom(room: Room) {
        if (!isEmpty(this.room)) {
            this.room?.roomImages?.remove(this)
        }
        this.room = room
        this.room?.roomImages?.add(this)
    }

}