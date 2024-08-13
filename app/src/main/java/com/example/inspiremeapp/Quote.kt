package com.example.inspiremeapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey val id: Int = 0,
    val text: String,
)
