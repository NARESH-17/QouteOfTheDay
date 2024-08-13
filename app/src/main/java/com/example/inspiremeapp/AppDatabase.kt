package com.example.inspiremeapp


import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Quote::class], version = 2)
@AutoMigration(from = 1, to = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
}
