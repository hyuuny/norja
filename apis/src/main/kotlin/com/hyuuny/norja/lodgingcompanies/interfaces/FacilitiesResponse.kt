package com.hyuuny.norja.lodgingcompanies.interfaces

import com.hyuuny.norja.domain.info.FacilitiesInfo

data class FacilitiesResponse(
    val id: Long,
    val lodgingCompanyId: Long?,
    val name: String,
    val priority: Long,
    val iconImageUrl: String?,
) {
    constructor(info: FacilitiesInfo) : this(
        id = info.id,
        lodgingCompanyId = info.lodgingCompanyId,
        name = info.name,
        priority = info.priority!!,
        iconImageUrl = info.iconImageUrl,
    )
}