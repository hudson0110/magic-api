package com.hudson.magicapi.model

import jakarta.persistence.*
import java.time.LocalDate

import jakarta.validation.constraints.Size
import org.hibernate.internal.util.collections.Stack


@Entity
@Table(name = "USUARIO")
data class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false,  length = 255)
    @Size(min = 3, max = 255)
    val nome: String = "",

    @Column(nullable = true, unique = true, length = 255)
    @Size(min = 1, max = 255)
    val nick: String = "",

    @Column(nullable = false)
    val birthDate: LocalDate = LocalDate.now(),


)