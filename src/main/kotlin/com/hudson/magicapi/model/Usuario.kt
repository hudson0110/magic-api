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

/*
 * Classe que representa a entidade Usuario no sistema.
 * É mapeada para a tabela USUARIO do banco de dados através do JPA.
 * Cada atributo da classe corresponde a uma coluna da tabela.
 * O campo id é a chave primária da entidade e utiliza UUID
 * para garantir identificadores únicos.
 * Também são definidas restrições de validação para alguns campos,
 * como tamanho mínimo e máximo para nome e nick.
 * Essa classe é utilizada pelo Hibernate para persistir e recuperar
 * informações dos usuários cadastrados na aplicação.
 */