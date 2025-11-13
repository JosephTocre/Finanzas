package com.example.finanzas2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzas2.data.FinanzasRepo
import com.example.finanzas2.databinding.FragmentHomeBinding
import com.example.finanzas2.ui.home.MovimientosAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MovimientosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovimientosAdapter(FinanzasRepo.obtenerMovimientos())
        binding.recyclerMovimientos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMovimientos.adapter = adapter

        actualizarSaldo()

        val categorias = mutableListOf("Todas")
        categorias.addAll(FinanzasRepo.obtenerMovimientos()
            .map { it.categoria }
            .distinct()
            .sorted())

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            com.example.finanzas2.R.layout.item_spinner_categoria,
            categorias
        )
        spinnerAdapter.setDropDownViewResource(com.example.finanzas2.R.layout.item_spinner_categoria)
        binding.spinnerFiltroCategoria.adapter = spinnerAdapter

        binding.spinnerFiltroCategoria.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val seleccion = categorias[position]
                if (seleccion == "Todas") {
                    adapter.actualizar(FinanzasRepo.obtenerMovimientos())
                } else {
                    val filtrados = FinanzasRepo.obtenerMovimientos()
                        .filter { it.categoria == seleccion }
                    adapter.actualizar(filtrados)
                }
                actualizarSaldo()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun actualizarSaldo() {
        val totalIngresos = FinanzasRepo.obtenerMovimientos().filter { it.esIngreso }.sumOf { it.monto }
        val totalGastos = FinanzasRepo.obtenerMovimientos().filter { !it.esIngreso }.sumOf { it.monto }
        val saldo = totalIngresos - totalGastos
        binding.txtSaldo.text = "S/ %.2f".format(saldo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
