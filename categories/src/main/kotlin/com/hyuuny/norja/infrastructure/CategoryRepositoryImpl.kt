package com.hyuuny.norja.infrastructure

import com.hyuuny.norja.domain.Category
import com.hyuuny.norja.domain.QCategory.category
import com.hyuuny.norja.jpa.support.CustomQueryDslRepository
import com.querydsl.jpa.impl.JPAQueryFactory

class CategoryRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomQueryDslRepository(Category::class.java), CategoryRepositoryCustom {

    override fun loadChildrenCategories(parentCategoryId: Long): List<Category> = queryFactory
        .selectFrom(category)
        .where(
            parentCategoryIdEq(parentCategoryId),
        )
        .orderBy(category.priority.asc())
        .fetch()

    private fun parentCategoryIdEq(parentCategoryId: Long) =
        category.parent.id.eq(parentCategoryId)

}