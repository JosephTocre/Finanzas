package com.example.finanzas2.data

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class PdfMovimientosGenerator(private val context: Context) {

    fun generarYCompartirPdf(movimientos: List<Movimiento>) {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            var page = pdfDocument.startPage(pageInfo)
            var canvas = page.canvas

            val tituloPaint = Paint().apply {
                textSize = 20f
                color = Color.BLACK
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
            }

            val headerPaint = Paint().apply {
                textSize = 12f
                color = Color.BLACK
                isFakeBoldText = true
            }

            val normalPaint = Paint().apply {
                textSize = 11f
                color = Color.BLACK
            }

            val ingresoPaint = Paint().apply {
                textSize = 11f
                color = Color.rgb(0, 140, 0)
                isFakeBoldText = true
            }

            val gastoPaint = Paint().apply {
                textSize = 11f
                color = Color.rgb(180, 0, 0)
                isFakeBoldText = true
            }

            val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "PE"))
            val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

            var y = 50

            canvas.drawText("REPORTE FINANCIERO", 297f, y.toFloat(), tituloPaint)
            y += 30

            canvas.drawText(
                "Fecha de generación: ${formatoFecha.format(Date())}",
                50f,
                y.toFloat(),
                normalPaint
            )
            y += 25

            val totalIngresos = movimientos.filter { it.esIngreso }.sumOf { it.monto }
            val totalGastos = movimientos.filter { !it.esIngreso }.sumOf { it.monto }
            val saldo = totalIngresos - totalGastos

            canvas.drawText("Total Ingresos: ${formatoMoneda.format(totalIngresos)}", 50f, y.toFloat(), ingresoPaint)
            y += 18
            canvas.drawText("Total Gastos: ${formatoMoneda.format(totalGastos)}", 50f, y.toFloat(), gastoPaint)
            y += 18
            canvas.drawText("Saldo Final: ${formatoMoneda.format(saldo)}", 50f, y.toFloat(), headerPaint)

            y += 30

            dibujarFilaTabla(
                canvas,
                y,
                headerPaint,
                true,
                "Fecha",
                "Título",
                "Categoría",
                "Monto"
            )
            y += 25

            movimientos.forEach { mov ->
                val paintMonto = if (mov.esIngreso) ingresoPaint else gastoPaint

                dibujarFilaTabla(
                    canvas,
                    y,
                    normalPaint,
                    false,
                    formatoFecha.format(mov.fecha),
                    mov.titulo,
                    mov.categoria,
                    formatoMoneda.format(mov.monto),
                    paintMonto
                )

                y += 22

                if (y > 780) {
                    pdfDocument.finishPage(page)
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    y = 50
                }
            }

            pdfDocument.finishPage(page)

            val file = File(
                context.getExternalFilesDir(null),
                "ReporteFinanzas_${System.currentTimeMillis()}.pdf"
            )

            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(
                Intent.createChooser(shareIntent, "Compartir reporte PDF")
            )

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    private fun dibujarFilaTabla(
        canvas: android.graphics.Canvas,
        y: Int,
        paint: Paint,
        esHeader: Boolean,
        fecha: String,
        titulo: String,
        categoria: String,
        monto: String,
        paintMontoEspecial: Paint? = null
    ) {
        val inicioX = 40f
        val altoFila = 20f

        val colFecha = 100f
        val colTitulo = 170f
        val colCategoria = 150f
        val colMonto = 95f

        val bordePaint = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
        }

        if (esHeader) {
            val fondo = Paint().apply {
                style = Paint.Style.FILL
                color = Color.LTGRAY
            }
            canvas.drawRect(inicioX, y.toFloat(), inicioX + colFecha + colTitulo + colCategoria + colMonto, y + altoFila, fondo)
        }
        canvas.drawRect(inicioX, y.toFloat(), inicioX + colFecha, y + altoFila, bordePaint)
        canvas.drawRect(inicioX + colFecha, y.toFloat(), inicioX + colFecha + colTitulo, y + altoFila, bordePaint)
        canvas.drawRect(inicioX + colFecha + colTitulo, y.toFloat(), inicioX + colFecha + colTitulo + colCategoria, y + altoFila, bordePaint)
        canvas.drawRect(inicioX + colFecha + colTitulo + colCategoria, y.toFloat(), inicioX + colFecha + colTitulo + colCategoria + colMonto, y + altoFila, bordePaint)
        canvas.drawText(fecha.take(16), inicioX + 5, (y + 14).toFloat(), paint)
        canvas.drawText(titulo.take(18), inicioX + colFecha + 5, (y + 14).toFloat(), paint)
        canvas.drawText(categoria.take(15), inicioX + colFecha + colTitulo + 5,
            (y + 14).toFloat(), paint)
        canvas.drawText(
            monto,
            inicioX + colFecha + colTitulo + colCategoria + 5,
            (y + 14).toFloat(),
            paintMontoEspecial ?: paint
        )
    }
}
