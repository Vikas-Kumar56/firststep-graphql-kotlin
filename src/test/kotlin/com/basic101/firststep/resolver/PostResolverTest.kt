package com.basic101.firststep.resolver

import com.basic101.firststep.model.PostEntity
import com.basic101.firststep.model.UserEntity
import com.basic101.firststep.repository.PostRepository
import com.basic101.firststep.repository.UserRepository
import com.basic101.firststep.util.JwtUtil
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration
import java.util.UUID

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostResolverTest(
    @LocalServerPort val randomServerPort: Int,
    @Value("\${key}") val signedKey: String,
    val userRepository: UserRepository,
    val postRepository: PostRepository
) : DescribeSpec({

    lateinit var graphQlTesterForUnsecureOperations: HttpGraphQlTester
    lateinit var graphQlTesterForSecureOperations: HttpGraphQlTester

    beforeSpec {
        val client = WebTestClient.bindToServer()
            .baseUrl("http://localhost:$randomServerPort/graphql")
            .responseTimeout(Duration.ofMinutes(2L))
            .build()

        graphQlTesterForUnsecureOperations = HttpGraphQlTester.create(client)

        graphQlTesterForSecureOperations = graphQlTesterForUnsecureOperations
            .mutate()
            .headers {
                it.set(
                    "Authorization", JwtUtil.generateJwtToken(
                        username = "John",
                        signedSecret = signedKey,
                        roles = listOf(
                            "ROLE_VIEWER"
                        )
                    )
                )
            }.build()

        val user = userRepository.save(
            UserEntity(
                name = "Vikas",
                password = "pass",
                roles = "ROLE_VIEWER"
            )
        )

        postRepository.save(
            PostEntity(
                title = "some title",
                description = "some description",
                author = user
            )
        )

    }

    describe("Post resolver") {

        it("should allow user to fetch all posts") {

            // language=GraphQL
            val getPostsQuery = """
                query {
                  getPosts {
                    id
                    title
                    description
                    author {
                       name                                      
                    }
                  }
                }
            """.trimIndent()

            data class UserTest(val name: String)
            data class PostTest(
                val id: UUID,
                val title: String,
                val description: String,
                val author: UserTest
            )

            val posts = graphQlTesterForSecureOperations.document(getPostsQuery)
                .execute().path("getPosts")
                .entityList(PostTest::class.java)
                .get()


            posts.shouldHaveSize(1)
            posts[0].title.shouldBe("some title")
            posts[0].description.shouldBe("some description")
            posts[0].author.name.shouldBe("Vikas")
        }

    }

})