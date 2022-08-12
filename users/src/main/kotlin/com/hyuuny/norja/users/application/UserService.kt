package com.hyuuny.norja.users.application

import com.hyuuny.norja.users.domain.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserService(
    private val userStore: UserStore,
    private val userReader: UserReader,
    private val passwordEncoder: PasswordEncoder,
    private val signUpValidator: SignUpValidator,
) {

    @Transactional
    fun singUp(command: SignUpCommand): Long {
        val newUser = command.toEntity()
        newUser.signUp(passwordEncoder, signUpValidator)
        return userStore.signUp(newUser).id!!
    }

    @Transactional
    fun singUp(command: SignUpCommand, roles: String): Long {
        val newUser = command.toEntity()
        newUser.assignRoles(roles)
        newUser.signUp(passwordEncoder, signUpValidator)
        return userStore.signUp(newUser).id!!
    }

    fun getUser(id: Long): UserInfo {
        val loadedUser = userReader.getUser(id)
        return UserInfo(loadedUser)
    }

    @Transactional
    fun changePassword(id: Long, command: ChangePasswordCommand) {
        val loadedUser = userReader.getUser(id)
        loadedUser.changePassword(passwordEncoder, command.password)
    }

    @Transactional
    fun changeAgreed(id: Long, command: ChangeAgreedCommand) {
        val loadedUser = userReader.getUser(id)
        command.changeAgreed(loadedUser)
    }

    @Transactional
    fun updateUser(id: Long, command: UserUpdateCommand): UserInfo {
        val loadedUser = userReader.getUser(id)
        command.update(loadedUser)
        return getUser(id)
    }

    @Transactional
    fun leave(id: Long) {
        val loadedUser = userReader.getUser(id)
        loadedUser.leave()
    }


}