package com.basic101.firststep.config

import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import java.util.regex.Pattern

@Configuration
class ScalarConfig {

    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { builder: RuntimeWiring.Builder ->
            builder.scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.DateTime)
                .scalar(
                    ExtendedScalars.newRegexScalar(
                        "PhoneNumber"
                    ).addPattern(Pattern.compile("\\([0-9]*\\)[0-9]*")).build()
                ).scalar(email())
        }
    }

    fun email(): GraphQLScalarType = GraphQLScalarType.newScalar()
        .name("Email")
        .description("String as Email with validation")
        .coercing(EmailScalar())
        .build()
}