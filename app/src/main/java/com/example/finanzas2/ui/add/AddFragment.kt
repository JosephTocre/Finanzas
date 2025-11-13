package com.example.finanzas2.ui.add

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzas2.R
import com.example.finanzas2.data.Categoria
import com.example.finanzas2.data.FinanzasRepo
import com.example.finanzas2.data.Movimiento
import com.example.finanzas2.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private var categoriaSeleccionada: Categoria? = null
    private var tipoSeleccionado: String? = null // "Ingreso" o "Gasto"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lista de categorías con sus iconos
        val listaCategorias = listOf(
            Categoria("Remuneración", R.drawable.ic_remuneracion),
            Categoria("Alimentación", R.drawable.ic_alimentacion),
            Categoria("Vivienda", R.drawable.ic_vivienda),
            Categoria("Transporte", R.drawable.ic_transporte),
            Categoria("Social", R.drawable.ic_social),
            Categoria("Regalos", R.drawable.ic_regalos),
            Categoria("Comunicación", R.drawable.ic_comunicacion),
            Categoria("Ropa", R.drawable.ic_ropa),
            Categoria("Recreación", R.drawable.ic_recreacion),
            Categoria("Salud", R.drawable.ic_salud),
            Categoria("Préstamos", R.drawable.ic_prestamos),
            Categoria("Extras", R.drawable.ic_extras)
        )

        // Configurar RecyclerView horizontal
        val adapter = CategoriasAdapter(listaCategorias) { categoria ->
            categoriaSeleccionada = categoria
        }
        binding.recyclerCategorias.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerCategorias.adapter = adapter

        // ChipGroup de tipo Ingreso/Gasto
        binding.radioGroupTipo.setOnCheckedChangeListener { _, checkedId ->
            tipoSeleccionado = when (checkedId) {
                binding.radioIngreso.id -> "Ingreso"
                binding.radioGasto.id -> "Gasto"
                else -> null
            }
        }

        // Botón Guardar
        binding.btnGuardar.setOnClickListener {
            val titulo = binding.edtTitulo.text.toString().trim()
            val montoText = binding.edtMonto.text.toString().trim()

            // Validaciones
            if (titulo.isEmpty() || montoText.isEmpty() || categoriaSeleccionada == null || tipoSeleccionado == null) {
                Snackbar.make(binding.root, "Completa todos los campos", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monto = montoText.toDoubleOrNull()
            if (monto == null) {
                Snackbar.make(binding.root, "Monto inválido", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear movimiento
            val movimiento = Movimiento(
                titulo = titulo,
                monto = monto,
                esIngreso = tipoSeleccionado == "Ingreso",
                fecha = Date(),
                categoria = categoriaSeleccionada!!.nombre
            )

            FinanzasRepo.agregarMovimiento(movimiento)

            // Confirmación
            binding.txtConfirmacion.text = "Movimiento registrado"

            // 🕒 Ocultar el texto después de 0.5 segundos
            Handler(Looper.getMainLooper()).postDelayed({
                binding.txtConfirmacion.text = ""
            }, 500)

            // Limpiar campos
            binding.edtTitulo.text?.clear()
            binding.edtMonto.text?.clear()
            binding.radioGroupTipo.clearCheck()
            categoriaSeleccionada = null
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
