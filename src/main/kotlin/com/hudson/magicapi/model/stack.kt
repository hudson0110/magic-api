package com.hudson.magicapi.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "STACK")
data class Stack(

    @Id
    @GeneratedValue
    val id: UUID?,

    @Column(nullable = false, length = 32)
    val name: String,

    @Column(name = "STACK_LEVEL", nullable = false)
    val level: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    var usuario: Usuario
)