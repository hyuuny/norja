package com.hyuuny.norja.lodgingcompanies.interfaces.req

import com.hyuuny.norja.domain.command.ImageCreateCommand

data class ImageCreateDto(
    val priority: Long?,
    val imageUrl: String,
) {
    val toCommand = ImageCreateCommand(priority = this.priority, imageUrl = this.imageUrl)
}
