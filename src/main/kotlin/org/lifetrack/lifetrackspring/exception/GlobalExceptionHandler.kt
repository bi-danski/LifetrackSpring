package org.lifetrack.lifetrackspring.exception

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFound::class)
    fun handleResourceNotFound(@Suppress("UNUSED_PARAMETER") ex: ResourceNotFound): ResponseEntity<String> {
        val html = """
            <!doctype html>
            <html lang="en">
            <head>
              <meta charset="utf-8"/>
              <meta name="viewport" content="width=device-width,initial-scale=1"/>
              <title>404 - Not Found</title>
              <style>
                body { font-family: Arial, sans-serif; background:#f8fafc; color:#111827;
                       display:flex; align-items:center; justify-content:center; height:100vh; margin:0; }
                .card { background:white; border-radius:8px; padding:28px; 
                        box-shadow:0 6px 20px rgba(16,24,40,0.08); max-width:720px; width:90%; text-align:center; }
                h1 { margin:0 0 8px 0; font-size:28px; }
                p { margin:0; color:#374151; }
              </style>
            </head>
            <body>
              <div class="card">
                <h1>404 — Page Not Found</h1>
                <p>The resource you are looking for could not be found.</p>
              </div>
            </body>
            </html>
        """.trimIndent()

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.TEXT_HTML)
            .body(html)
    }

//    @ExceptionHandler(Exception::class)
//    fun handleGeneral(ex: Exception): ResponseEntity<Map<String, Any>> {
//        val errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
//        val errorMessage = ex.message ?: "Server can't do that! Try again next time"
//
//
//        return ResponseEntity
//            .status(HttpStatus.INTERNAL_SERVER_ERROR)
//            .body(
//                mapOf(
//                    "error" to  "Server can't do that! Try again next time",
//                )
//            )
//    }
}
