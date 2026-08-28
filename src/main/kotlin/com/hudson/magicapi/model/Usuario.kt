package com.hudson.magicapi.model

import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "USUARIO")
data class Usuario(

    @Id
    @GeneratedValue
    val id: UUID?,

    @Column(nullable = false, length = 255)
    val nome: String,

    @Column(nullable = false, unique = true, length = 255)
    val nick: String,

    @Column(nullable = false)
    val birthDate: LocalDate,

    @OneToMany(
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    val stack: MutableList<Stack> = mutableListOf()
)