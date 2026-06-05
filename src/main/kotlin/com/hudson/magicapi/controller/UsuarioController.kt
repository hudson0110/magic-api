package com.hudson.magicapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    @GetMapping
    fun listar(): String {
        return "Funcionando!"
    }
}