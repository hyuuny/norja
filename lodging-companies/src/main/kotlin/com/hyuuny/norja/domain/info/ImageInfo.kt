package com.hyuuny.norja.domain.info

import com.hyuuny.norja.domain.Image

data class ImageInfo(
    val lodgingCompanyId: Long,
    val priority: Long?,
    val imageUrl: String,
) {

    constructor(entity: Image) : this(
        lodgingCompanyId = entity.lodgingCompany?.id!!,
        priority = entity.priority,
        imageUrl = entity.imageUrl
    )

}