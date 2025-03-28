package com.example.calculator_for_android
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.PointF

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_X REAL,
                $COLUMN_Y REAL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun clearTable() {
        writableDatabase.execSQL("DELETE FROM $TABLE_NAME")
    }

    fun insertPoint(x: Double, y: Double) {
        writableDatabase.use { db ->
            val values = ContentValues().apply {
                put(COLUMN_X, x)
                put(COLUMN_Y, y)
            }
            db.insert(TABLE_NAME, null, values)
        }
    }

    fun getAllPoints(): List<PointF> {
        val points = mutableListOf<PointF>()
        readableDatabase.use { db ->
            val cursor = db.rawQuery("SELECT $COLUMN_X, $COLUMN_Y FROM $TABLE_NAME", null)
            cursor.use {
                while (it.moveToNext()) {
                    val x = it.getDouble(0).toFloat()
                    val y = it.getDouble(1).toFloat()
                    points.add(PointF(x, y))
                }
            }
        }
        return points
    }

    companion object {
        private const val DATABASE_NAME = "graph.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "sinus_points"
        private const val COLUMN_ID = "id"
        private const val COLUMN_X = "x"
        private const val COLUMN_Y = "y"
    }
}