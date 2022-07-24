package com.hyuuny.norja.domain

import org.springframework.util.ObjectUtils.isEmpty
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne

class Facilities private constructor(
    lodgingCompany: LodgingCompany? = null,
    name: String,
    iconImageUrl: String? = null,
    priority: Long?,
) {

    companion object {
        fun create(name: String, iconImageUrl: String?, priority: Long? = 100) = Facilities(
            name = name,
            iconImageUrl = iconImageUrl,
            priority = priority,
        )
    }

    @ManyToOne(optional = false, fetch = LAZY)
    var lodgingCompany = lodgingCompany
        private set

    var name = name
        private set

    var iconImageUrl = iconImageUrl
        private set

    var priority = priority
        private set

    fun assignLodgingCompany(lodgingCompany: LodgingCompany) {
        if (!isEmpty(this.lodgingCompany)) {
            this.lodgingCompany?.facilities?.toMutableList()?.remove(this)
        }
        this.lodgingCompany = lodgingCompany
        this.lodgingCompany?.facilities?.toMutableList()?.add(this)
    }

}