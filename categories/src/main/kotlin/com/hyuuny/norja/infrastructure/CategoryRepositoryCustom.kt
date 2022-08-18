package com.hyuuny.norja.infrastructure

import com.hyuuny.norja.domain.Category

interface CategoryRepositoryCustom {

    fun loadChildrenCategories(parentCategoryId: Long): List<Category>

}