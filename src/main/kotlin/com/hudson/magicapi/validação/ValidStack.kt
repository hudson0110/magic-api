package com.hudson.magicapi.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [StackValidator::class])
annotation class ValidStack(
    val message: String = "A Stack não pode ser nula, vazia ou conter elementos nulosio, e deve ter menos de 32 caracteres.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)