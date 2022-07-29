package com.hyuuny.norja.domain.info

import com.hyuuny.norja.domain.Image

data class ImageInfo(
    val roomId: Long,
    val priority: Long?,
    val imageUrl: String,
){
    constructor(entity:Image):this(
        roomId = entity.room?.id!!,
        priority = entity.priority,
        imageUrl = entity.imageUrl,
    )
}
