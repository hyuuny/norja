package com.hyuuny.norja.users.infrastructure

import com.hyuuny.norja.jpa.support.CustomQueryDslRepository
import com.hyuuny.norja.users.domain.QUser.user
import com.hyuuny.norja.users.domain.SearchedUser
import com.hyuuny.norja.users.domain.Status
import com.hyuuny.norja.users.domain.User
import com.hyuuny.norja.users.domain.UserSearchQuery
import com.querydsl.core.types.Projections.fields
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.ObjectUtils.isEmpty

class UserRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomQueryDslRepository(User::class.java), UserRepositoryCustom {

    override fun retrieveUser(
        searchQuery: UserSearchQuery,
        pageable: Pageable
    ): PageImpl<SearchedUser> {
        return applyPageImpl(
            pageable, queryFactory.select(
                fields(
                    SearchedUser::class.java,
                    user.id,
                    user.username,
                    user.nickname,
                    user.status,
                    user.phoneNumber,
                    user.agreedTermsOfService,
                    user.agreedPrivacyPolicy,
                    user.agreedReceiveMessage,
                    user.createdAt,
                )
            )
                .from(user)
                .where(
                    idEq(searchQuery.id),
                    usernameContains(searchQuery.username),
                    statusEq(searchQuery.status),
                )
        )
    }

    private fun idEq(id: Long?) = if (isEmpty(id)) null else user.id.eq(id)

    private fun usernameContains(username: String?) =
        if (isEmpty(username)) null else user.username.contains(username)

    private fun statusEq(status: Status?) = if (isEmpty(status)) null else user.status.eq(status)

}