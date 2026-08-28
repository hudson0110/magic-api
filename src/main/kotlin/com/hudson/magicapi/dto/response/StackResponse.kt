package com.hudson.magicapi.dto.response

import java.util.UUID

data class StackResponse(
    val id: UUID?,
    val name: String,
    val level: Int
)