package com.hudson.magicapi.controller

import com.hudson.magicapi.model.Usuario
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.hudson.magicapi.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import java.util.UUID
import jakarta.validation.Valid


@RestController
@RequestMapping("/usuarios")
class UsuarioController (
    private val usuarioService: UsuarioService
) {

    @GetMapping
    fun listar(): List<Usuario> {
        return usuarioService.listar()
    }

    @PostMapping
    fun criar(@RequestBody usuario: Usuario): Usuario {
        return usuarioService.criar(usuario)
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: UUID): Usuario? {
        return usuarioService.buscarPorId(id)
    }

    @PutMapping("/{id}")
    fun editarPorId(
        @PathVariable id: UUID,
        @Valid @RequestBody usuario: Usuario
    ): ResponseEntity<Usuario> {

        val usuarioAtualizado = usuarioService.editarPorId(id, usuario)

        return if (usuarioAtualizado != null) {
            ResponseEntity.ok(usuarioAtualizado)
        } else {
            ResponseEntity.notFound().build()
        }
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
