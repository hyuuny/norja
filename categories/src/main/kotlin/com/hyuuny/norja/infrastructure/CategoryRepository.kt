package com.hyuuny.norja.infrastructure

import com.hyuuny.norja.domain.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long>, CategoryRepositoryCustom {
}