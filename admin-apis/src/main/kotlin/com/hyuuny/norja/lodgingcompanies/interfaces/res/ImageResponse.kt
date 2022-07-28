package com.hyuuny.norja.lodgingcompanies.interfaces.res

import com.hyuuny.norja.domain.info.ImageInfo

data class ImageResponse(
    val lodgingCompanyId: Long,
    val priority: Long,
    val imageUrl: String,
) {
    constructor(info: ImageInfo) : this(
        lodgingCompanyId = info.lodgingCompanyId,
        priority = info.priority!!,
        imageUrl = info.imageUrl
    )
}