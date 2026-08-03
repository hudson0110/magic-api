package com.hudson.magicapi.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class StackValidator : ConstraintValidator<ValidStack, List<String>> {

    override fun isValid(
        value: List<String>?,
        context: ConstraintValidatorContext
    ): Boolean {

        if (value == null) return false

        return value.all {
            it.isNotBlank() && it.length <= 32
        }
    }
}