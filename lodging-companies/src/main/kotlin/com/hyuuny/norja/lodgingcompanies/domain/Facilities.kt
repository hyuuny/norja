package com.hyuuny.norja.lodgingcompanies.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import org.springframework.util.ObjectUtils.isEmpty
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne

@Entity
class Facilities private constructor(
    lodgingCompany: LodgingCompany? = null,
    val name: String,
    val iconImageUrl: String,
    val priority: Long?,
) : BaseEntity() {

    companion object {
        fun create(name: String, iconImageUrl: String, priority: Long? = 100) = Facilities(
            name = name,
            iconImageUrl = iconImageUrl,
            priority = priority,
        )
    }

    @ManyToOne(optional = false, fetch = LAZY)
    var lodgingCompany = lodgingCompany
        private set

    fun assignLodgingCompany(lodgingCompany: LodgingCompany) {
        if (!isEmpty(this.lodgingCompany)) {
            this.lodgingCompany?.facilities?.remove(this)
        }
        this.lodgingCompany = lodgingCompany
        this.lodgingCompany?.facilities?.add(this)
    }


}