package com.example.finanzas2.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzas2.data.Movimiento
import com.example.finanzas2.databinding.ItemMovimientoBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MovimientosAdapter(private var listaMovimientos: List<Movimiento>) :
    RecyclerView.Adapter<MovimientosAdapter.MovimientoViewHolder>() {

    class MovimientoViewHolder(val binding: ItemMovimientoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val binding = ItemMovimientoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovimientoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val movimiento = listaMovimientos[position]
        val formato = NumberFormat.getCurrencyInstance(Locale("es", "PE"))
        val fechaFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        with(holder.binding) {
            txtTitulo.text = movimiento.titulo
            txtCategoria.text = movimiento.categoria
            txtMonto.text =
                if (movimiento.esIngreso) "+${formato.format(movimiento.monto)}"
                else "-${formato.format(movimiento.monto)}"
            txtFecha.text = fechaFormat.format(movimiento.fecha)

            val color = if (movimiento.esIngreso)
                android.R.color.holo_green_dark
            else
                android.R.color.holo_red_dark

            txtMonto.setTextColor(holder.itemView.context.getColor(color))
        }
    }

    override fun getItemCount(): Int = listaMovimientos.size

    // ✅ Método para actualizar la lista de movimientos
    fun actualizar(nuevosMovimientos: List<Movimiento>) {
        listaMovimientos = nuevosMovimientos
        notifyDataSetChanged()
    }
}
