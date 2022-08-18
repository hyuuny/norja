package com.hyuuny.norja.infrastructure

import com.hyuuny.norja.domain.Category
import com.hyuuny.norja.domain.CategoryReader
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class CategoryReaderImpl(
    private val categoryRepository: CategoryRepository,
) : CategoryReader {

    override fun getCategory(id: Long): Category = categoryRepository.findByIdOrNull(id)
        ?: throw HttpStatusMessageException(
            HttpStatus.BAD_REQUEST,
            "category.id.notFound",
            id
        )

    override fun getAllCategories(): List<Category> =
        categoryRepository.findAll(Sort.by("level", "priority"))

    override fun getChildrenCategories(parentCategoryId: Long): List<Category> =
        categoryRepository.loadChildrenCategories(parentCategoryId)

}