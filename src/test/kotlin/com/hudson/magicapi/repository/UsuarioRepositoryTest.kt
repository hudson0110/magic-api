package com.hudson.magicapi.repository

import com.hudson.magicapi.model.Usuario
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDate

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    lateinit var usuarioRepository: UsuarioRepository


    @Test
    fun `deve retornar true quando nick existir`() {

        val usuario = Usuario(id = null, nome = "Hudson", nick = "hud", birthDate = LocalDate.now())

        usuarioRepository.save(usuario)

        val resultado = usuarioRepository.existsByNick("hud")

        assertTrue(resultado)
    }

    @Test
    fun `deve retornar false quando nick nao existir`() {

        val resultado = usuarioRepository.existsByNick("hud")

        assertFalse(resultado)
    }

}