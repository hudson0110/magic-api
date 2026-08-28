package com.hudson.magicapi.dto.response

import java.time.LocalDate
import java.util.UUID

data class UsuarioResponse(
    val id: UUID?,
    val nome: String,
    val nick: String,
    val birthDate: LocalDate,
    val stack: List<StackResponse>
)