package com.hudson.magicapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hudson.magicapi.model.Usuario
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


/*
* junit
* mokito
*
*
*
*
*
* testes necessarios
* {x} 201 com body ao criar valido
* {} 400 qunado algum campo estiver
* {}409 qunado nik ja existe
*
* {} 200 com lista
*
* {}200 qunado existe
* {}404 qunado nao existe
*
* {}200 quando atualiza os campos
* {}404 qunado n existe
* {}400 para validação
*
* {}204 inativa
* {}404 nao exite
*  */



@WebMvcTest(UsuarioController::class)
class UsuarioControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var usuarioService: UsuarioService

    @Autowired
    lateinit var objectMapper: ObjectMapper



    //should when

    @Test
    fun `201 com body ao criar valido`() {
        val id = UUID.randomUUID()
        val usuario = Usuario(id, "Hudson", "hud", LocalDate.now(), mutableListOf())
        whenever(usuarioService.criar(any<Usuario>())).thenReturn(usuario)//garantindo q to em um ambiante

        mockMvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isCreated)
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.name").value("Hudson"))
    }

    @Test
    fun `400 quando algum campo estiver invalido ao criar`() {
        // Nome curto demais (falha Size) e data futura (falha PastOrPresent)
        val usuarioInvalido = Usuario(UUID.randomUUID(), "Ab", "h", LocalDate.now().plusDays(1))

        mockMvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuarioInvalido)))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.name").value("nome deve ter entre 3 e 255 letras"))
            .andExpect(jsonPath("$.birthDate").value("insira uma data valida"))
    }


    @Test
    fun `409 quando nick ja existe`() {
        val usuario = Usuario(UUID.randomUUID(), "Hudson", "hud", LocalDate.now(), mutableListOf())
        whenever(usuarioService.criar(any<Usuario>())).thenThrow(org.springframework.dao.DataIntegrityViolationException("Nick já existe"))

        mockMvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isConflict)
    }

    @Test
    fun `200 quando existe ao buscar por id`() {
        val id = UUID.randomUUID()
        val usuario = Usuario(id, "Hudson", "hud", LocalDate.now(), mutableListOf())
        whenever(usuarioService.buscarPorId(id)).thenReturn(usuario)

        mockMvc.perform(get("/usuarios/$id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Hudson"))
    }

    @Test
    fun `404 quando nao existe ao buscar por id`() {
        val id = UUID.randomUUID()
        whenever(usuarioService.buscarPorId(id)).thenReturn(null)

        mockMvc.perform(get("/usuarios/$id"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `200 quando atualiza os campos`() {
        val id = UUID.randomUUID()
        val usuario = Usuario(id, "Editado", "edit", LocalDate.now(), mutableListOf())
        whenever(usuarioService.editarPorId(eq(id), any<Usuario>())).thenReturn(usuario)

        mockMvc.perform(put("/usuarios/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Editado"))
    }

    @Test
    fun `404 quando n existe ao editar`() {
        val id = UUID.randomUUID()
        val usuario = Usuario(id, "Editado", "edit", LocalDate.now(), mutableListOf())
        whenever(usuarioService.editarPorId(eq(id), any<Usuario>())).thenReturn(null)

        mockMvc.perform(put("/usuarios/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuario)))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `400 para validacao ao editar`() {
        val id = UUID.randomUUID()
        val usuarioInvalido = Usuario(id, "Ab", "edit", LocalDate.now())

        mockMvc.perform(put("/usuarios/$id")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(usuarioInvalido)))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.name").value("nome deve ter entre 3 e 255 letras"))
    }

    @Test
    fun `204 inativa ao deletar`() {
        val id = UUID.randomUUID()
        whenever(usuarioService.deletarPorId(id)).thenReturn(true)

        mockMvc.perform(delete("/usuarios/$id"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `404 nao existe ao deletar`() {
        val id = UUID.randomUUID()
        whenever(usuarioService.deletarPorId(id)).thenReturn(false)

        mockMvc.perform(delete("/usuarios/$id"))
            .andExpect(status().isNotFound)
    }
}
