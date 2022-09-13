package com.hyuuny.norja.application

import com.hyuuny.norja.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class CategoryService(
    private val categoryStore: CategoryStore,
    private val categoryReader: CategoryReader,
) {

    @Transactional
    fun createCategory(command: CategoryCreateCommand): Long {
        val newCategory = command.toEntity
        return categoryStore.store(newCategory).id!!
    }

    @Transactional
    fun createChildCategory(
        parentCategoryId: Long,
        command: CategoryCreateCommand
    ): CategoryResponseDto {
        val parentCategory = categoryReader.getCategory(parentCategoryId)
        val childCategory = command.toEntity
        childCategory.assignCategory(parentCategory)
        val savedChildCategoryId = categoryStore.store(childCategory).id!!
        return getCategory(savedChildCategoryId)
    }

    fun getCategory(id: Long): CategoryResponseDto {
        val loadedCategory = categoryReader.getCategory(id)
        return CategoryResponseDto(loadedCategory)
    }

    fun getAllCategories(): List<CategoryResponseDto> {
        val loadedCategories = categoryReader.getAllCategories()
        return toResponses(loadedCategories)
    }

    fun getChildrenCategories(parentCategoryId: Long): List<CategoryResponseDto> {
        val childrenCategories = categoryReader.getChildrenCategories(parentCategoryId)
        return toResponses(childrenCategories)
    }

    private fun toResponses(categories: List<Category>): List<CategoryResponseDto> =
        categories.stream()
            .map { CategoryResponseDto(it) }
            .toList()

    fun updateCategory(id: Long, command: CategoryUpdateCommand): CategoryResponseDto {
        val loadedCategory = categoryReader.getCategory(id)
        command.update(loadedCategory)
        return getCategory(id)
    }

    fun deleteCategory(id: Long) {
        val loadedCategory = categoryReader.getCategory(id)
        categoryStore.delete(loadedCategory.id!!)
    }

}