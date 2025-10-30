package com.example.finanzas2.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzas2.R
import com.example.finanzas2.data.Movimiento
import com.example.finanzas2.databinding.ItemMovimientoBinding
import java.text.SimpleDateFormat
import java.util.*

class MovimientosAdapter(
    private var movimientos: List<Movimiento>
) : RecyclerView.Adapter<MovimientosAdapter.MovimientoViewHolder>() {

    class MovimientoViewHolder(val binding: ItemMovimientoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val binding = ItemMovimientoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovimientoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val mov = movimientos[position]

        holder.binding.txtTitulo.text = mov.titulo

        val montoFormateado = "S/ %.2f".format(mov.monto)

        holder.binding.txtMonto.text =
            if (mov.esIngreso) "+$montoFormateado" else "-$montoFormateado"

        holder.binding.txtFecha.text =
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(mov.fecha)

        val context = holder.itemView.context
        val color = if (mov.esIngreso)
            ContextCompat.getColor(context, R.color.green_primary)
        else
            ContextCompat.getColor(context, R.color.red)

        holder.binding.txtMonto.setTextColor(color)
    }

    override fun getItemCount(): Int = movimientos.size

    fun actualizar(lista: List<Movimiento>) {
        movimientos = lista
        notifyDataSetChanged()
    }
}
