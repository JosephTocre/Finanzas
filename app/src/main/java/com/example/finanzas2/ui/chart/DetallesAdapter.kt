package com.example.finanzas2.ui.chart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzas2.data.Movimiento
import com.example.finanzas2.databinding.ItemDetalleBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetallesAdapter(private var detalles: List<Movimiento>) :
    RecyclerView.Adapter<DetallesAdapter.DetalleViewHolder>() {

    class DetalleViewHolder(val binding: ItemDetalleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetalleViewHolder {
        val binding = ItemDetalleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetalleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetalleViewHolder, position: Int) {
        val detalle = detalles[position]

        val formato = NumberFormat.getCurrencyInstance(Locale("es", "PE"))
        val fechaFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        holder.binding.txtTituloDetalle.text = detalle.titulo
        holder.binding.txtMontoDetalle.text =
            if (detalle.esIngreso) "+${formato.format(detalle.monto)}"
            else "-${formato.format(detalle.monto)}"

        holder.binding.txtFechaDetalle.text = fechaFormat.format(detalle.fecha)

        val color = if (detalle.esIngreso) android.R.color.holo_green_dark
        else android.R.color.holo_red_dark

        holder.binding.txtMontoDetalle.setTextColor(
            holder.itemView.context.getColor(color)
        )
    }

    override fun getItemCount(): Int = detalles.size

    fun actualizar(nuevosDetalles: List<Movimiento>) {
        detalles = nuevosDetalles
        notifyDataSetChanged()
    }
}
