package com.hudson.magicapi.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import java.time.LocalDate
import jakarta.validation.constraints.Size
import java.util.UUID


@Entity
@Table(name = "USUARIO")
data class Usuario(

    @Id
    @GeneratedValue
    val id: UUID,

    @Column(nullable = false,  length = 255)
    @Size(min = 3, max = 255, message = "nome deve ter entre 3 e 255 letras")//criar um controler adviced
    @NotBlank(message = "Nome necessario!")
    val name: String,

    @Column(nullable = true, unique = true, length = 255)
    @Size(min = 1, max = 255)
    val nick: String? = null,

    @Column(nullable = false)
    @NotNull(message = "uma data e necessario")
    @PastOrPresent(message = "insira uma data valida")
    val birthDate: LocalDate = LocalDate.now(),



    // editar

    @ElementCollection
    val stack: MutableList<String> = mutableListOf()
    
)
