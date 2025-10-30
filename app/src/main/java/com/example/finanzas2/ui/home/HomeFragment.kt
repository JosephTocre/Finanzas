package com.example.finanzas2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzas2.data.FinanzasRepo
import com.example.finanzas2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MovimientosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapter = MovimientosAdapter(FinanzasRepo.obtenerMovimientos())
        binding.recyclerMovimientos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMovimientos.adapter = adapter

        actualizarSaldo()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        adapter.actualizar(FinanzasRepo.obtenerMovimientos())
        actualizarSaldo()
    }

    private fun actualizarSaldo() {
        val saldo = FinanzasRepo.calcularSaldo()
        binding.txtSaldo.text = "Saldo: $saldo"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
