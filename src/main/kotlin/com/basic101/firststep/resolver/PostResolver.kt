package com.basic101.firststep.resolver

import com.basic101.firststep.service.PostService
import com.basic101.firststep.service.UserService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
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

    @QueryMapping
    fun recentPosts(@Argument page: Int,@Argument size: Int): List<Post> {
        return postService.getPosts(page, size)
    }

    @MutationMapping
    fun addUser(@Argument("addUserInput") userInput: AddUserInput): UUID {
        return userService.addUser(userInput)
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

data class AddUserInput(
    val name: String
)