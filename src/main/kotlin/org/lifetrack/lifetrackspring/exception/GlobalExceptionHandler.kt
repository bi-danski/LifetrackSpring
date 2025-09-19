import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.nio.file.attribute.UserPrincipalNotFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserPrincipalNotFoundException::class)
    fun handleNotFound(ex: UserPrincipalNotFoundException): ResponseEntity<out Map<String, Any?>> {
        val body = mapOf(
            "status" to HttpStatus.NOT_FOUND.value(),
            "details" to "Resource not found",
            "errors" to ex.message
        )
        return ResponseEntity(body, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception): ResponseEntity<Map<String, Any>> {
        val body = mapOf(
            "status" to HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "details" to "Server can't do that! Try again next time",
            "errors" to ex.localizedMessage
        )
        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
