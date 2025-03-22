package project.config

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import project.exception.EntityNotFoundException
import project.model.error.dto.ErrorResponse

@ControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<ErrorResponse> {
        log.warn("NoSuchElementException: ${ex.message}", ex)
        val error = ErrorResponse(
            message = ex.message ?: "No such element",
            errorCode = "NOT_FOUND"
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(ex: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        log.warn("EntityNotFoundException: ${ex.message}", ex)
        val error = ErrorResponse(
            message = ex.message ?: "Entity not found",
            errorCode = "NOT_FOUND"
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.warn("Validation error: ${ex.message}", ex)
        val fieldErrors = ex.bindingResult.fieldErrors.joinToString("; ") {
            "${it.field}: ${it.defaultMessage}"
        }
        val error = ErrorResponse(
            message = "Validation error: $fieldErrors",
            errorCode = "VALIDATION_ERROR"
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        log.error("Unexpected error: ${ex.message}", ex)
        val error = ErrorResponse(
            message = "Internal server error",
            errorCode = "INTERNAL_ERROR"
        )
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
