package com.example.finanzas2.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzas2.data.FinanzasRepo
import com.example.finanzas2.databinding.FragmentChartBinding
import java.text.NumberFormat
import java.util.*

class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DetallesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)

        val (ingresos, gastos) = FinanzasRepo.calcularTotales()

        val formato = NumberFormat.getCurrencyInstance(Locale("es", "PE"))
        binding.txtIngresos.text = "Ingresos: ${formato.format(ingresos)}"
        binding.txtGastos.text = "Gastos: ${formato.format(gastos)}"

        adapter = DetallesAdapter(emptyList())
        binding.recyclerDetalles.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDetalles.adapter = adapter

        // Gráfico circular con protección contra acceso a binding nulo
        GraficoCircular(binding.pieChart) { tipo ->
            // Verifica que la vista aún exista antes de actualizar
            if (_binding != null && isAdded) {
                val detalles = when (tipo) {
                    "Ingresos" -> FinanzasRepo.obtenerMovimientos().filter { it.esIngreso }
                    "Gastos" -> FinanzasRepo.obtenerMovimientos().filter { !it.esIngreso }
                    else -> emptyList()
                }

                binding.txtTituloLista.text = "Detalles de $tipo"
                adapter.actualizar(detalles)
            }
        }.mostrarGrafico(ingresos, gastos)

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
