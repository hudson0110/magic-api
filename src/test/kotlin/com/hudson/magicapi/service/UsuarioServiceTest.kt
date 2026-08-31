package com.hudson.magicapi.service

import com.hudson.magicapi.dto.request.StackRequest
import com.hudson.magicapi.dto.request.UsuarioRequest
import com.hudson.magicapi.model.Stack
import com.hudson.magicapi.model.Usuario
import com.hudson.magicapi.repository.UsuarioRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.mock
import java.time.LocalDate
import org.springframework.dao.DataIntegrityViolationException
import java.util.UUID
import org.junit.jupiter.api.assertThrows
import java.util.Optional
import com.hudson.magicapi.exception.ResourceNotFoundException


class UsuarioServiceTest {

    private lateinit var usuarioRepository: UsuarioRepository

    private lateinit var usuarioService: UsuarioService

    @BeforeEach
    fun setup() {
        usuarioRepository = mock()
        usuarioService = UsuarioService(usuarioRepository)
    }

    @Test
    fun `deve criar usuario com sucesso`() {

        val request = UsuarioRequest(
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now(),
            stack = listOf(StackRequest(name = "Kotlin", level = 8))
        )
        val usuarioSalvo = Usuario(
            id = UUID.randomUUID(),
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now()
        )
        usuarioSalvo.stack.add(
            Stack(
                id = UUID.randomUUID(),
                name = "Kotlin",
                level = 8,
                usuario = usuarioSalvo
            )
        )
        whenever(usuarioRepository.existsByNick("hud"))
            .thenReturn(false)

        whenever(usuarioRepository.save(any()))
            .thenReturn(usuarioSalvo)

        val resultado = usuarioService.criar(request)

        assertNotNull(resultado)
        assertEquals("Hudson", resultado.nome)
        assertEquals("hud", resultado.nick)
        verify(usuarioRepository).save(any())
    }

    @Test
    fun `deve lancar excecao quando nick ja existe`() {

        val request = UsuarioRequest(
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now(),
            stack = listOf(StackRequest(name = "Kotlin", level = 8))
        )

        whenever(usuarioRepository.existsByNick("hud"))
            .thenReturn(true)

        assertThrows<DataIntegrityViolationException> {
            usuarioService.criar(request)
        }

        verify(usuarioRepository).existsByNick("hud")
    }

    @Test
    fun `deve buscar usuario por id quando existir`() {

        val id = UUID.randomUUID()
        val usuario = Usuario(id = id, nome = "Hudson", nick = "hud", birthDate = LocalDate.now())
        whenever(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario))

        val resultado = usuarioService.buscarPorId(id)

        assertNotNull(resultado)
        assertEquals(id, resultado?.id)
        assertEquals("Hudson", resultado?.nome)
        assertEquals("hud", resultado?.nick)

        verify(usuarioRepository).findById(id)
    }

    @Test
    fun `deve lancar excecao quando usuario nao existir`() {

        val id = UUID.randomUUID()

        whenever(usuarioRepository.findById(id))
            .thenReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            usuarioService.buscarPorId(id)
        }

        verify(usuarioRepository).findById(id)
    }

    @Test
    fun `deve editar usuario existente`() {

        val id = UUID.randomUUID()

        val usuarioExistente = Usuario(id = id, nome = "Hudson", nick = "hud", birthDate = LocalDate.now())
        val request = UsuarioRequest(nome = "Hudson Editado", nick = "hud.editado", birthDate = LocalDate.now(), stack = listOf(StackRequest(name = "Java", level = 9)))
        val usuarioSalvo = Usuario(id = id, nome = "Hudson Editado", nick = "hud.editado", birthDate = request.birthDate)

        usuarioSalvo.stack.add(Stack(id = UUID.randomUUID(), name = "Java", level = 9, usuario = usuarioSalvo))


        whenever(usuarioRepository.findById(id))
            .thenReturn(Optional.of(usuarioExistente))

        whenever(usuarioRepository.save(any()))
            .thenReturn(usuarioSalvo)

        val resultado = usuarioService.editarPorId(id, request)

        assertNotNull(resultado)
        assertEquals("Hudson Editado", resultado?.nome)
        assertEquals("hud.editado", resultado?.nick)

        verify(usuarioRepository).findById(id)
        verify(usuarioRepository).save(any())
    }

    @Test
    fun `deve lancar excecao ao editar usuario inexistente`() {

        val id = UUID.randomUUID()
        val request = UsuarioRequest(nome = "Hudson Editado", nick = "hud.editado", birthDate = LocalDate.now(), stack = listOf(StackRequest(name = "Java", level = 9)))

        whenever(usuarioRepository.findById(id))
            .thenReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            usuarioService.editarPorId(id, request)
        }

        verify(usuarioRepository).findById(id)
    }

    @Test
    fun `deve deletar usuario existente`() {

        val id = UUID.randomUUID()

        whenever(usuarioRepository.existsById(id))
            .thenReturn(true)

        usuarioService.deletarPorId(id)

        verify(usuarioRepository).existsById(id)
        verify(usuarioRepository).deleteById(id)
    }

    @Test
    fun `deve lancar excecao ao deletar usuario inexistente`() {

        val id = UUID.randomUUID()

        whenever(usuarioRepository.existsById(id))
            .thenReturn(false)

        assertThrows<ResourceNotFoundException> {
            usuarioService.deletarPorId(id)
        }
    }

    @Test
    fun `deve listar stacks do usuario quando existir`() {

        val id = UUID.randomUUID()
        val usuario = Usuario(id = id, nome = "Hudson", nick = "hud", birthDate = LocalDate.now())
        usuario.stack.add(Stack(id = UUID.randomUUID(), name = "Kotlin", level = 8, usuario = usuario))

        whenever(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario))

        val resultado = usuarioService.listarStacksPorUsuario(id)

        assertNotNull(resultado)
        assertEquals(1, resultado?.size)
        assertEquals("Kotlin", resultado?.first()?.name)
        assertEquals(8, resultado?.first()?.level)

        verify(usuarioRepository).findById(id)
    }

    @Test
    fun `deve lancar excecao ao listar stacks de usuario inexistente`() {

        val id = UUID.randomUUID()

        whenever(usuarioRepository.findById(id))
            .thenReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            usuarioService.listarStacksPorUsuario(id)
        }

        verify(usuarioRepository).findById(id)
    }

    @Test
    fun `deve retornar lista vazia quando nao existirem usuarios`() {

        whenever(usuarioRepository.findAll()).thenReturn(emptyList())

        val resultado = usuarioService.listar()

        assertTrue(resultado.isEmpty())

        verify(usuarioRepository).findAll()
    }

}

