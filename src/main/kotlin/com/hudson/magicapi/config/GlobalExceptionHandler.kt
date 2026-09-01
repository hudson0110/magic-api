package com.hudson.magicapi.config

import com.hudson.magicapi.exception.ResourceNotFoundException
import com.hudson.magicapi.exception.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(
        ex: ResourceNotFoundException
    ): ResponseEntity<ErrorResponse> {

        logger.warn("Recurso não encontrado. Mensagem={}", ex.message)

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    erro = "NOT_FOUND",
                    nomeParametro = "id",
                    descricao = ex.message ?: "Recurso não encontrado."
                )
            )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable():
            ResponseEntity<ErrorResponse> {

        logger.warn("Payload inválido recebido.")

        return ResponseEntity.badRequest().body(
            ErrorResponse(
                erro = "INVALID_REQUEST",
                descricao = "Payload inválido ou campos obrigatórios ausentes."
            )
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(
        ex: MethodArgumentTypeMismatchException
    ): ResponseEntity<ErrorResponse> {

        logger.warn(
            "Parâmetro inválido recebido. Parametro={} Valor={}",
            ex.name,
            ex.value
        )

        return ResponseEntity.badRequest().body(
            ErrorResponse(
                erro = "INVALID_PARAMETER",
                nomeParametro = ex.name,
                descricao = "O parâmetro ${ex.name} deve ser um UUID válido."
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<Map<String, ErrorResponse>> {

        val erros = mutableMapOf<String, ErrorResponse>()

        ex.bindingResult.fieldErrors.forEach { erro ->
            erros[erro.field] = ErrorResponse(
                erro.code.toString(),
                erro.defaultMessage ?: "Valor inválido",
                "O valor ${erro.rejectedValue} foi rejeitado pelo campo ${erro.field}"
            )
        }

        logger.warn(
            "Erro de validação encontrado. QuantidadeErros={}",
            erros.size
        )

        return ResponseEntity.badRequest().body(erros)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(): ResponseEntity<ErrorResponse> {

        logger.warn(
            "Violação de integridade detectada. Nick já cadastrado."
        )

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    erro = "CONFLICT",
                    descricao = "Nick já cadastrado"
                )
            )
    }
}