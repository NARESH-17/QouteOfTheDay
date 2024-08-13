package com.example.inspiremeapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: Quote)

    @Query("SELECT * FROM quotes")
    suspend fun getAllQuotes(): List<Quote>
}
