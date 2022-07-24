package com.hyuuny.norja.domain

import org.springframework.util.ObjectUtils.isEmpty
import javax.persistence.FetchType
import javax.persistence.ManyToOne

class Image private constructor(
    lodgingCompany: LodgingCompany? = null,
    val priority: Long?,
    val imageUrl: String,
) {

    companion object {
        fun create(priority: Long? = 100, imageUrl: String) = Image(
            priority = priority,
            imageUrl = imageUrl,
        )
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    var lodgingCompany = lodgingCompany
        private set


    fun assignLodgingCompany(lodgingCompany: LodgingCompany) {
        if (!isEmpty(this.lodgingCompany)) {
            this.lodgingCompany?.images?.toMutableList()?.remove(this)
        }
        this.lodgingCompany = lodgingCompany
        this.lodgingCompany?.images?.toMutableList()?.add(this)
    }

}