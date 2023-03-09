package com.basic101.firststep.resolver

import com.basic101.firststep.service.CommentService
import org.springframework.graphql.data.method.annotation.Argument
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

    @SchemaMapping(typeName = "Post")
    fun comments(post: Post): List<Comment> {
        return commentService.getCommentsByPostId(post.id)
    }

    @SchemaMapping(typeName = "User")
    fun comments(user: User): List<Comment> {
        return commentService.getCommentsByUserId(user.id)
    }

    @MutationMapping
    fun addComment(@Argument("addCommentInput") addComment: AddCommentDto): Comment {
        return commentService.addComment(addComment)
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