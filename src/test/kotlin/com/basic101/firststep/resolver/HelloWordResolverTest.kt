package com.basic101.firststep.resolver

import com.basic101.firststep.config.GraphqlConfig
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest
import org.springframework.context.annotation.Import
import org.springframework.graphql.test.tester.GraphQlTester
import org.springframework.test.context.ActiveProfiles

@Import(GraphqlConfig::class)
@ActiveProfiles("test")
@GraphQlTest(HelloWordResolver::class)
class HelloWordResolverTest(
    private val graphQlTester: GraphQlTester
): DescribeSpec({

    describe("Hello World Queries testcases") {

        it("should able to execute helloworld query") {

            // language=GraphQl
            val HELLO_WORLD_QUERY = """
                query {
                  helloworld
                }
            """.trimIndent()

            graphQlTester.document(
                HELLO_WORLD_QUERY
            ).execute()
                .path("helloworld")
                .entity(String::class.java)
                .isEqualTo("Hello World!")

        }

        it("should return Event When execute GetEvent query") {

            // language=GraphQL
            val GETEVENT_QUERY = """
                 query {
                   getEvent {
                      eventType
                      id                  
                   }
                 }
            """.trimIndent()

            graphQlTester.document(GETEVENT_QUERY)
                .execute()
                .path("getEvent")
                .entity(Event::class.java)
                .satisfies{
                    event ->
                    event.eventType.shouldBe("TESTING")
                    event.id.shouldNotBeNull()
                }
        }

        it("should return success data when execute greet Query") {

            // language=GraphQL
            val GREETQUERY = """
                query greet(${'$'}name: String!) {
                  greet(name: ${'$'}name)
                }
            """.trimIndent()


            graphQlTester.document(GREETQUERY)
                .variable("name", "John1")
                .execute()
                .path("data.greet")
                .entity(String::class.java)
                .satisfies {
                    it.shouldBe("Hello John1")
                }


        }

        it("should return error when greet query called with ABC name") {

            // language=GraphQL
            val GREETQUERY = """
                query greet(${'$'}name: String!) {
                  greet(name: ${'$'}name)
                }
            """.trimIndent()


            graphQlTester.document(GREETQUERY)
                .variable("name", "ABC")
                .execute()
                .errors()
                .satisfy {
                    error ->
                     error[0].message.shouldBe("name length should be in 5 to 10 chars")
                }
        }
    }
})