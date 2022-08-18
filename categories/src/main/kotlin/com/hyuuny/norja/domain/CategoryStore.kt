package com.hyuuny.norja.domain

interface CategoryStore {

    fun store(category: Category): Category

    fun delete(id: Long)

}