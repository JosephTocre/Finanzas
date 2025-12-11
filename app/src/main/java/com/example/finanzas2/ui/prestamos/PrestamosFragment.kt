package com.example.finanzas2.ui.prestamos

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzas2.databinding.FragmentPrestamosBinding
import java.io.File
import java.io.FileOutputStream
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

        binding.btnGuardarImagen.setOnClickListener {
            guardarCronogramaComoImagen()
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
    private fun guardarCronogramaComoImagen() {
        val adapter = binding.recyclerCuotas.adapter ?: return
        val itemCount = adapter.itemCount

        if (itemCount == 0) {
            Toast.makeText(requireContext(), "No hay datos para exportar", Toast.LENGTH_SHORT).show()
            return
        }

        val viewWidth = binding.recyclerCuotas.width
        var totalHeight = 0

        val paint = Paint()
        val itemBitmaps = mutableListOf<Bitmap>()

        for (i in 0 until itemCount) {
            val holder = adapter.createViewHolder(binding.recyclerCuotas, adapter.getItemViewType(i))
            adapter.onBindViewHolder(holder, i)

            holder.itemView.measure(
                View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            holder.itemView.layout(0, 0, viewWidth, holder.itemView.measuredHeight)

            val bitmap = Bitmap.createBitmap(
                viewWidth,
                holder.itemView.measuredHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)
            holder.itemView.draw(canvas)

            totalHeight += holder.itemView.measuredHeight
            itemBitmaps.add(bitmap)
        }

        val finalBitmap = Bitmap.createBitmap(viewWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val finalCanvas = Canvas(finalBitmap)

        var yOffset = 0
        for (bmp in itemBitmaps) {
            finalCanvas.drawBitmap(bmp, 0f, yOffset.toFloat(), paint)
            yOffset += bmp.height
            bmp.recycle()
        }

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, "CronogramaPrestamo.png")

        try {
            val output = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
            output.flush()
            output.close()

            Toast.makeText(requireContext(), "Imagen guardada en Descargas", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
