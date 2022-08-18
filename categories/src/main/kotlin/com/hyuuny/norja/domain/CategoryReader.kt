package com.hyuuny.norja.domain

interface CategoryReader {

    fun getCategory(id: Long): Category

    fun getAllCategories(): List<Category>

    fun getChildrenCategories(parentCategoryId: Long): List<Category>

}