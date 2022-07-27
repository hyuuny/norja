package com.hyuuny.norja.lodgingcompanies.interfaces.req

import com.hyuuny.norja.domain.command.FacilitiesCreateCommand

data class FacilitiesCreateDto(
    val name: String,
    val iconImageUrl: String,
    val priority: Long?,
) {
    val toCommand = FacilitiesCreateCommand(
        name = this.name,
        iconImageUrl = this.iconImageUrl,
        priority = this.priority
    )
}
