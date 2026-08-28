package com.hudson.magicapi.validation

import com.hudson.magicapi.dto.request.StackRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class StackValidator : ConstraintValidator<ValidStack, List<StackRequest>> {

    override fun isValid(
        value: List<StackRequest>?,
        context: ConstraintValidatorContext
    ): Boolean {

        if (value == null) return false

        return value.isNotEmpty()
    }
}