package com.basic101.firststep.config

import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import graphql.schema.idl.RuntimeWiring
import graphql.validation.rules.OnValidationErrorStrategy
import graphql.validation.rules.ValidationRules
import graphql.validation.schemawiring.ValidationSchemaWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import java.util.regex.Pattern

@Configuration
class GraphqlConfig {

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
                .directiveWiring(validationSchemaWiring())
                .directiveWiring(UppercaseDirective())
        }
    }

    fun validationSchemaWiring(): ValidationSchemaWiring {
        val validationRule = ValidationRules.newValidationRules()
            .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
            .build()

        return ValidationSchemaWiring(validationRule)
    }

    fun email(): GraphQLScalarType = GraphQLScalarType.newScalar()
        .name("Email")
        .description("String as Email with validation")
        .coercing(EmailScalar())
        .build()
}