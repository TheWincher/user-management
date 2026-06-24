package louchez.emmanuel.interfaces.graphql

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import louchez.emmanuel.domain.error.AppError
import louchez.emmanuel.domain.error.ErrorClassification
import java.util.concurrent.CompletableFuture

class AppGraphQLExceptionHandler : DataFetcherExceptionHandler {
    override fun handleException(
        handlerParameters: DataFetcherExceptionHandlerParameters
    ): CompletableFuture<DataFetcherExceptionHandlerResult> {
        val exception = handlerParameters.exception
        return when (exception) {
            is AppException -> GraphqlErrorBuilder.newError()
                .message(exception.message)
                .extensions(
                    mapOf(
                        "code" to exception.error.code,
                        "classification" to exception.error.classification.name
                    )
                )
                .build()

            else -> GraphqlErrorBuilder.newError()
                .message("Internal server error")
                .extensions(
                    mapOf(
                        "code" to "INTERNAL_ERROR",
                        "classification" to "INTERNAL_ERROR"
                    )
                )
                .build()
        }.let { error ->
            CompletableFuture.completedFuture(
                DataFetcherExceptionHandlerResult.newResult()
                    .error(error)
                    .build()
            )
        }
    }
}