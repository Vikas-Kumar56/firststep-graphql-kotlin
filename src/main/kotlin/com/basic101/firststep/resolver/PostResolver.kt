package com.basic101.firststep.resolver

import com.basic101.firststep.service.PostService
import com.basic101.firststep.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import java.lang.RuntimeException
import java.util.UUID

@Controller
class PostResolver(
    private val postService: PostService
) {

    @PreAuthorize("hasAnyRole('VIEWER', 'ADMIN')")
    @QueryMapping
    fun getPosts(): List<Post> {
        LOGGER.info("Fetching posts from database")
        return postService.getPosts()
    }

    @PreAuthorize("hasAnyRole('VIEWER', 'ADMIN')")
    @QueryMapping
    fun recentPosts(@Argument page: Int,@Argument size: Int): List<Post> {
        return postService.getPosts(page, size)
    }

    @PreAuthorize("hasAnyRole('VIEWER', 'ADMIN')")
    @SchemaMapping(typeName = "User")
    fun posts(user: User): List<Post> {
        val userId = user.id ?: throw RuntimeException("UserId cant not be null")
        return postService.getPostsByAuthor(userId)
    }

    @PreAuthorize("hasAnyRole('VIEWER', 'ADMIN')")
    @SchemaMapping(typeName = "User")
    fun totalPost(user: User): Int {
        val userId = user.id ?: throw RuntimeException("UserId cant not be null")
        return postService.getPostsByAuthor(userId).size
    }


    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    fun addPost(@Argument("addPostInput") addPost: AddPost): Post {
        return postService.addPost(addPost)
    }

    @PreAuthorize("hasAnyRole('VIEWER', 'ADMIN')")
    @SchemaMapping(typeName = "Comment")
    fun post(comment: Comment): Post {
        return postService.getPostByCommentId(comment.id)
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)
    }
}

data class Post(
    val id: UUID?,
    val title: String,
    val description: String?
)

data class AddPost(
    val title: String,
    val description: String?,
    val authorId: UUID
)