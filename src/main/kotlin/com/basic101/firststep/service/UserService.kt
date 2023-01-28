package com.basic101.firststep.service

import com.basic101.firststep.model.UserEntity
import com.basic101.firststep.repository.PostRepository
import com.basic101.firststep.repository.UserRepository
import com.basic101.firststep.resolver.AddUserInput
import com.basic101.firststep.resolver.Post
import com.basic101.firststep.resolver.User
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) {

    fun findByPostId(postId: UUID): User {
        val postEntity = postRepository.findById(postId).orElseThrow {
            RuntimeException("Post does not exist for this user postId: $postId ")
        }

        return User(
            id = postEntity.author.id,
            name = postEntity.author.name
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
}