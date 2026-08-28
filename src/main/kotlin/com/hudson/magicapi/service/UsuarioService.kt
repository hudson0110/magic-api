package com.hudson.magicapi.service

import com.hudson.magicapi.dto.request.UsuarioRequest
import com.hudson.magicapi.dto.response.StackResponse
import com.hudson.magicapi.dto.response.UsuarioResponse
import com.hudson.magicapi.model.Stack
import com.hudson.magicapi.model.Usuario
import com.hudson.magicapi.repository.UsuarioRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository
) {

    private val logger = LoggerFactory.getLogger(UsuarioService::class.java)

    fun criar(usuarioRequest: UsuarioRequest): UsuarioResponse {


        val usuario = Usuario(
            id = null,
            nome = usuarioRequest.nome,
            nick = usuarioRequest.nick,
            birthDate = usuarioRequest.birthDate
        )

        val stacks = usuarioRequest.stack.map {
            Stack(
                id = null,
                name = it.name,
                level = it.level,
                usuario = usuario,
            )
        }.toMutableList()

        usuario.stack.addAll(stacks)

        if (usuarioRepository.existsByNick(usuario.nick)) {
            logger.warn(
                "Tentativa de criar usuário com nick já existente. Nick={}",
                usuario.nick
            )
            throw DataIntegrityViolationException("Nick já cadastrado")
        }

        val usuarioSalvo = usuarioRepository.save(usuario)

        logger.info("Usuário criado com sucesso. Id={}", usuarioSalvo.id)

        return usuarioSalvo.toResponse()
    }

    fun listar(): List<UsuarioResponse> {

        logger.debug("Buscando usuários no banco de dados")

        return usuarioRepository.findAll().map { usuario ->
            usuario.toResponse()
        }
    }

    fun buscarPorId(id: UUID): UsuarioResponse? {

        val usuario = usuarioRepository.findById(id).orElse(null)

        if (usuario == null) {
            logger.warn("Usuário não encontrado. Id={}", id)
            return null
        }

        return usuario.toResponse()
    }

    fun editarPorId(
        id: UUID,
        usuarioRequest: UsuarioRequest
    ): UsuarioResponse? {

        val usuarioExistente = usuarioRepository.findById(id).orElse(null)
            ?: run {
                logger.warn("Usuário não encontrado para edição. Id={}", id)
                return null
            }

        val usuarioEditado = usuarioExistente.copy(
            nome = usuarioRequest.nome,
            nick = usuarioRequest.nick,
            birthDate = usuarioRequest.birthDate
        )

        val stacks = usuarioRequest.stack.map {
            Stack(
                id = null,
                name = it.name,
                level = it.level,
                usuario = usuarioEditado
            )
        }.toMutableList()

        usuarioEditado.stack.clear()

        usuarioEditado.stack.addAll(stacks)

        val usuarioSalvo = usuarioRepository.save(usuarioEditado)

        logger.info("Usuário editado com sucesso. Id={}", id)

        return usuarioSalvo.toResponse()
    }

    fun deletarPorId(id: UUID): Boolean {

        if (!usuarioRepository.existsById(id)) {
            logger.warn("Tentativa de remover usuário inexistente. Id={}", id)
            return false
        }

        usuarioRepository.deleteById(id)

        logger.info("Usuário removido com sucesso. Id={}", id)

        return true
    }

    fun listarStacksPorUsuario(id: UUID): List<StackResponse>? {

        val usuario = usuarioRepository.findById(id).orElse(null)
            ?: return null

        return usuario.stack.map {
            StackResponse(
                id = it.id,
                name = it.name,
                level = it.level
            )
        }
    }

    private fun Usuario.toResponse(): UsuarioResponse {
        return UsuarioResponse(
            id = id,
            nome = nome,
            nick = nick,
            birthDate = birthDate,
            stack = stack.map {
                StackResponse(
                    id = it.id,
                    name = it.name,
                    level = it.level
                )
            }
        )
    }
}