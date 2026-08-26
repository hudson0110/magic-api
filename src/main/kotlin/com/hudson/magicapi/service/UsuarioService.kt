package com.hudson.magicapi.service

import com.hudson.magicapi.model.Usuario
import com.hudson.magicapi.repository.UsuarioRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.util.UUID
import com.hudson.magicapi.dto.request.UsuarioRequest
import com.hudson.magicapi.dto.response.UsuarioResponse
import org.slf4j.LoggerFactory


@Service
class UsuarioService(private val usuarioRepository: UsuarioRepository)
{

    private val logger = LoggerFactory.getLogger(UsuarioService::class.java)

    fun criar( usuarioRequest: UsuarioRequest): UsuarioResponse {

        logger.info("Recebida solicitação de criação de usuário. Nick={}", usuarioRequest.nick)

        val usuario = Usuario(
            id = null,
            nome = usuarioRequest.nome,
            nick = usuarioRequest.nick,
            birthDate = usuarioRequest.birthDate,
            stack = usuarioRequest.stack
        )

        if (usuarioRepository.existsByNick(usuario.nick)){
            logger.warn("Tentativa de criar usuário com nick já existente. Nick={}", usuario.nick)
            throw DataIntegrityViolationException("Nick já cadastrado")
        }
        val usuarioSalvo = usuarioRepository.save(usuario)
        logger.info("Usuário criado com sucesso. Id={}", usuarioSalvo.id)


        return UsuarioResponse(
            id = usuarioSalvo.id,
            nome = usuarioSalvo.nome,
            nick = usuarioSalvo.nick,
            birthDate = usuarioSalvo.birthDate,
            stack = usuarioSalvo.stack
        )
    }

    fun listar(): List<UsuarioResponse> {
        return usuarioRepository.findAll().map { usuario ->
            logger.debug("Listando usuários")
            UsuarioResponse(
                id = usuario.id,
                nome = usuario.nome,
                nick = usuario.nick,
                birthDate = usuario.birthDate,
                stack = usuario.stack
            )
        }
    }


    fun buscarPorId(id: UUID): UsuarioResponse? {
        logger.info("Buscando usuário por id={}", id)

        val usuario = usuarioRepository.findById(id).orElse(null)
            ?: return null

        logger.info("Usuário encontrado. id={}", id)

        return UsuarioResponse(
            id = usuario.id,
            nome = usuario.nome,
            nick = usuario.nick,
            birthDate = usuario.birthDate,
            stack = usuario.stack
        )
    }

    fun editarPorId(id: UUID, usuarioRequest: UsuarioRequest): UsuarioResponse? {

        logger.info("Iniciando edição do usuário. id={}", id)

        val usuarioExistente = usuarioRepository.findById(id).orElse(null)


            ?:run {
                logger.warn("Usuário não encontrado para edição. id={}", id)
                return null
            }

        logger.info("Usuário editado com sucesso. id={}", id)

        val usuarioEditado = usuarioExistente.copy(
            nome = usuarioRequest.nome,
            nick = usuarioRequest.nick,
            birthDate = usuarioRequest.birthDate,
            stack = usuarioRequest.stack
        )

        val usuarioSalvo = usuarioRepository.save(usuarioEditado)

        return UsuarioResponse(
            id = usuarioSalvo.id,
            nome = usuarioSalvo.nome,
            nick = usuarioSalvo.nick,
            birthDate = usuarioSalvo.birthDate,
            stack = usuarioSalvo.stack
        )
    }

    fun deletarPorId(id: UUID): Boolean {

        logger.info("Solicitação de exclusão do usuário. id={}", id)

        return if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id)
            logger.info("Usuário removido com sucesso. id={}", id)
            true
        } else {
            logger.warn("Tentativa de remover usuário inexistente. id={}", id)
            false
        }
    }
}