package com.hudson.magicapi.exception.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class ErrorResponse(
    var erro: String = "",
    var nomeParametro: String? = null,
    var descricao: String = ""
)
