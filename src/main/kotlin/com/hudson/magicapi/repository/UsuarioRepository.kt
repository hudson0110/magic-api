package com.hudson.magicapi.repository

import com.hudson.magicapi.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID


interface UsuarioRepository : JpaRepository<Usuario, UUID>




/*
1contoler q prcisa de um servise e repositor e todo precisa de um model


 */