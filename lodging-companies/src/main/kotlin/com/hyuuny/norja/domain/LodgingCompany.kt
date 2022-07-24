package com.hyuuny.norja.domain

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.domain.command.LodgingCompanyUpdateCommand
import com.hyuuny.norja.jpa.domain.BaseEntity
import net.bytebuddy.implementation.bind.annotation.Default
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.FetchType.*

@Entity
class LodgingCompany private constructor(
    type: Type,
    name: String,
    thumbnail: String,
    businessNumber: String,
    tellNumber: String,
    address: Address,
    searchTag: String?,
    images: List<Image>?,
    facilities: List<Facilities>?,
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
            images: List<Image>? = listOf(),
            facilities: List<Facilities>? = listOf(),
        ) = LodgingCompany(
            type = type,
            name = name,
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

}