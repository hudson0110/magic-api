package com.hudson.magicapi.controller

import com.hudson.magicapi.service.UsuarioService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID
import jakarta.validation.Valid
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import com.hudson.magicapi.dto.request.UsuarioRequest
import com.hudson.magicapi.dto.response.UsuarioResponse
import com.hudson.magicapi.dto.response.StackResponse

@RestController
@RequestMapping("/usuarios")
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    private val logger = LoggerFactory.getLogger(UsuarioController::class.java)

    @GetMapping
    fun listar(): List<UsuarioResponse> {
        logger.info("Recebida requisição para listar usuários")
        return usuarioService.listar()
    }

    @PostMapping
    fun criar(@RequestBody @Valid usuario: UsuarioRequest): ResponseEntity<UsuarioResponse> {
        logger.info("Recebida requisição para criar usuário. Nick={}", usuario.nick)

        val novoUsuario = usuarioService.criar(usuario)

        val location =
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoUsuario.id)
                .toUri()

        return ResponseEntity.created(location).body(novoUsuario)
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: UUID): ResponseEntity<UsuarioResponse> {
        logger.info("Recebida requisição para buscar usuário. Id={}", id)

        val usuario = usuarioService.buscarPorId(id)

        return ResponseEntity.ok(usuario)
    }


    @GetMapping("/{id}/stacks")
    fun listarStacksPorUsuario(
        @PathVariable id: UUID
    ): ResponseEntity<List<StackResponse>> {

        logger.info("Recebida requisição para listar stacks do usuário. Id={}", id)

        val stacks = usuarioService.listarStacksPorUsuario(id)

        return ResponseEntity.ok(stacks)
    }

    @PutMapping("/{id}")
    fun editarPorId(
        @PathVariable id: UUID,
        @Valid @RequestBody usuario: UsuarioRequest
    ): ResponseEntity<UsuarioResponse> {

        logger.info("Recebida requisição para editar usuário. Id={}", id)

        val usuarioAtualizado = usuarioService.editarPorId(id, usuario)

        return ResponseEntity.ok(usuarioAtualizado)
    }

    @DeleteMapping("/{id}")
    fun deletarPorId(@PathVariable id: UUID): ResponseEntity<Void> {
        logger.info("Recebida requisição para deletar usuário. Id={}", id)

        usuarioService.deletarPorId(id)

        return ResponseEntity.noContent().build()
    }
}