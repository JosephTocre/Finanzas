package com.example.finanzas2.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE movimientos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                monto REAL NOT NULL,
                esIngreso INTEGER NOT NULL,
                fecha INTEGER NOT NULL,
                categoria TEXT NOT NULL
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS movimientos")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "finanzas.db"
        private const val DATABASE_VERSION = 1
    }
}
