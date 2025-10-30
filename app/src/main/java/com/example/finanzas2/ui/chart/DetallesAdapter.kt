package com.example.finanzas2.ui.chart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzas2.data.Movimiento
import com.example.finanzas2.databinding.ItemDetalleBinding
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
        holder.binding.txtTituloDetalle.text = detalle.titulo
        holder.binding.txtMontoDetalle.text =
            if (detalle.esIngreso) "+${detalle.monto}" else "-${detalle.monto}"
        holder.binding.txtFechaDetalle.text =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(detalle.fecha)
    }

    override fun getItemCount(): Int = detalles.size

    fun actualizar(nuevosDetalles: List<Movimiento>) {
        detalles = nuevosDetalles
        notifyDataSetChanged()
    }
}
