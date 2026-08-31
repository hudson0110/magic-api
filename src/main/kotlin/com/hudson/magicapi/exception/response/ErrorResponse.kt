package com.hudson.magicapi.exception.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.PropertyNamingStrategies


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ErrorResponse(
    var erro: String = "",
    var nomeParametro: String? = null,
    var descricao: String = ""
)
