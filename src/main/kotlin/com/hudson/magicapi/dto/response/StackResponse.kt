package com.hudson.magicapi.dto.response

import java.util.UUID
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class StackResponse(
    val id: UUID?,
    val name: String,
    val level: Int
)