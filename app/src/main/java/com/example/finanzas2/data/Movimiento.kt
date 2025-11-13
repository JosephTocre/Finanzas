package com.example.finanzas2.data

import java.util.*

data class Movimiento(
    val titulo: String,
    val monto: Double,
    val esIngreso: Boolean,
    val fecha: Date,
    val categoria: String // <-- nuevo campo
)
