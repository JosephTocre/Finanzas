package com.example.finanzas2.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzas2.data.FinanzasRepo
import com.example.finanzas2.databinding.FragmentChartBinding

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
        binding.txtIngresos.text = "Ingresos: $ingresos"
        binding.txtGastos.text = "Gastos: $gastos"

        // Configurar lista vacía inicialmente
        adapter = DetallesAdapter(emptyList())
        binding.recyclerDetalles.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDetalles.adapter = adapter

        // Pasamos una lambda al gráfico para actualizar la lista cuando se toca un sector
        GraficoCircular(binding.pieChart) { tipo ->
            val detalles = when (tipo) {
                "Ingresos" -> FinanzasRepo.obtenerMovimientos().filter { it.esIngreso }
                "Gastos" -> FinanzasRepo.obtenerMovimientos().filter { !it.esIngreso }
                else -> emptyList()
            }
            binding.txtTituloLista.text = "Detalles de $tipo"
            adapter.actualizar(detalles)
        }.mostrarGrafico(ingresos, gastos)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
