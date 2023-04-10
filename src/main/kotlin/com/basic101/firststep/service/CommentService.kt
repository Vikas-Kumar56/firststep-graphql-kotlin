package com.basic101.firststep.service

import com.basic101.firststep.model.CommentEntity
import com.basic101.firststep.repository.CommentRepository
import com.basic101.firststep.repository.PostRepository
import com.basic101.firststep.repository.UserRepository
import com.basic101.firststep.resolver.AddCommentDto
import com.basic101.firststep.resolver.Comment
import com.basic101.firststep.resolver.Post
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
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

    fun addComment(addComment: AddCommentDto): Comment {
       val loggedInUsername = SecurityContextHolder.getContext().authentication.name
       val user = userRepository.findByName(loggedInUsername)
           ?: throw RuntimeException("user does not exist with id: $loggedInUsername")

        val post = postRepository.findById(addComment.postId).orElseThrow() {
            RuntimeException("Post does not exist with id: ${addComment.postId}")
        }

       val comment = CommentEntity(
           text = addComment.text,
           author = user,
           post = post
       )

       val createdComment = commentRepository.save(comment)

        return Comment(
            id = createdComment.id,
            text = createdComment.text
        )
    }

    fun getCommentsByPosts(posts: List<Post>): Map<Post, List<Comment>> {
       val comments = commentRepository.findAllByPostIds(
           posts.mapNotNull { it.id }.toList())

        return posts.associateWith { post ->
            comments.filter { comment -> comment.post.id == post.id }
                .map { comment -> Comment(id = comment.id, text = comment.text) }
                .toList()
        }
    }
}