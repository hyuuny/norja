package com.hyuuny.norja.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import org.springframework.util.ObjectUtils.isEmpty
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne

@Entity
class Facilities private constructor(
    room: Room? = null,
    val name: String,
    val iconImageUrl: String? = null,
    val priority: Long?,
) : BaseEntity() {

    companion object {
        fun create(name: String, iconImageUrl: String?, priority: Long? = 100) = Facilities(
            name = name,
            iconImageUrl = iconImageUrl,
            priority = priority,
        )
    }

    @ManyToOne(optional = true, fetch = LAZY)
    var room = room
        private set

    fun assignRoom(room: Room) {
        if (!isEmpty(this.room)) {
            this.room?.facilities?.toMutableList()?.remove(this)
        }
        this.room = room
        this.room?.facilities?.toMutableList()?.add(this)
    }

}