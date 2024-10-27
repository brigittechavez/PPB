package com.example.equipotres.models

import java.util.UUID

data class Reto(
    val id: String = UUID.randomUUID().toString(),
    var  description: String,
    var  iconResId: Int,
    val createdAt: String
)
