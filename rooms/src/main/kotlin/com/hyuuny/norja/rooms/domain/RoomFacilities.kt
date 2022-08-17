package com.hyuuny.norja.rooms.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import org.springframework.util.ObjectUtils.isEmpty
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne

@Entity
class RoomFacilities private constructor(
    room: Room? = null,
    val name: String,
    val iconImageUrl: String,
    val priority: Long?,
) : BaseEntity() {

    companion object {
        fun create(name: String, iconImageUrl: String, priority: Long? = 100) = RoomFacilities(
            name = name,
            iconImageUrl = iconImageUrl,
            priority = priority,
        )
    }

    val roomId
        get() = this.room?.id!!

    @ManyToOne(optional = true, fetch = LAZY)
    var room = room
        private set

    fun assignRoom(room: Room) {
        if (!isEmpty(this.room)) {
            this.room?.roomFacilities?.remove(this)
        }
        this.room = room
        this.room?.roomFacilities?.add(this)
    }

}