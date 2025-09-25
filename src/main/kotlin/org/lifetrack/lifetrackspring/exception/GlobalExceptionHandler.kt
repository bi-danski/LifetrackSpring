
import org.lifetrack.lifetrackspring.exception.ResourceNotFound
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound::class)
    fun handleNotFound(ex: ResourceNotFound): ResponseEntity <Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(mapOf("error" to ex.message.toString()))
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("error" to ex.localizedMessage,
                "details" to "Server can't do that! Try again next time",
//                "metro" to ex.stackTraceToString()
                )
            )
    }


}
