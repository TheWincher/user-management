package louchez.emmanuel.domain.error

interface AppError {
    val code: String
    val classification: ErrorClassification
    val message: String
}

enum class ErrorClassification {
    NOT_FOUND,
    VALIDATION_ERROR,
    UNAUTHORIZED,
    CONFLICT,
    FORBIDDEN
}