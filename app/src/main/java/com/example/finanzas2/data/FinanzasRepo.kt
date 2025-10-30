package com.example.finanzas2.data

object FinanzasRepo {
    private val movimientos = mutableListOf<Movimiento>()

    fun agregarMovimiento(movimiento: Movimiento) {
        movimientos.add(movimiento)
    }

    fun obtenerMovimientos(): List<Movimiento> = movimientos

    fun calcularSaldo(): Double =
        movimientos.fold(0.0) { acc, mov ->
            acc + if (mov.esIngreso) mov.monto else -mov.monto
        }

    fun calcularTotales(): Pair<Double, Double> {
        val ingresos = movimientos.filter { it.esIngreso }.sumOf { it.monto }
        val gastos = movimientos.filter { !it.esIngreso }.sumOf { it.monto }
        return ingresos to gastos
    }
}
