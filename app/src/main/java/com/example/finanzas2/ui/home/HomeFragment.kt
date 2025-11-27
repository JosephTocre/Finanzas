package com.example.finanzas2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzas2.R
import com.example.finanzas2.data.Movimiento
import com.example.finanzas2.data.MovimientoDao
import com.example.finanzas2.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MovimientosAdapter
    private lateinit var dao: MovimientoDao
    private var movimientos: List<Movimiento> = emptyList()
    private var saldoVisible = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dao = MovimientoDao(requireContext())
        movimientos = dao.obtenerMovimientos()

        adapter = MovimientosAdapter(movimientos) { movimiento ->
            mostrarDetalleMovimiento(movimiento)
        }
        binding.recyclerMovimientos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMovimientos.adapter = adapter

        actualizarSaldo()
        configurarSpinner()

        binding.recyclerMovimientos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.cardSaldo.visibility == View.VISIBLE) {
                    binding.cardSaldo.animate()
                        .translationY(-binding.cardSaldo.height.toFloat())
                        .setDuration(200)
                        .start()
                } else if (dy < 0 && binding.cardSaldo.translationY != 0f) {
                    binding.cardSaldo.animate()
                        .translationY(0f)
                        .setDuration(200)
                        .start()
                }
            }
        })

        binding.btnToggleSaldo.setOnClickListener {
            saldoVisible = !saldoVisible
            if (saldoVisible) {
                binding.txtSaldo.text = "S/ %.2f".format(calcularSaldo())
                binding.btnToggleSaldo.setImageResource(R.drawable.ic_eye)
            } else {
                binding.txtSaldo.text = "****"
                binding.btnToggleSaldo.setImageResource(R.drawable.eye_off)
            }
        }
    }

    private fun configurarSpinner() {
        val categorias = mutableListOf("Todas")
        categorias.addAll(movimientos.map { it.categoria }.distinct().sorted())

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner_categoria,
            categorias
        )
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_categoria)
        binding.spinnerFiltroCategoria.adapter = spinnerAdapter

        binding.spinnerFiltroCategoria.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val seleccion = categorias[position]
                movimientos = if (seleccion == "Todas") {
                    dao.obtenerMovimientos()
                } else {
                    dao.obtenerMovimientos().filter { it.categoria == seleccion }
                }
                adapter.actualizar(movimientos)
                actualizarSaldo()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun calcularSaldo(): Double {
        val totalIngresos = movimientos.filter { it.esIngreso }.sumOf { it.monto }
        val totalGastos = movimientos.filter { !it.esIngreso }.sumOf { it.monto }
        return totalIngresos - totalGastos
    }

    private fun actualizarSaldo() {
        binding.txtSaldo.text =
            if (saldoVisible) "S/ %.2f".format(calcularSaldo()) else "****"
    }

    private fun mostrarDetalleMovimiento(movimiento: Movimiento) {
        val view = layoutInflater.inflate(R.layout.dialog_detalle_movimiento, null)
        val fechaFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        view.findViewById<TextView>(R.id.txtTituloDetalle).text = movimiento.titulo
        view.findViewById<TextView>(R.id.txtMontoDetalle).text =
            if (movimiento.esIngreso) "+S/ ${movimiento.monto}" else "-S/ ${movimiento.monto}"
        view.findViewById<TextView>(R.id.txtCategoriaDetalle).text =
            "Categoría: ${movimiento.categoria}"
        view.findViewById<TextView>(R.id.txtFechaDetalle).text =
            "Fecha: ${fechaFormat.format(movimiento.fecha)}"
        view.findViewById<TextView>(R.id.txtNotaDetalle).text =
            "Nota: ${movimiento.note ?: "Sin nota"}"

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
