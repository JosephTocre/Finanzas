package com.example.finanzas2.ui.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzas2.R
import com.example.finanzas2.data.Categoria
import com.example.finanzas2.databinding.ItemCategoriaBinding

class CategoriasAdapter(
    private val categorias: List<Categoria>,
    private val onCategoriaSeleccionada: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriasAdapter.CategoriaViewHolder>() {

    private var seleccionadaPos = -1

    inner class CategoriaViewHolder(private val binding: ItemCategoriaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoria: Categoria, position: Int) {
            binding.txtNombre.text = categoria.nombre
            binding.imgIcono.setImageResource(categoria.icono)

            val background = if (position == seleccionadaPos)
                R.drawable.bg_categoria_seleccionada
            else
                R.drawable.bg_categoria_normal

            binding.container.setBackgroundResource(background)

            binding.container.setOnClickListener {
                val oldPos = seleccionadaPos
                seleccionadaPos = position
                notifyItemChanged(oldPos)
                notifyItemChanged(seleccionadaPos)
                onCategoriaSeleccionada(categoria)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val binding = ItemCategoriaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        holder.bind(categorias[position], position)
    }

    override fun getItemCount(): Int = categorias.size
}
