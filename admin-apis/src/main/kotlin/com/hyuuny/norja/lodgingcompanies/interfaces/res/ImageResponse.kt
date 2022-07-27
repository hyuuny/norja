package com.hyuuny.norja.lodgingcompanies.interfaces.res

import com.hyuuny.norja.domain.info.ImageInfo

data class ImageResponse(
    val id: Long,
    val lodgingCompanyId: Long,
    val priority: Long,
    val imageUrl: String,
) {
    constructor(info: ImageInfo) : this(
        id = info.id,
        lodgingCompanyId = info.lodgingCompanyId,
        priority = info.priority!!,
        imageUrl = info.imageUrl
    )
}