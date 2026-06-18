package com.hudson.magicapi.repository

import com.hudson.magicapi.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID


interface UsuarioRepository : JpaRepository<Usuario, UUID>{

    fun existsByNick(nick: String): Boolean

}




/*
 * Interface responsável pelo acesso aos dados da entidade Usuario.
 * Herda de JpaRepository, disponibilizando automaticamente operações
 * básicas como salvar, atualizar, remover e buscar registros.
 * O Spring Data JPA implementa essa interface em tempo de execução,
 * eliminando a necessidade de escrever consultas SQL simples.
 * Atua como camada intermediária entre a aplicação e o banco de dados,
 * facilitando a manutenção e organização do código.
 */