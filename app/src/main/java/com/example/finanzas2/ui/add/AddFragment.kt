package com.example.finanzas2.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.finanzas2.data.FinanzasRepo
import com.example.finanzas2.data.Movimiento
import com.example.finanzas2.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar
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
            val titulo = binding.edtTitulo.text.toString().trim()
            val montoTexto = binding.edtMonto.text.toString().trim()
            val monto = montoTexto.toDoubleOrNull()

            if (titulo.isEmpty()) {
                binding.edtTitulo.error = "Ingresa un título"
                return@setOnClickListener
            }

            if (montoTexto.isEmpty()) {
                binding.edtMonto.error = "Ingresa un monto"
                return@setOnClickListener
            }

            if (monto == null) {
                binding.edtMonto.error = "Monto inválido"
                return@setOnClickListener
            }

            val esIngreso = binding.radioIngreso.isChecked

            // Crear el movimiento y guardar
            val movimiento = Movimiento(titulo, monto, esIngreso, Date())
            FinanzasRepo.agregarMovimiento(movimiento)

            // Limpieza
            binding.edtTitulo.text?.clear()
            binding.edtMonto.text?.clear()
            binding.radioIngreso.isChecked = true
            binding.txtConfirmacion.text = "Movimiento agregado exitosamente"

            binding.txtConfirmacion.postDelayed({
                binding.txtConfirmacion.text = ""
            }, 3000)

            Snackbar.make(binding.root, "Movimiento registrado", Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
