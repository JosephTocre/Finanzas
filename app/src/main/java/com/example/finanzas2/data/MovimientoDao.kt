package com.example.finanzas2.data

import android.content.ContentValues
import android.content.Context

object MovimientoTable {
    const val TABLE = "movimientos"
    const val ID = "id"
    const val TITULO = "titulo"
    const val MONTO = "monto"
    const val ES_INGRESO = "esIngreso"
    const val FECHA = "fecha"
    const val CATEGORIA = "categoria"
}

fun Movimiento.toContentValues(): ContentValues {
    return ContentValues().apply {
        put(MovimientoTable.TITULO, titulo)
        put(MovimientoTable.MONTO, monto)
        put(MovimientoTable.ES_INGRESO, if (esIngreso) 1 else 0)
        put(MovimientoTable.FECHA, fecha.time)
        put(MovimientoTable.CATEGORIA, categoria)
    }
}

class MovimientoDao(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertarMovimiento(mov: Movimiento): Long {
        val db = dbHelper.writableDatabase
        return db.insert(MovimientoTable.TABLE, null, mov.toContentValues())
    }

    fun obtenerMovimientos(): List<Movimiento> {
        val lista = mutableListOf<Movimiento>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${MovimientoTable.TABLE} ORDER BY ${MovimientoTable.FECHA} DESC",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Movimiento(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(MovimientoTable.ID)),
                        titulo = cursor.getString(cursor.getColumnIndexOrThrow(MovimientoTable.TITULO)),
                        monto = cursor.getDouble(cursor.getColumnIndexOrThrow(MovimientoTable.MONTO)),
                        esIngreso = cursor.getInt(cursor.getColumnIndexOrThrow(MovimientoTable.ES_INGRESO)) == 1,
                        fecha = java.util.Date(cursor.getLong(cursor.getColumnIndexOrThrow(MovimientoTable.FECHA))),
                        categoria = cursor.getString(cursor.getColumnIndexOrThrow(MovimientoTable.CATEGORIA))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }

    fun actualizarMovimiento(id: Int, movimiento: Movimiento): Int {
        val db = dbHelper.writableDatabase
        return db.update(
            MovimientoTable.TABLE,
            movimiento.toContentValues(),
            "${MovimientoTable.ID}=?",
            arrayOf(id.toString())
        )
    }

    fun eliminarMovimiento(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            MovimientoTable.TABLE,
            "${MovimientoTable.ID}=?",
            arrayOf(id.toString())
        )
    }

}
