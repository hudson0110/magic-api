package com.hudson.magicapi.service

import com.hudson.magicapi.model.Usuario
import com.hudson.magicapi.repository.UsuarioRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.util.UUID
import com.hudson.magicapi.dto.request.UsuarioRequest
import com.hudson.magicapi.dto.response.UsuarioResponse



// faz validaçoes de negocios(regras de negocio(regras))
// segunda camada
// no controler eu valido formato/estrutura dos dados e na service se os dados estao coesos

@Service
class UsuarioService(private val usuarioRepository: UsuarioRepository)
{

    fun criar( usuarioRequest: UsuarioRequest): UsuarioResponse {

        val usuario = Usuario(
            id = null,
            nome = usuarioRequest.nome,
            nick = usuarioRequest.nick,
            birthDate = usuarioRequest.birthDate,
            stack = usuarioRequest.stack
        )

        if (usuarioRepository.existsByNick(usuario.nick)){
            throw DataIntegrityViolationException("Nick já cadastrado")
        }
        val usuarioSalvo = usuarioRepository.save(usuario)

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
        val usuario = usuarioRepository.findById(id).orElse(null)
            ?: return null

        return UsuarioResponse(
            id = usuario.id,
            nome = usuario.nome,
            nick = usuario.nick,
            birthDate = usuario.birthDate,
            stack = usuario.stack
        )
    }

    fun editarPorId(
        id: UUID,
        usuarioRequest: UsuarioRequest
    ): UsuarioResponse? {

        val usuarioExistente = usuarioRepository.findById(id).orElse(null)
            ?: return null

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
        return if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}