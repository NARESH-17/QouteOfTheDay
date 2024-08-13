package com.example.inspiremeapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuoteAdapter(private val quotes: List<Quote>) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    class QuoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quoteTextView: TextView = view.findViewById(R.id.quoteTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quote_item, parent, false)
        return QuoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.quoteTextView.text = quotes[position].text
    }

    override fun getItemCount() = quotes.size
}

