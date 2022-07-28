package com.hyuuny.norja.lodgingcompanies.interfaces.req

import com.hyuuny.norja.domain.command.ImageCreateCommand

data class ImageCreateDto(
    val priority: Long? = 100,
    val imageUrl: String,
) {
    val toCommand = ImageCreateCommand(priority = this.priority, imageUrl = this.imageUrl)
}
