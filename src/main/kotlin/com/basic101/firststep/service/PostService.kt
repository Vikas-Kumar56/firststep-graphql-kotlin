package com.basic101.firststep.service

import com.basic101.firststep.model.PostEntity
import com.basic101.firststep.repository.PostRepository
import com.basic101.firststep.repository.UserRepository
import com.basic101.firststep.resolver.AddPost
import com.basic101.firststep.resolver.Post
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {

    fun getPosts(): List<Post> {
      return postRepository.findAll().map {
          Post(
              id = it.id,
              title = it.title,
              description = it.description
          )
      }
    }

    fun getPosts(page: Int, size: Int): List<Post> {
        val page = PageRequest.of(page, size)
       return postRepository.findAll(page)
            .map {
                Post(
                    id = it.id,
                    title = it.title,
                    description = it.description
                )
            }.toList()
    }

    fun getPostsByAuthor(userId: UUID): List<Post> {
        return postRepository.findAllByAuthorId(userId)
            .map {
                Post(
                    id = it.id,
                    title = it.title,
                    description = it.description
                )
            }
    }

    fun addPost(addPost: AddPost): Post {
        val loggedInUsername = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByName(loggedInUsername)
            ?: throw RuntimeException("UserId is not valid, userId: $loggedInUsername")

        val postEntity = PostEntity(
            title = addPost.title,
            description = addPost.description,
            author = user
        )

        val createdPost = postRepository.save(postEntity)

        return Post(
            id = createdPost.id,
            title = createdPost.title,
            description = createdPost.description
        )
    }

    fun getPostByCommentId(commentId: UUID?): Post {
        commentId ?: throw RuntimeException("commentId cant not be null")
        val postEntity = postRepository.findByCommentsId(commentId)
        return Post(
            id = postEntity.id,
            title = postEntity.title,
            description = postEntity.description
        )
    }
}