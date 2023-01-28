package com.basic101.firststep.service

import com.basic101.firststep.repository.PostRepository
import com.basic101.firststep.resolver.Post
import org.springframework.data.domain.PageRequest
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