package com.example.finanzas2.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.finanzas2.data.FinanzasRepo
import com.example.finanzas2.data.Movimiento
import com.example.finanzas2.databinding.FragmentAddBinding
import java.util.*

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.btnGuardar.setOnClickListener {
            val titulo = binding.edtTitulo.text.toString()
            val monto = binding.edtMonto.text.toString().toDoubleOrNull() ?: 0.0
            val esIngreso = binding.radioIngreso.isChecked

            val movimiento = Movimiento(titulo, monto, esIngreso, Date())
            FinanzasRepo.agregarMovimiento(movimiento)

            binding.txtConfirmacion.text = "Movimiento agregado ✔"
            binding.edtTitulo.text.clear()
            binding.edtMonto.text.clear()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
