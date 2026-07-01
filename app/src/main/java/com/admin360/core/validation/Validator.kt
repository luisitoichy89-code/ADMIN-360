package com.admin360.core.validation

object Validator {

    fun required(value: String): Boolean {
        return value.isNotBlank()
    }

    fun email(value: String): Boolean {
        return value.contains("@") && value.contains(".")
    }

    fun phone(value: String): Boolean {
        return value.length >= 8
    }
}
