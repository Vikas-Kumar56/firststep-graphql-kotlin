package com.basic101.firststep.service

import com.basic101.firststep.repository.CommentRepository
import com.basic101.firststep.resolver.Comment
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentService(
    private val commentRepository: CommentRepository
) {
    fun getComments(page: Int, size: Int): List<Comment> {
        val pageRequest = PageRequest.of(page, size)
        val commentEntities = commentRepository.findAll(pageRequest)
        return commentEntities.map {
            Comment(
                id = it.id,
                text = it.text,
            )
        }.toList()
    }

    fun getCommentsByPostId(postId: UUID?): List<Comment> {
        postId ?: throw RuntimeException("post id cant be null")
        return commentRepository.findAllByPostId(postId)
            .map {
                Comment(
                    id = it.id,
                    text = it.text
                )
            }.toList()
    }

    fun getCommentsByUserId(userId: UUID?): List<Comment> {
        userId ?: throw RuntimeException("userId cant be null")
        return commentRepository.findAllByAuthorId(userId)
            .map {
                Comment(
                    id = it.id,
                    text = it.text
                )
            }.toList()
    }
}