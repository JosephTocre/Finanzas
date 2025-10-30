package com.example.finanzas2.ui.chart

import android.graphics.Color
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.data.Entry

class GraficoCircular(
    private val pieChart: PieChart,
    private val onSectorSeleccionado: (String) -> Unit
) {

    fun mostrarGrafico(ingresos: Double, gastos: Double) {
        val total = ingresos + gastos
        if (total == 0.0) return

        val entries = listOf(
            PieEntry(ingresos.toFloat(), "Ingresos"),
            PieEntry(gastos.toFloat(), "Gastos")
        )

        val dataSet = PieDataSet(entries, "Resumen Financiero").apply {
            colors = listOf(Color.parseColor("#4CAF50"), Color.parseColor("#F44336"))
            valueTextSize = 14f
            valueTextColor = Color.WHITE
            sliceSpace = 2f
        }

        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(pieChart))
            setValueTextSize(14f)
            setValueTextColor(Color.WHITE)
        }

        pieChart.apply {
            this.data = data
            setUsePercentValues(true)
            description.isEnabled = false
            isDrawHoleEnabled = true
            holeRadius = 45f
            transparentCircleRadius = 50f
            setCenterText("Finanzas Totales")
            setCenterTextSize(16f)
            setEntryLabelColor(Color.BLACK)
            animateY(1000)
            legend.isEnabled = true
            invalidate()
        }

        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val entry = e as PieEntry
                onSectorSeleccionado(entry.label)
            }

            override fun onNothingSelected() {}
        })
    }
}
