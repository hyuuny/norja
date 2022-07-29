package com.hyuuny.norja.domain.command

import com.hyuuny.norja.domain.Image

data class ImageCreateCommand(
    val priority: Long?,
    val imageUrl: String,
) {
    val toEntity = Image.create(priority = this.priority, imageUrl = this.imageUrl)
}
