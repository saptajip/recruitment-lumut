package com.example.saptajiprasetyo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saptajiprasetyo.data.model.Post

class PostAdapter(
    private var items: List<Post>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(android.R.id.text1)

        init {
            view.setOnClickListener {
                val dataDikirim = items[adapterPosition].id // Masih kirim title (String)
                Log.d("ClickLog", "Adapter Clicked: Title '$dataDikirim' di posisi ${adapterPosition}")
                onClick(dataDikirim)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = items[position].title
    }

    fun updateList(newItems: List<Post>) {
        items = newItems
        notifyDataSetChanged()
    }
}
