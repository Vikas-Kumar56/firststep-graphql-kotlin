package com.basic101.firststep.service

import com.basic101.firststep.model.UserEntity
import com.basic101.firststep.repository.PostRepository
import com.basic101.firststep.repository.UserRepository
import com.basic101.firststep.resolver.AddUserInput
import com.basic101.firststep.resolver.Post
import com.basic101.firststep.resolver.User
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
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
          name = userInput.name
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
}