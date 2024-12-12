package com.dicoding.wiseorganic.ui.main.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.wiseorganic.R
import okhttp3.Callback

class ListHistoryAdapter(private val listHistory: ArrayList<History>) : RecyclerView.Adapter<ListHistoryAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHistDepartement: TextView = itemView.findViewById(R.id.tvHistDepartment)
        val tvHistCreated: TextView = itemView.findViewById(R.id.tvHistCreated)
        val tvHistUpdated: TextView = itemView.findViewById(R.id.tvHistUpdated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_history, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (departmentName, created, updated) = listHistory[position]
        holder.tvHistDepartement.text = departmentName
        holder.tvHistCreated.text = created
        holder.tvHistUpdated.text = updated
        holder.itemView.setOnClickListener {
          onItemClickCallback.onItemClicked(listHistory[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: History)
    }

    override fun getItemCount(): Int = listHistory.size

}