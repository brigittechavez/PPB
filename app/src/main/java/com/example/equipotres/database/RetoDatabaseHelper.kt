package com.example.equipotres.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.equipotres.models.Reto

class RetoDatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "retos.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "retos"
        const val COLUMN_ID = "id"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_ICON_RES_ID = "iconResId"
        const val COLUMN_CREATED_AT = "created_at"

        @Volatile
        private var instance: RetoDatabaseHelper? = null

        fun getInstance(context: Context): RetoDatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: RetoDatabaseHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_ICON_RES_ID INTEGER,
                $COLUMN_CREATED_AT TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addReto(reto: Reto) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, reto.id)
            put(COLUMN_DESCRIPTION, reto.description)
            put(COLUMN_ICON_RES_ID, reto.iconResId)
            put(COLUMN_CREATED_AT, reto.createdAt)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllRetos(): List<Reto> {
        val retos = mutableListOf<Reto>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_CREATED_AT DESC")

        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(COLUMN_ID))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val iconResId = getInt(getColumnIndexOrThrow(COLUMN_ICON_RES_ID))
                val createdAt = getString(getColumnIndexOrThrow(COLUMN_CREATED_AT))
                retos.add(Reto(id, description, iconResId, createdAt))
            }
        }
        cursor.close()
        db.close()
        return retos
    }

    fun updateReto(reto: Reto): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DESCRIPTION, reto.description)
            put(COLUMN_ICON_RES_ID, reto.iconResId)
            put(COLUMN_CREATED_AT, reto.createdAt)
        }
        val result = db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(reto.id))
        db.close()
        return result
    }

    fun deleteReto(reto: Reto) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(reto.id))
        db.close()
    }
}