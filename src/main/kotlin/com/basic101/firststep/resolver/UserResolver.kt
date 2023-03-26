package com.basic101.firststep.resolver

import com.basic101.firststep.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.lang.RuntimeException
import java.util.*

@Controller
class UserResolver(
    private val userService: UserService,
) {

    @QueryMapping
    fun getUsers(@Argument page: Int, @Argument size: Int): List<User> {
        return userService.getUsers(page, size)
    }

    @MutationMapping
    fun addUser(@Argument("addUserInput") userInput: AddUserInput): UUID {
        return userService.addUser(userInput)
    }

    // field resolver
    @SchemaMapping(typeName = "Post")
    fun author(post: Post): User {
        LOGGER.info("Fetching author data for postId: ${post.id}")
        val postId = post.id ?: throw RuntimeException("postId cant not be null")
        return userService.findByPostId(postId)
    }

    @SchemaMapping(typeName = "Comment")
    fun author(comment: Comment): User {
        return userService.findByCommentId(comment.id)
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)
    }
}

data class User(
    val id: UUID?,
    val name: String
)

data class AddUserInput(
    val name: String
)