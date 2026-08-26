package com.hudson.magicapi.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.hudson.magicapi.validation.ValidStack
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.LocalDate




@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class UsuarioRequest(

    @field:NotBlank(message = "Nome necessário!")
    @field:Size(
        min = 3,
        max = 255,
        message = "Nome deve ter entre 3 e 255 caracteres."
    )
    val nome: String,

    @field:Size(
        min = 1,
        max = 255,
        message = "Nick deve ter entre 1 e 255 caracteres."
    )
    val nick: String,

    @field:NotNull(message = "Uma data é necessária.")
    @field:PastOrPresent(message = "Insira uma data válida.")
    val birthDate: LocalDate,

    @field:ValidStack
    val stack: MutableList<String>
    )