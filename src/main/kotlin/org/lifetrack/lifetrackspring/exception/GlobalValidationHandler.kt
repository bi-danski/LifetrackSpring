package org.lifetrack.lifetrackspring.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult.allErrors
            .mapNotNull { error ->
                if (error is FieldError) {
                    error.field to (error.defaultMessage ?: "Invalid value")
                } else null
            }.toMap()

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(mapOf(
                "status" to HttpStatus.BAD_REQUEST.value(),
                "details" to "Validation failed",
//                "errors" to errors
                )
            )
    }
}

