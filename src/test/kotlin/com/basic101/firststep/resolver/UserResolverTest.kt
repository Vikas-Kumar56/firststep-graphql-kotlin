package com.basic101.firststep.resolver

import com.basic101.firststep.repository.UserRepository
import com.basic101.firststep.util.JwtUtil
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration
import java.util.*

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserResolverTest(
    @LocalServerPort val randomServerPort: Int,
    @Value("\${key}") val signedKey: String,
    val userRepository: UserRepository
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
    }

    describe("User resolver") {

        it("should allow to create new user") {

            // language=GraphQL
            val createUserMutation = """
                mutation {
                  addUser(addUserInput: {
                  name: "Vikas",
                  password: "pass",
                  roles: "ROLE_VIEWER"
                 })
                }
            """.trimIndent()

            val users = userRepository.findAll()

            users.size.shouldBe(0)

            val userId = graphQlTesterForUnsecureOperations.document(
                createUserMutation
            ).execute()
                .path("addUser")
                .entity(String::class.java)
                .get()

            val newUserList = userRepository.findAll()

            newUserList.size.shouldBe(1)

            newUserList.firstOrNull {
                it.id == UUID.fromString(userId)
            }.shouldNotBeNull()
                .name.shouldBe("Vikas")

        }

        it("should allow user to login with valid cred") {

            // language=GraphQL
            val createUserMutation = """
                mutation {
                  addUser(addUserInput: {
                  name: "Vikas",
                  password: "pass",
                  roles: "ROLE_VIEWER"
                 })
                }
            """.trimIndent()

            graphQlTesterForUnsecureOperations.document(
                createUserMutation
            ).executeAndVerify()

            // language=GraphQL
            val loginMutation = """
                mutation login(${'$'}username: String!, ${'$'}password: String!) {
                  login(username: ${'$'}username, password: ${'$'}password)
                }
            """.trimIndent()

            val token = graphQlTesterForUnsecureOperations.document(loginMutation)
                .variable("username", "Vikas")
                .variable("password", "pass")
                .execute()
                .path("login")
                .entity(String::class.java)
                .get()

            token.shouldNotBeNull()
        }

        it("should allow to perform getUser query") {
            // language=GraphQL
            val createUserMutation = """
                mutation {
                  addUser(addUserInput: {
                  name: "Vikas",
                  password: "pass",
                  roles: "ROLE_VIEWER"
                 })
                }
            """.trimIndent()

            graphQlTesterForUnsecureOperations.document(
                createUserMutation
            ).executeAndVerify()


            val users = userRepository.findAll()
            users.shouldHaveSize(1)


            // language=GraphQL
            val getUserQuery = """
                query getUsers(${'$'}page: Int!, ${'$'}size: Int!) {
                  getUsers(page: ${'$'}page, size: ${'$'}size) {
                    name
                  }
                }
            """.trimIndent()

            data class UserTest(val name: String)
           val getUsersResponse = graphQlTesterForSecureOperations
                .document(getUserQuery)
                .variable("page", 0)
                .variable("size", 1)
                .execute()
                .path("getUsers")
                .entityList(UserTest::class.java)
                .get()

            getUsersResponse.size.shouldBe(1)
            getUsersResponse[0].name.shouldBe("Vikas")
        }

    }
})