package com.hyuuny.norja.domain.command

import com.hyuuny.norja.domain.Facilities

data class FacilitiesCreateCommand(
    val name: String,
    val iconImageUrl: String,
    val priority: Long?,
) {
    val toEntity = Facilities.create(name = name, iconImageUrl = iconImageUrl, priority = priority)
}
