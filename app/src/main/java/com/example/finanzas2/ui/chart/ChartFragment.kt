package com.example.finanzas2.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzas2.data.MovimientoDao
import com.example.finanzas2.data.Movimiento
import com.example.finanzas2.data.PdfMovimientosGenerator
import com.example.finanzas2.databinding.FragmentChartBinding
import java.text.NumberFormat
import java.util.*

class ChartFragment : Fragment() {
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DetallesAdapter
    private lateinit var dao: MovimientoDao
    private var movimientos: List<Movimiento> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)

        dao = MovimientoDao(requireContext())
        movimientos = dao.obtenerMovimientos()

        val ingresos = movimientos.filter { it.esIngreso }.sumOf { it.monto }
        val gastos = movimientos.filter { !it.esIngreso }.sumOf { it.monto }

        val formato = NumberFormat.getCurrencyInstance(Locale("es", "PE"))
        binding.txtIngresos.text = "Ingresos: ${formato.format(ingresos)}"
        binding.txtGastos.text = "Gastos: ${formato.format(gastos)}"

        adapter = DetallesAdapter(emptyList())
        binding.recyclerDetalles.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDetalles.adapter = adapter

        GraficoCircular(binding.pieChart) { tipo ->
            if (_binding != null && isAdded) {
                val detalles = when (tipo) {
                    "Ingresos" -> movimientos.filter { it.esIngreso }
                    "Gastos" -> movimientos.filter { !it.esIngreso }
                    else -> emptyList()
                }

                binding.txtTituloLista.text = "Detalles de $tipo"
                adapter.actualizar(detalles)
            }
        }.mostrarGrafico(ingresos, gastos)

        binding.btnExportarPdf.setOnClickListener {
            val pdfGenerator = PdfMovimientosGenerator(requireContext())
            pdfGenerator.generarYCompartirPdf(movimientos)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
