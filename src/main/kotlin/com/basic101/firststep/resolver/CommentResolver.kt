package com.basic101.firststep.resolver

import com.basic101.firststep.service.CommentService
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class CommentResolver(
    private val commentService: CommentService
) {

    @QueryMapping
    fun getComments(@Argument("page") page: Int, @Argument size: Int): List<Comment> {
         return commentService.getComments(page, size)
    }

    @BatchMapping
    fun comments(posts: List<Post>): Map<Post, List<Comment>> {
        LOGGER.info("fetching comments for postIds: (${posts.map { it.id }})")

        return commentService.getCommentsByPosts(posts)
    }

//    @SchemaMapping(typeName = "Post")
//    fun comments(post: Post): List<Comment> {
//        LOGGER.info("Fetching comments for postId: ${post.id}")
//        return commentService.getCommentsByPostId(post.id)
//    }

    @SchemaMapping(typeName = "User")
    fun comments(user: User): List<Comment> {
        return commentService.getCommentsByUserId(user.id)
    }

    @MutationMapping
    fun addComment(@Argument("addCommentInput") addComment: AddCommentDto): Comment {
        return commentService.addComment(addComment)
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)
    }

}

data class Comment(
    val id: UUID?,
    val text: String,
)

data class AddCommentDto(
    val text: String,
    val authorId: UUID,
    val postId: UUID
)