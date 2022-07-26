package com.hyuuny.norja.domain.info

import com.hyuuny.norja.domain.Facilities

data class FacilitiesInfo(
    val id: Long,
    val lodgingCompanyId: Long,
    val name: String,
    val priority: Long?,
    val iconImageUrl: String?,
) {

    constructor(entity: Facilities) : this(
        id = entity.id!!,
        lodgingCompanyId = entity.lodgingCompany?.id!!,
        name = entity.name,
        priority = entity.priority,
        iconImageUrl = entity.iconImageUrl,
    )

}