package com.basic101.firststep.service

import com.basic101.firststep.repository.PostRepository
import com.basic101.firststep.resolver.Post
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository
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

    fun getPostsByAuthor(userId: UUID): List<Post> {
        return postRepository.findAllByAuthor_Id(userId)
            .map {
                Post(
                    id = it.id,
                    title = it.title,
                    description = it.description
                )
            }
    }
}