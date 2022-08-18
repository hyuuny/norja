package com.hyuuny.norja.infrastructure

import com.hyuuny.norja.domain.Category
import com.hyuuny.norja.domain.CategoryStore
import org.springframework.stereotype.Component

@Component
class CategoryStoreImpl(
    private val categoryRepository: CategoryRepository,
) : CategoryStore {

    override fun store(category: Category) = categoryRepository.save(category)

    override fun delete(id: Long) = categoryRepository.deleteById(id)

}