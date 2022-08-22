package com.hyuuny.norja.lodgingcompanies.domain

data class SearchedLodgingCompany(
    val lodgingCompany: LodgingCompany? = null,
    val reviewCount: Long? = null,
    val reviewAverageScore: Double? = null,
) {
    val id
        get() = this.lodgingCompany!!.id

    val categoryId
        get() = this.lodgingCompany!!.categoryId

    val type
        get() = this.lodgingCompany!!.type

    val status
        get() = this.lodgingCompany!!.status

    val name
        get() = this.lodgingCompany!!.name

    val thumbnail
        get() = this.lodgingCompany!!.thumbnail

    val businessNumber
        get() = this.lodgingCompany!!.businessNumber

    val tellNumber
        get() = this.lodgingCompany!!.tellNumber

    val address
        get() = this.lodgingCompany!!.address

    val searchTag
        get() = this.lodgingCompany!!.searchTag

    val images
        get() = this.lodgingCompany!!.images!!.stream()
            .map(::ImageResponse)
            .sorted((Comparator.comparing(ImageResponse::priority)))
            .toList()

    val facilities
        get() = this.lodgingCompany!!.facilities!!.stream()
            .map(::FacilitiesResponse)
            .sorted((Comparator.comparing(FacilitiesResponse::priority)))
            .toList()

    val createdAt
        get() = this.lodgingCompany!!.createdAt
}
