package com.hudson.magicapi.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.util.UUID
import com.hudson.magicapi.validation.ValidStack



@Entity
@Table(name = "USUARIO")
data class Usuario(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false, length = 255)
    @field:NotBlank(message = "Nome necessário!")
    @field:Size(
        min = 3,
        max = 255,
        message = "Nome deve ter entre 3 e 255 caracteres."
    )
    val nome: String,

    @Column(nullable = true, unique = true, length = 255)
    @field:Size(
        min = 1,
        max = 255,
        message = "Nick deve ter entre 1 e 255 caracteres."
    )
    val nick: String = "",

    @Column(nullable = false)
    @field:NotNull(message = "Uma data é necessária.")
    @field:Past(message = "Insira uma data válida.")
    val birthDate: LocalDate,

    @ElementCollection
    @field:ValidStack
    val stack: MutableList<String> = mutableListOf()
)