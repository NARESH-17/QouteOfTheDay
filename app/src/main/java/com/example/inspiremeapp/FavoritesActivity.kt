package com.example.inspiremeapp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var NoQuotestextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "quotes-db"
        ).fallbackToDestructiveMigration().build()

        recyclerView = findViewById(R.id.recyclerView)
        NoQuotestextView = findViewById(R.id.NoQuotestextView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadFavoriteQuotes()
    }

    private fun loadFavoriteQuotes() {
        lifecycleScope.launch {
            val quotes = withContext(Dispatchers.IO) {
                database.quoteDao().getAllQuotes()
            }
            if(quotes.isNotEmpty())
            {
                recyclerView.adapter = QuoteAdapter(quotes)
                recyclerView.visibility = View.VISIBLE
                NoQuotestextView.visibility = View.GONE
            }else
            {
                NoQuotestextView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE

            }

        }
    }
}