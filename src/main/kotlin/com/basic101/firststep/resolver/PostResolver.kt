package com.basic101.firststep.resolver

import com.basic101.firststep.service.PostService
import com.basic101.firststep.service.UserService
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.lang.RuntimeException
import java.util.UUID

@Controller
class PostResolver(
    private val userService: UserService,
    private val postService: PostService
) {

    @QueryMapping
    fun getPosts(): List<Post> {
        return postService.getPosts()
    }

    // field resolver
    @SchemaMapping(typeName = "Post")
    fun author(post: Post): User {
        val postId = post.id ?: throw RuntimeException("postId cant not be null")
        return userService.findByPostId(postId)
    }

    @SchemaMapping(typeName = "User")
    fun posts(user: User): List<Post> {
        val userId = user.id ?: throw RuntimeException("UserId cant not be null")
        return postService.getPostsByAuthor(userId)
    }
}

data class Post(
    val id: UUID?,
    val title: String,
    val description: String?
)

data class User(
    val id: UUID?,
    val name: String
)