package com.hyuuny.norja.lodgingcompanies.interfaces.res

import com.hyuuny.norja.domain.info.FacilitiesInfo

data class FacilitiesResponse(
    val lodgingCompanyId: Long?,
    val name: String,
    val priority: Long,
    val iconImageUrl: String?,
) {
    constructor(info: FacilitiesInfo) : this(
        lodgingCompanyId = info.lodgingCompanyId,
        name = info.name,
        priority = info.priority!!,
        iconImageUrl = info.iconImageUrl,
    )
}