package com.example.inspiremeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var quoteTextView: TextView
    private lateinit var quoteRefresh: SwipeRefreshLayout
    private lateinit var favoriteButton: ImageButton
    private lateinit var shareButton: Button
    private lateinit var viewFavoritesButton: Button

    private val quotes = mapOf<Int,String>(
       1 to "The revolution is not an apple that falls when it is ripe. You have to make it fall.",
        2 to "Happiness is real when you share",
        3 to "You miss 100% of the shots you don’t take",
        4 to "The best way to predict the future is to create it.",
        5 to "யாதும் ஊரே யாவரும் கேளிர்",
        6 to "There is no tomorrow",
        7 to "Love isn't something you find. Love is something that finds you",
        8 to "Live a life you will remember"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "quotes-db"
        ).fallbackToDestructiveMigration().build()

        quoteTextView = findViewById(R.id.quoteTextView)
        favoriteButton = findViewById(R.id.favoriteButton)
        shareButton = findViewById(R.id.shareButton)
        quoteRefresh = findViewById(R.id.swiperefresh)
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton)

        displayQuoteOfTheDay()

        shareButton.setOnClickListener {
            shareQuote(quoteTextView.text.toString())
        }

        favoriteButton.setOnClickListener {
            saveFavoriteQuote(quoteTextView.text.toString(), quoteTextView.getTag(R.string.lastquoteID) as Int)
        }

        viewFavoritesButton.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
        // Sets up a SwipeRefreshLayout.OnRefreshListener that invokes when
// the user performs a swipe-to-refresh gesture.

        quoteRefresh.setOnRefreshListener {

            // This method performs the actual data-refresh operation and calls
            // setRefreshing(false) when it finishes.
            displayQuoteOfTheDay(true)
            quoteRefresh.isRefreshing = false


        }
    }

    private fun displayQuoteOfTheDay(isRefresh :Boolean = false) {
        val sharedPreferences = getSharedPreferences("InspireMePrefs", Context.MODE_PRIVATE)
        val lastQuoteDate = sharedPreferences.getString("lastQuoteDate", "")
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (lastQuoteDate != currentDate || isRefresh) {
            val newQuote = quotes.entries.random()
                val QuoteValue= newQuote.value
            sharedPreferences.edit().putString("lastQuote", QuoteValue).apply()
            sharedPreferences.edit().putInt("lastQuoteID", newQuote.key).apply()
            sharedPreferences.edit().putString("lastQuoteDate", currentDate).apply()
            quoteTextView.text = QuoteValue
            quoteTextView.setTag(R.string.lastquoteID, newQuote.key)
        } else {
            quoteTextView.text = sharedPreferences.getString("lastQuote", "Stay inspired!")
            quoteTextView.setTag(R.string.lastquoteID, sharedPreferences.getInt("lastQuoteID", 1))

        }
    }

    private fun shareQuote(quote: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, quote)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Quote"))
    }

    private fun saveFavoriteQuote(quote: String, tag: Int) {
        lifecycleScope.launch {
            val favoriteQuote = Quote(text = quote,)
            withContext(Dispatchers.IO) {
                database.quoteDao().insert(favoriteQuote)
            }
            showToast("Quote added to the Favorites! ")
        }
    }

    private fun showToast(text:String){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show()
    }

}