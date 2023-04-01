package com.basic101.firststep.config

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import java.lang.RuntimeException
import java.util.regex.Pattern

class EmailScalar: Coercing<String, String> {
    override fun serialize(dataFetcherResult: Any): String {
        if (dataFetcherResult is StringValue) {
            return dataFetcherResult.value.toString()
        }

        throw RuntimeException("email is not valid")
    }

    override fun parseValue(input: Any): String {
        if (input is StringValue) {
            val possibleEmail = input.value.toString()
            if (isValidEmail(possibleEmail)) {
                return  possibleEmail
            }
        }

        throw CoercingParseValueException("failed to parse")
    }

    override fun parseLiteral(input: Any): String {
        if (input is StringValue) {
            val possibleEmail = input.value.toString()
            if (isValidEmail(possibleEmail)) {
                return  possibleEmail
            }
        }

        throw CoercingParseLiteralException("failed to parse")
    }

    private fun isValidEmail(email: String) = Pattern.matches(
       "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$", email
    )
}