package com.example.finanzas2.ui.prestamos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzas2.databinding.ItemCuotaBinding

class CuotasAdapter(
    private val lista: List<CuotaModel>
) : RecyclerView.Adapter<CuotasAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCuotaBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCuotaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        holder.binding.txtNumeroCuota.text = "Cuota ${item.numero}"
        holder.binding.txtMontoCuota.text = "S/ ${"%.2f".format(item.cuota)}"
        holder.binding.txtInteresCuota.text = "Interés: S/ ${"%.2f".format(item.interes)}"
        holder.binding.txtSaldoCuota.text = "Saldo: S/ ${"%.2f".format(item.saldoRestante)}"
    }
}
