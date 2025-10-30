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
import androidx.core.graphics.toColorInt

class GraficoCircular(
    private val pieChart: PieChart,
    private val onSectorSeleccionado: (String) -> Unit
) {

    fun mostrarGrafico(ingresos: Double, gastos: Double) {
        val ingresosF = if (ingresos == 0.0) 0.01f else ingresos.toFloat()
        val gastosF = if (gastos == 0.0) 0.01f else gastos.toFloat()

        val entries = listOf(
            PieEntry(ingresosF, "Ingresos"),
            PieEntry(gastosF, "Gastos")
        )

        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(
                "#2ECC71".toColorInt(),
                "#E74C3C".toColorInt()
            )
            valueTextSize = 12f
            valueTextColor = Color.WHITE
            sliceSpace = 3f
        }

        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(pieChart))
            setValueTextSize(12f)
        }

        pieChart.apply {
            this.data = data
            setUsePercentValues(true)
            description.isEnabled = false

            isDrawHoleEnabled = true
            holeRadius = 55f
            transparentCircleRadius = 62f

            setCenterText("Finanzas\nTotales")
            setCenterTextSize(16f)
            setCenterTextColor(Color.DKGRAY)

            setEntryLabelColor(Color.DKGRAY)
            legend.isEnabled = false

            animateY(1200)
            invalidate()
        }

        pieChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                onSectorSeleccionado((e as PieEntry).label)
            }
            override fun onNothingSelected() {}
        })
    }

}
