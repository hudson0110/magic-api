package com.hudson.magicapi.dto.response

import java.time.LocalDate
import java.util.UUID
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UsuarioResponse(
    val id: UUID?,
    val nome: String,
    val nick: String,
    val birthDate: LocalDate,
    val stack: List<StackResponse>
)