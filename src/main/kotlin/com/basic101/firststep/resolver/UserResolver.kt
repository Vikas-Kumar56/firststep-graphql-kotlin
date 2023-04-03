package com.basic101.firststep.resolver

import com.basic101.firststep.service.UserService
import com.basic101.firststep.util.JwtUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import java.lang.RuntimeException
import java.util.*

@Controller
class UserResolver(
    private val userService: UserService,
) {

    @PreAuthorize("hasAnyRole('VIEWER', 'ADMIN')")
    @QueryMapping
    fun getUsers(@Argument page: Int, @Argument size: Int): List<User> {
        return userService.getUsers(page, size)
    }

    @MutationMapping
    fun addUser(@Argument("addUserInput") userInput: AddUserInput): UUID {
        return userService.addUser(userInput)
    }

    // field resolver
    @PreAuthorize("hasAnyRole('VIEWER', 'ADMIN')")
    @SchemaMapping(typeName = "Post")
    fun author(post: Post): User {
        LOGGER.info("Fetching author data for postId: ${post.id}")
        val postId = post.id ?: throw RuntimeException("postId cant not be null")
        return userService.findByPostId(postId)
    }

    @PreAuthorize("hasAnyRole('VIEWER', 'ADMIN')")
    @SchemaMapping(typeName = "Comment")
    fun author(comment: Comment): User {
        return userService.findByCommentId(comment.id)
    }

    @MutationMapping
    fun login(@Argument username: String, @Argument password: String): String {
        return userService.login(username, password)
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
    val name: String,
    val password: String,
    val roles: String
)