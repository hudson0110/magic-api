package com.hudson.magicapi.model

import jakarta.persistence.*
import java.time.LocalDate
import jakarta.validation.constraints.Size
import java.util.UUID


@Entity
@Table(name = "USUARIO")
data class Usuario(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false,  length = 255)
    @Size(min = 3, max = 255)
    val nome: String = "",

    @Column(nullable = true, unique = true, length = 255)
    @Size(min = 1, max = 255)
    val nick: String = "",

    @Column(nullable = false)
    val birthDate: LocalDate = LocalDate.now(),

    @ElementCollection
    val stack: MutableList<String> = mutableListOf()
    
)

