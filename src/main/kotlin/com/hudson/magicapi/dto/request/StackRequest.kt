package com.hudson.magicapi.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class StackRequest(

    @field:NotBlank(message = "Nome da stack é obrigatório.")
    @field:Size(
        min = 1,
        max = 32,
        message = "Nome da stack deve ter entre 1 e 32 caracteres."
    )
    val name: String,

    @field:Min(value = 1, message = "Level deve ser no mínimo 1.")
    @field:Max(value = 10, message = "Level deve ser no máximo 10.")
    val level: Int
)