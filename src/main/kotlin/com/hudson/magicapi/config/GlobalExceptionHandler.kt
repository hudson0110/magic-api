package com.hudson.magicapi.config

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import com.hudson.magicapi.exception.response.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<Map<String, ErrorResponse>> {

        val erros = mutableMapOf<String, ErrorResponse>()

        ex.bindingResult.fieldErrors.forEach { erro ->
            erros[erro.field] = ErrorResponse(erro.code.toString(), erro.defaultMessage ?: "Valor inválido", "O valor " + erro.rejectedValue + " foi rejeitado pelo campo " + erro.field)

        }

        return ResponseEntity.badRequest().body(erros)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(
        ex: DataIntegrityViolationException
    ): ResponseEntity<Map<String, String>> {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            mapOf(
                "erro" to (ex.mostSpecificCause.message ?: "Erro de integridade")
            )
        )
    }



}