package com.hyuuny.norja.domain

class CategoryCommand

data class CategoryCreateCommand(
    val name: String,
    val priority: Long,
    val level: Int,
    val iconImageUrl: String,
) {
    val toEntity: Category
        get() = Category.create(
            name = this.name,
            priority = this.priority,
            level = this.level,
            iconImageUrl = this.iconImageUrl
        )
}

data class CategoryUpdateCommand(
    val name: String,
    val priority: Long,
    val level: Int,
    val iconImageUrl: String,
) {
    fun update(entity: Category) {
        entity.changeName(this.name)
        entity.changePriority(this.priority)
        entity.changeLevel(this.level)
        entity.changeIconImageUrl(this.iconImageUrl)
    }
}
