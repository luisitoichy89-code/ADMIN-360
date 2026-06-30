package com.admin360.feature.user.data

data class UserDto(
    val id: String,
    val email: String,
    val cliente_id: String,
    val negocio_id: String?,
    val rol: String,
    val android_id: String?
)
