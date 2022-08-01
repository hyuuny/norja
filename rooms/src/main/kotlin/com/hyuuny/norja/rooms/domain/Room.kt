package com.hyuuny.norja.rooms.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.OneToMany

@Entity
class Room private constructor(
    lodgingCompanyId: Long,
    type: Type,
    name: String,
    roomCount: Int = 10,
    standardPersonnel: Int = 2,
    maximumPersonnel: Int = 2,
    price: Long,
    content: String? = null,
    roomImages: MutableList<RoomImage>? = mutableListOf(),
    roomFacilities: MutableList<RoomFacilities>? = mutableListOf(),
) : BaseEntity() {

    companion object {
        fun create(
            lodgingCompanyId: Long,
            type: Type,
            name: String,
            roomCount: Int = 10,
            standardPersonnel: Int,
            maximumPersonnel: Int,
            roomFacilities: MutableList<RoomFacilities>? = mutableListOf(),
            roomImages: MutableList<RoomImage>? = mutableListOf(),
            price: Long,
            content: String?,
        ) = Room(
            lodgingCompanyId = lodgingCompanyId,
            type = type,
            name = name,
            roomCount = roomCount,
            standardPersonnel = standardPersonnel,
            maximumPersonnel = maximumPersonnel,
            roomFacilities = roomFacilities,
            roomImages = roomImages,
            price = price,
            content = content,
        )
    }

    var lodgingCompanyId = lodgingCompanyId
        private set

    var type = type
        private set

    var name = name
        private set

    var roomCount = roomCount
        private set

    var standardPersonnel = standardPersonnel
        private set

    var maximumPersonnel = maximumPersonnel
        private set

    @OneToMany(mappedBy = "room", cascade = [ALL], fetch = LAZY, orphanRemoval = true)
    var roomFacilities = roomFacilities
        private set

    @OneToMany(mappedBy = "room", cascade = [ALL], fetch = LAZY, orphanRemoval = true)
    var roomImages = roomImages
        private set

    var price = price
        private set

    var content = content
        private set

    fun changeType(type: Type) {
        this.type = type
    }

    fun changeLodgingCompanyId(lodgingCompanyId: Long) {
        this.lodgingCompanyId = lodgingCompanyId
    }

    fun changeName(name: String) {
        this.name = name
    }

    fun changeRoomCount(roomCount: Int) {
        this.roomCount = roomCount
    }

    fun changeStandardPersonnel(standardPersonnel: Int) {
        this.standardPersonnel = standardPersonnel
    }

    fun changeMaximumPersonnel(maximumPersonnel: Int) {
        this.maximumPersonnel = maximumPersonnel
    }

    fun changePrice(price: Long) {
        this.price = price
    }

    fun changeContent(content: String?) {
        this.content = content
    }

    fun addFacilities(roomFacilities: RoomFacilities) = roomFacilities.assignRoom(this)

    fun addImages(roomImage: RoomImage) = roomImage.assignRoom(this)

    fun imagesClear() {
        this.roomImages!!.clear()
    }

    fun facilitiesClear() {
        this.roomFacilities!!.clear()
    }

}