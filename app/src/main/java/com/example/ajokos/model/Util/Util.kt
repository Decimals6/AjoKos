package com.example.todoapp.util

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ajokos.model.database.AppDatabase

val DB_NAME = "ajokos_database"


fun buildDb(context: Context): AppDatabase {
    val db = AppDatabase.getDatabase(context)
    return db
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE Budget ADD COLUMN budgetSpend INTEGER DEFAULT 0 not null")
    }
}

//val MIGRATION_2_3 = object : Migration(2, 3) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL(
//            "ALTER TABLE Budget ADD COLUMN is_done INTEGER NOT NULL DEFAULT 0")
//    }
//}