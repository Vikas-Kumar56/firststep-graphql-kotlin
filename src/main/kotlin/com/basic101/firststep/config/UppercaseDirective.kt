package com.basic101.firststep.config

import graphql.schema.DataFetcherFactories
import graphql.schema.FieldCoordinates
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment
import java.util.Objects
import java.util.function.BiFunction

class UppercaseDirective: SchemaDirectiveWiring {
    override fun onField(
        environment: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>?
    ): GraphQLFieldDefinition? {

        val fieldDefinition = environment?.fieldDefinition
        val container = environment?.fieldsContainer
        val dataFetcher = environment?.codeRegistry?.getDataFetcher(container, fieldDefinition)

        val wrapDataFetcher = DataFetcherFactories.wrapDataFetcher(dataFetcher) { env, value ->
                if (Objects.nonNull(value)) {
                    return@wrapDataFetcher (value as String).uppercase()
                }
                value
        }

        val fieldCoordinates = FieldCoordinates.coordinates(container, fieldDefinition)
        environment?.codeRegistry?.dataFetcher(fieldCoordinates, wrapDataFetcher)

        return fieldDefinition
    }
}