package com.hudson.magicapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hudson.magicapi.dto.request.UsuarioRequest
import com.hudson.magicapi.service.UsuarioService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.util.*
import com.hudson.magicapi.dto.response.UsuarioResponse
import com.hudson.magicapi.dto.response.StackResponse
import com.hudson.magicapi.dto.request.StackRequest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.kotlin.doThrow
import com.hudson.magicapi.exception.ResourceNotFoundException

@WebMvcTest(UsuarioController::class)
class UsuarioControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var usuarioService: UsuarioService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `201 com body ao criar valido`() {
        val id = UUID.randomUUID()
        val request = UsuarioRequest(
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now(),
            stack = listOf(StackRequest(name = "Kotlin", level = 8))
        )
        val response = UsuarioResponse(
            id = id,
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now(),
            stack = listOf(StackResponse(id = UUID.randomUUID(), name = "Kotlin", level = 8))
        )
        whenever(usuarioService.criar(any<UsuarioRequest>())).thenReturn(response)

        mockMvc.perform(
            post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.nome").value(response.nome))
    }

    @Test
    fun `400 quando algum campo estiver invalido ao criar`() {
        val requestInvalido = UsuarioRequest(
            nome = "Bi",
            nick = "",
            birthDate = LocalDate.now().plusDays(1),
            stack = listOf(StackRequest(name = "testando com mais de trinta e dois caracteres", level = 20))
        )

        mockMvc.perform(
            post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido))
        )
            .andExpect(status().isBadRequest())

            .andExpect(
                jsonPath("$.nome.nome_parametro")
                    .value("Nome deve ter entre 3 e 255 caracteres.")
            )

            .andExpect(
                jsonPath("$.nick.nome_parametro")
                    .value("Nick deve ter entre 1 e 255 caracteres.")
            )

            .andExpect(
                jsonPath("$.birthDate.nome_parametro")
                    .value("Insira uma data válida.")
            )

            .andExpect(
                jsonPath("$['stack[0].name'].nome_parametro")
                    .value("Nome da stack deve ter entre 1 e 32 caracteres.")
            )

            .andExpect(
                jsonPath("$['stack[0].level'].nome_parametro")
                    .value("Level deve ser no máximo 10.")
            )

    }

    @Test
    fun `409 quando nick ja existe`() {
        val request = UsuarioRequest(
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now(),
            stack = listOf(StackRequest(name = "Kotlin", level = 8))
        )

        whenever(usuarioService.criar(any<UsuarioRequest>())).thenThrow(
            org.springframework.dao.DataIntegrityViolationException(
                "Nick já existe"
            )
        )

        mockMvc.perform(
            post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isConflict())
    }

    @Test
    fun `200 com lista`() {
        val response = UsuarioResponse(
            id = UUID.randomUUID(),
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now(),
            stack = listOf(StackResponse(id = UUID.randomUUID(), name = "Kotlin", level = 8))
        )
        whenever(usuarioService.listar()).thenReturn(listOf(response))

        mockMvc.perform(
            get("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
    }

    @Test
    fun `200 quando existe ao buscar por id`() {

        val id = UUID.randomUUID()
        val response = UsuarioResponse(
            id = id,
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now(),
            stack = listOf(StackResponse(id = UUID.randomUUID(), name = "Kotlin", level = 8))
        )
        whenever(usuarioService.buscarPorId(id))
            .thenReturn(response)

        mockMvc.perform(get("/usuarios/$id"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Hudson"))
    }

    @Test
    fun `404 quando nao existe ao buscar por id`() {

        val id = UUID.randomUUID()

        doThrow(ResourceNotFoundException("Usuário não encontrado."))
            .whenever(usuarioService)
            .buscarPorId(id)

        mockMvc.perform(get("/usuarios/$id"))
            .andExpect(status().isNotFound())
    }

    @Test
    fun `200 quando atualiza os campos`() {
        val id = UUID.randomUUID()
        val request = UsuarioRequest(
            nome = "Editado",
            nick = "edit",
            birthDate = LocalDate.now(),
            stack = listOf(StackRequest(name = "Kotlin", level = 8))
        )
        val response = UsuarioResponse(
            id = id,
            nome = "Editado",
            nick = "edit",
            birthDate = LocalDate.now(),
            stack = listOf(StackResponse(id = UUID.randomUUID(), name = "Kotlin", level = 8))
        )

        whenever(
            usuarioService.editarPorId(eq(id), any<UsuarioRequest>())
        ).thenReturn(response)

        mockMvc.perform(
            put("/usuarios/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Editado"))

    }

    @Test
    fun `404 quando não existe ao editar`() {

        val id = UUID.randomUUID()

        val request = UsuarioRequest(
            nome = "Editado",
            nick = "edit",
            birthDate = LocalDate.now(),
            stack = listOf(
                StackRequest(
                    name = "Kotlin",
                    level = 8
                )
            )
        )

        doThrow(ResourceNotFoundException("Usuário não encontrado."))
            .whenever(usuarioService)
            .editarPorId(eq(id), any<UsuarioRequest>())

        mockMvc.perform(
            put("/usuarios/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound())
    }

    @Test
    fun `400 para validacao ao editar`() {
        val id = UUID.randomUUID()
        val requestInvalido = UsuarioRequest(
            nome = "An",
            nick = "edit",
            birthDate = LocalDate.now(),
            stack = listOf(StackRequest(name = "Kotlin", level = 8))
        )

        mockMvc.perform(
            put("/usuarios/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido))
        )
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.nome.nome_parametro")
                    .value("Nome deve ter entre 3 e 255 caracteres.")
            )
    }

    @Test
    fun `204 inativa ao deletar`() {

        val id = UUID.randomUUID()

        doNothing().whenever(usuarioService).deletarPorId(id)

        mockMvc.perform(delete("/usuarios/$id"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `404 nao existe ao deletar`() {

        val id = UUID.randomUUID()

        doThrow(ResourceNotFoundException("Usuário não encontrado."))
            .whenever(usuarioService)
            .deletarPorId(id)

        mockMvc.perform(delete("/usuarios/$id"))
            .andExpect(status().isNotFound())
    }

    @Test
    fun `400 quando nome da stack tiver mais de 32 caracteres`() {
        val requestInvalido = UsuarioRequest(
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now(),
            stack = listOf(StackRequest(name = "abcdefghijklmnopqrstuvwxyz1234567", level = 8))
        )

        mockMvc.perform(
            post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido))
        )
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$['stack[0].name'].nome_parametro")
                    .value("Nome da stack deve ter entre 1 e 32 caracteres.")
            )
    }

    @Test
    fun `200 quando existe stacks para o usuario`() {
        val id = UUID.randomUUID()
        val stack = StackResponse(id = UUID.randomUUID(), name = "Kotlin", level = 8)

        whenever(usuarioService.listarStacksPorUsuario(id))
            .thenReturn(listOf(stack))

        mockMvc.perform(get("/usuarios/$id/stacks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Kotlin"))
            .andExpect(jsonPath("$[0].level").value(8))
    }

    @Test
    fun `200 quando usuario possui mais de uma stack`() {

        val id = UUID.randomUUID()
        val stacks = listOf(
            StackResponse(id = UUID.randomUUID(), name = "Kotlin", level = 8),
            StackResponse(id = UUID.randomUUID(), name = "Java", level = 9)
        )

        whenever(usuarioService.listarStacksPorUsuario(id))
            .thenReturn(stacks)

        mockMvc.perform(get("/usuarios/$id/stacks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Kotlin"))
            .andExpect(jsonPath("$[1].name").value("Java"))
    }

    @Test
    fun `200 quando nao existem usuarios`() {
        whenever(usuarioService.listar())
            .thenReturn(emptyList())

        mockMvc.perform(get("/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `400 quando stack estiver vazia`() {

        val request = UsuarioRequest(
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now(),
            stack = emptyList()
        )

        mockMvc.perform(
            post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest())
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 11, 20])
    fun `400 quando level da stack for invalido`(level: Int) {

        val request = UsuarioRequest(
            nome = "Hudson",
            nick = "hud",
            birthDate = LocalDate.now(),
            stack = listOf(
                StackRequest(
                    name = "Kotlin",
                    level = level
                )
            )
        )

        mockMvc.perform(
            post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest())
    }

}

