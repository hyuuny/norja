package com.hyuuny.norja.domain

import com.hyuuny.norja.jpa.domain.BaseEntity
import org.springframework.util.ObjectUtils.isEmpty
import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
class Category private constructor(
    name: String,
    priority: Long,
    level: Int,
    iconImageUrl: String,
    parent: Category? = null,
    children: MutableList<Category>? = mutableListOf(),
) : BaseEntity() {

    companion object {
        fun create(
            name: String,
            priority: Long,
            level: Int,
            iconImageUrl: String,
        ) = Category(
            name = name,
            priority = priority,
            level = level,
            iconImageUrl = iconImageUrl,
        )
    }

    var name = name
        private set

    var priority = priority
        private set

    var level = level
        private set

    var iconImageUrl = iconImageUrl
        private set

    @ManyToOne(fetch = LAZY)
    var parent = parent
        private set

    @OneToMany(mappedBy = "parent", cascade = [ALL], fetch = LAZY, orphanRemoval = true)
    var children = children
        private set

    val parentId
        get() = this.parent?.id

    fun changeName(name: String) {
        this.name = name
    }

    fun changePriority(priority: Long) {
        this.priority = priority
    }

    fun changeLevel(level: Int) {
        this.level = level
    }

    fun changeIconImageUrl(iconImageUrl: String) {
        this.iconImageUrl = iconImageUrl
    }

    fun assignCategory(parent: Category) {
        if (!isEmpty(this.parent)) {
            this.parent?.children?.remove(this)
        }
        this.parent = parent
        this.parent?.children?.add(this)
    }

}