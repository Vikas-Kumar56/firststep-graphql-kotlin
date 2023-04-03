package com.basic101.firststep.service

import com.basic101.firststep.model.UserEntity
import com.basic101.firststep.repository.PostRepository
import com.basic101.firststep.repository.UserRepository
import com.basic101.firststep.resolver.AddUserInput
import com.basic101.firststep.resolver.Post
import com.basic101.firststep.resolver.User
import com.basic101.firststep.util.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${key}")
    private val secretKey: String
) {

    fun findByPostId(postId: UUID): User {
        val userEntity = userRepository.findByPostsId(postId)

        return User(
            id = userEntity.id,
            name = userEntity.name
        )
    }

    fun addUser(userInput: AddUserInput): UUID {
        val userEntity = UserEntity(
            name = userInput.name,
            password = passwordEncoder.encode(userInput.password),
            roles = userInput.roles
        )

        val user = userRepository.save(userEntity)

        user.id ?: throw RuntimeException("User id cant be null")

        return user.id
    }

    fun getUsers(page: Int, size: Int): List<User> {
        val users = userRepository.findAll(PageRequest.of(page, size))
        return users.map {
            User(
                id = it.id,
                name = it.name
            )
        }.toList()
    }

    fun findByCommentId(commentId: UUID?): User {
        commentId ?: throw RuntimeException("commentId cant be null")
        val userEntity = userRepository.findByCommentsId(commentId)
        return User(
            id = userEntity.id,
            name = userEntity.name
        )
    }

    fun login(username: String, password: String): String {
        val user = userRepository.findByName(username) ?:
                  throw RuntimeException("user does not exist")

        if(!passwordEncoder.matches(password, user.password)) {
            throw BadCredentialsException("Password is not correct")
        }

        return  JwtUtil.generateJwtToken(
            username = username,
            signedSecret = secretKey,
            roles = user.roles.split(", ")
        )

    }
}