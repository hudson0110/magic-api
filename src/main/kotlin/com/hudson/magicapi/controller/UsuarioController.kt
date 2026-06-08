package com.hudson.magicapi.controller

import com.hudson.magicapi.model.Usuario
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.hudson.magicapi.repository.UsuarioRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/usuarios")
class UsuarioController (
    private val usuarioRepository: UsuarioRepository
) {

    @GetMapping
    fun listar(): List<Usuario> {
        return usuarioRepository.findAll()
    }

    @PostMapping
    fun criar(@RequestBody usuario: Usuario): Usuario {
        return usuarioRepository.save(usuario)
    }
}

/*
 * Controller responsável por expor os endpoints da API relacionados
 * aos usuários. Recebe requisições HTTP enviadas pelos clientes,
 * processa as solicitações e retorna as respostas adequadas.
 * Utiliza o UsuarioRepository para acessar os dados persistidos
 * no banco de dados. Através das anotações do Spring, é possível
 * definir rotas e métodos HTTP como GET, POST, PUT e DELETE.
 * Essa camada representa o ponto de entrada da aplicação para
 * operações realizadas pelos consumidores da API.
 */
