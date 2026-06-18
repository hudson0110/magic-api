package com.hudson.magicapi.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import java.time.LocalDate
import jakarta.validation.constraints.Size
import org.aspectj.lang.annotation.Before
import java.util.UUID


@Entity
@Table(name = "USUARIO")
data class Usuario(

    @Id
    @GeneratedValue
    val id: UUID? = null,

    @Column(nullable = false,  length = 255)
    @Size(min = 3, max = 255, message = "nome deve ter entre 3 e 255 letras")//criar um controler adviced
    @NotBlank(message = "Nome necessario!")
    val nome: String,

    @Column(nullable = true, unique = true, length = 255)
    @Size(min = 1, max = 255)
    val nick: String = "",

    @Column(nullable = false)
    @NotNull(message = "uma data e necessario")
    @Past(message = "insira uma data valida") //valida q e no passado q a pessoa n nasceu no futuro
    val birthDate: LocalDate = LocalDate.now(),

    @ElementCollection
    val stack: MutableList<String> = mutableListOf()
    
)
