package com.example.finanzas2.ui.prestamos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzas2.databinding.FragmentPrestamosBinding
import kotlin.math.pow

class PrestamosFragment : Fragment() {

    private var _binding: FragmentPrestamosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CuotasAdapter
    private val cuotasList = mutableListOf<CuotaModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrestamosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CuotasAdapter(cuotasList)
        binding.recyclerCuotas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerCuotas.adapter = adapter

        binding.btnCalcularPrestamo.setOnClickListener {
            calcularPrestamo()
        }
    }

    private fun calcularPrestamo() {
        val monto = binding.edtMontoPrestamo.text.toString().toDoubleOrNull()
        val tasa = binding.edtInteres.text.toString().toDoubleOrNull()
        val meses = binding.edtMeses.text.toString().toIntOrNull()

        if (monto == null || tasa == null || meses == null || meses <= 0) {
            binding.txtTotalPagar.text = "Datos inválidos"
            return
        }

        val tasaDecimal = tasa / 100.0

        val cuota = if (tasaDecimal == 0.0) {
            monto / meses
        } else {
            val factor = (1 + tasaDecimal).pow(meses)
            monto * (tasaDecimal * factor) / (factor - 1)
        }

        val totalPagar = cuota * meses

        binding.layoutResultados.visibility = View.VISIBLE
        binding.txtCuotasTitulo.visibility = View.VISIBLE
        binding.recyclerCuotas.visibility = View.VISIBLE

        binding.txtTotalPagar.text = "Total a pagar: S/ ${"%.2f".format(totalPagar)}"
        binding.txtPagoMensual.text = "Pago mensual: S/ ${"%.2f".format(cuota)}"

        generarCuotas(monto, tasaDecimal, meses, cuota)
    }

    private fun generarCuotas(monto: Double, tasaMensual: Double, meses: Int, cuota: Double) {
        cuotasList.clear()
        var saldo = monto

        for (i in 1..meses) {

            val interesMes = saldo * tasaMensual
            val amortizacion = cuota - interesMes
            saldo -= amortizacion

            if (saldo < 0) saldo = 0.0

            cuotasList.add(
                CuotaModel(
                    numero = i,
                    cuota = cuota,
                    interes = interesMes,
                    saldoRestante = saldo
                )
            )
        }

        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
