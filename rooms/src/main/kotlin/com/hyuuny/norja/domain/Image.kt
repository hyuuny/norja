package com.hyuuny.norja.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import org.springframework.util.ObjectUtils.isEmpty
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne

@Entity
class Image private constructor(
    room: Room? = null,
    val priority: Long?,
    val imageUrl: String,
) : BaseEntity() {

    companion object {
        fun create(priority: Long? = 100, imageUrl: String) = Image(
            priority = priority,
            imageUrl = imageUrl,
        )
    }

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    var room = room
        private set

    fun assignRoom(room: Room) {
        if (!isEmpty(this.room)) {
            this.room?.images?.remove(this)
        }
        this.room = room
        this.room?.images?.add(this)
    }

}