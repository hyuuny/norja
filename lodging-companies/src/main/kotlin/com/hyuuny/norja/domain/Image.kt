package com.hyuuny.norja.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import org.springframework.util.ObjectUtils.isEmpty
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne

@Entity
class Image private constructor(
    lodgingCompany: LodgingCompany? = null,
    val priority: Long? = 100,
    val imageUrl: String,
) : BaseEntity() {

    companion object {
        fun create(priority: Long? = 100, imageUrl: String) = Image(
            priority = priority,
            imageUrl = imageUrl,
        )
    }

    @ManyToOne(optional = false, fetch = LAZY)
    var lodgingCompany = lodgingCompany
        private set

    fun assignLodgingCompany(lodgingCompany: LodgingCompany) {
        if (!isEmpty(this.lodgingCompany)) {
            this.lodgingCompany?.images?.remove(this)
        }
        this.lodgingCompany = lodgingCompany
        this.lodgingCompany?.images?.add(this)
    }


}