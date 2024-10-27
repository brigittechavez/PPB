package com.example.equipotres.ui.retos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.equipotres.R
import com.example.equipotres.models.Reto

class RetoAdapter(
    private val retos: MutableList<Reto>,
    private val onEditClick: (Reto) -> Unit,
    private val onDeleteClick: (Reto) -> Unit
) : RecyclerView.Adapter<RetoAdapter.RetoViewHolder>() {

    inner class RetoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val description: TextView = itemView.findViewById(R.id.reto_description)
        val editButton: ImageButton = itemView.findViewById(R.id.edit_button)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reto, parent, false)
        return RetoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        val reto = retos[position]
        holder.icon.setImageResource(reto.iconResId)
        holder.description.text = reto.description

        holder.editButton.setOnClickListener {
            onEditClick(reto)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(reto)
        }
    }

    override fun getItemCount(): Int = retos.size

    fun setRetos(newRetos: List<Reto>) {
        retos.clear()
        retos.addAll(newRetos)
        notifyDataSetChanged()
    }

    fun addReto(reto: Reto) {
        retos.add(0, reto)
        notifyItemInserted(0)
    }

    fun updateReto(reto: Reto) {
        val index = retos.indexOfFirst { it.id == reto.id }
        if (index != -1) {
            retos[index] = reto
            notifyItemChanged(index)
        }
    }
}