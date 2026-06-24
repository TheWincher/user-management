package louchez.emmanuel

import louchez.emmanuel.domain.core.Result

fun <T, E> Result<T, E>.assertSuccess(): T = when (this) {
    is Result.Success -> value
    is Result.Failure -> throw AssertionError("Expected Success but got Failure: $error")
}

fun <T, E> Result<T, E>.assertFailure(): E = when (this) {
    is Result.Success -> throw AssertionError("Expected Failure but got Success: $value")
    is Result.Failure -> error
}