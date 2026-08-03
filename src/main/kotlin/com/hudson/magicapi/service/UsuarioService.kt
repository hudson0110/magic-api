package com.hudson.magicapi.service

import com.hudson.magicapi.model.Usuario
import com.hudson.magicapi.repository.UsuarioRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.util.UUID


@Service
class UsuarioService(private val usuarioRepository: UsuarioRepository)
{

    fun criar( usuario: Usuario): Usuario {
        if (usuarioRepository.existsByNick(usuario.nick)){
            throw DataIntegrityViolationException("Nick já cadastrado")
        }
        return usuarioRepository.save(usuario)
    }

    fun listar(): List<Usuario> {
        return usuarioRepository.findAll()
    }

    fun buscarPorId( id: UUID): Usuario? {
        return usuarioRepository.findById(id).orElse(null)
    }

    fun editarPorId(id: UUID, usuarioAtualizado: Usuario): Usuario? {

        val usuarioExistente = usuarioRepository.findById(id).orElse(null)
            ?: return null

        val usuarioEditado = usuarioExistente.copy(
            nome = usuarioAtualizado.nome,
            nick = usuarioAtualizado.nick,
            birthDate = usuarioAtualizado.birthDate,
            stack = usuarioAtualizado.stack
        )

        return usuarioRepository.save(usuarioEditado)
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