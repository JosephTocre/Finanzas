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
    private var listaMovimientos: List<Movimiento>
) : RecyclerView.Adapter<MovimientosAdapter.MovimientoViewHolder>() {

    inner class MovimientoViewHolder(val binding: ItemMovimientoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientoViewHolder {
        val binding = ItemMovimientoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovimientoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovimientoViewHolder, position: Int) {
        val mov = listaMovimientos[position]
        val context = holder.itemView.context

        with(holder.binding) {
            // 🔹 Título
            txtTitulo.text = mov.titulo

            // 🔹 Categoría
            txtCategoria.text = mov.categoria

            // 🔹 Fecha formateada
            val formato = SimpleDateFormat("dd MMM yyyy", Locale("es", "ES"))
            txtFecha.text = formato.format(mov.fecha)

            // 🔹 Formato del monto con color y símbolo
            val montoFormateado = "S/ %.2f".format(mov.monto)
            if (mov.esIngreso) {
                txtMonto.text = "+$montoFormateado"
                txtMonto.setTextColor(ContextCompat.getColor(context, R.color.green_primary))
            } else {
                txtMonto.text = "-$montoFormateado"
                txtMonto.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
    }

    override fun getItemCount(): Int = listaMovimientos.size

    // 🔹 Método usado por HomeFragment
    fun actualizarLista(nuevaLista: List<Movimiento>) {
        listaMovimientos = nuevaLista
        notifyDataSetChanged()
    }
}
