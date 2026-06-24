package louchez.emmanuel.interfaces.graphql

import louchez.emmanuel.domain.error.AppError

class AppException(val error: AppError) : Exception(error.message)