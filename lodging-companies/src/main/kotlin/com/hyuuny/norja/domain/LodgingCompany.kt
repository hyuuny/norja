package com.hyuuny.norja.domain

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.jpa.domain.BaseEntity
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType.LAZY
import javax.persistence.OneToMany

@Entity
class LodgingCompany private constructor(
    type: Type,
    status: Status = Status.OPEN,
    name: String,
    thumbnail: String,
    businessNumber: String,
    tellNumber: String,
    address: Address,
    searchTag: String? = null,
    images: MutableList<Image>? = mutableListOf(),
    facilities: MutableList<Facilities>? = mutableListOf(),
) : BaseEntity() {

    companion object {
        fun create(
            type: Type,
            name: String,
            thumbnail: String,
            businessNumber: String,
            tellNumber: String,
            address: Address,
            searchTag: String? = null,
            images: MutableList<Image>? = mutableListOf(),
            facilities: MutableList<Facilities>? = mutableListOf(),
        ) = LodgingCompany(
            type = type,
            name = name,
            status = Status.OPEN,
            thumbnail = thumbnail,
            businessNumber = businessNumber,
            tellNumber = tellNumber,
            address = address,
            searchTag = searchTag,
            images = images,
            facilities = facilities
        )
    }

    @Enumerated(EnumType.STRING)
    var type = type
        private set

    @Enumerated(EnumType.STRING)
    var status = status
        private set

    var name = name
        private set

    var thumbnail = thumbnail
        private set

    var businessNumber = businessNumber
        private set

    var tellNumber = tellNumber
        private set

    var address = address
        private set

    var searchTag = searchTag
        private set

    @OneToMany(mappedBy = "lodgingCompany", cascade = [ALL], fetch = LAZY, orphanRemoval = true)
    var images = images
        private set

    @OneToMany(mappedBy = "lodgingCompany", cascade = [ALL], fetch = LAZY, orphanRemoval = true)
    var facilities = facilities
        private set

    fun changeType(type: Type) {
        this.type = type
    }

    fun changeName(name: String) {
        this.name = name
    }

    fun changeThumbnail(thumbnail: String) {
        this.thumbnail = thumbnail
    }

    fun changeBusinessNumber(businessNumber: String) {
        this.businessNumber = businessNumber
    }

    fun changeTellNumber(tellNumber: String) {
        this.tellNumber = tellNumber
    }

    fun changeAddress(address: Address) {
        this.address = address
    }

    fun changeSearchTag(searchTag: String?) {
        this.searchTag = searchTag
    }

    fun addImages(image: Image) = image.assignLodgingCompany(this)

    fun addFacilities(facilities: Facilities) = facilities.assignLodgingCompany(this)

    fun imagesClear() {
        this.images?.toMutableList()?.clear()
    }

    fun facilitiesClear() {
        this.facilities?.toMutableList()?.clear()
    }

    fun closed() {
        this.status = Status.CLOSED
    }

    fun delete() {
        this.status = Status.DELETED
    }

}