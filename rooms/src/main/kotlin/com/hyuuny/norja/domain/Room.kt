package com.hyuuny.norja.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.OneToMany

@Entity
class Room(
    val lodgingCompanyId: Long,
    type: Type,
    name: String,
    standardPersonnel: Int = 2,
    maximumPersonnel: Int = 2,
    images: List<Image>,
    facilities: List<Facilities>,
    price: Long,
    content: String? = null,
) : BaseEntity() {

    companion object {
        fun create(
            lodgingCompanyId: Long,
            type: Type,
            name: String,
            standardPersonnel: Int,
            maximumPersonnel: Int,
            facilities: List<Facilities>,
            images: List<Image>,
            price: Long,
            content: String?,
        ) = Room(
            lodgingCompanyId = lodgingCompanyId,
            type = type,
            name = name,
            standardPersonnel = standardPersonnel,
            maximumPersonnel = maximumPersonnel,
            facilities = facilities,
            images = images,
            price = price,
            content = content,
        )
    }

    var type = type
        private set

    var name = name
        private set

    var standardPersonnel = standardPersonnel
        private set

    var maximumPersonnel = maximumPersonnel
        private set

    @OneToMany(mappedBy = "room", cascade = [ALL], fetch = LAZY, orphanRemoval = true)
    var facilities = facilities
        private set

    @OneToMany(mappedBy = "room", cascade = [ALL], fetch = LAZY, orphanRemoval = true)
    var images = images
        private set

    var price = price
        private set

    var content = content
        private set

    fun changeType(type: Type) {
        this.type = type
    }

    fun changeName(name: String) {
        this.name = name
    }

    fun changeStandardPersonnel(standardPersonnel: Int) {
        this.standardPersonnel = standardPersonnel
    }

    fun changeMaximumPersonnel(maximumPersonnel: Int) {
        this.maximumPersonnel = maximumPersonnel
    }

    fun changeFacilities(facilities: List<Facilities>) {
        this.facilities = facilities
    }

    fun changeImages(images: List<Image>) {
        this.images = images
    }

    fun changePrice(price: Long) {
        this.price = price
    }

    fun changeContent(content: String?) {
        this.content = content
    }

    fun addFacilities(facilities: Facilities) = facilities.assignRoom(this)

    fun addImages(image: Image) = image.assignRoom(this)

}