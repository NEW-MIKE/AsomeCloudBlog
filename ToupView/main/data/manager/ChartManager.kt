package com.example.astroclient.manager

import android.graphics.Color
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


class ChartManager(val mCombinedChart: CombinedChart) {
    private val leftAxis: YAxis = mCombinedChart.axisLeft
    private val rightAxis: YAxis = mCombinedChart.axisRight

    private fun initChart() {
        mCombinedChart.apply {
            description.isEnabled = false
            drawOrder = arrayOf(
                DrawOrder.BAR,
                DrawOrder.LINE
            )
            setBackgroundColor(Color.TRANSPARENT)
            setDrawBorders(true)
            setBorderColor(Color.GRAY)
        }

        val legend = mCombinedChart.legend
        legend.apply {
            isWordWrapEnabled = true
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
            textColor = Color.GRAY
        }

        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)
        leftAxis.axisMinimum = -5f
        leftAxis.axisMaximum = 5f
        leftAxis.textColor = Color.GRAY
    }

    fun setXAxis(maxisMaximum: Float) {
        val xAxis = mCombinedChart.xAxis
        xAxis.apply {
            axisMaximum = maxisMaximum
            granularity = 1f
            setDrawLabels(false)
        }
        mCombinedChart.invalidate()
    }

    private fun getLineData(
        lineChartYs: List<List<Float?>>,
        lineNames: List<String>,
        lineColors: List<Int>
    ): LineData? {
        val lineData = LineData()
        for (i in lineChartYs.indices) {
            val yValues = ArrayList<Entry>()
            for (j in lineChartYs[i].indices) {
                lineChartYs.get(i).get(j)?.let {
                    yValues.add(Entry(j.toFloat(), it))
                }
            }
            val dataSet = LineDataSet(yValues, lineNames[i])
            dataSet.apply {
                color = lineColors[i]
                setDrawCircles(false)
                setDrawValues(false)
            }
            lineData.addDataSet(dataSet)
        }
        return lineData
    }

    private fun getBarData(
        barChartYs: List<List<Float>>,
        barNames: List<String>,
        barColors: List<Int>
    ): BarData? {
        val lists = ArrayList<IBarDataSet>()
        for (i in barChartYs.indices) {
            val entries = ArrayList<BarEntry>()
            for (j in barChartYs[i].indices) {
                entries.add(BarEntry(j.toFloat(), barChartYs[i][j]))
            }
            val barDataSet = BarDataSet(entries, barNames[i])
            barDataSet.apply {
                setColor(barColors[i])
                setDrawValues(false)
            }
            lists.add(barDataSet)
        }
        val barData = BarData(lists)
        val amount = barChartYs.size
        val groupSpace = 0.12f
        val barSpace = ((1 - 0.12) / amount / 10).toFloat()
        val barWidth = ((1 - 0.12) / amount / 10 * 9).toFloat()

        barData.barWidth = barWidth
        barData.groupBars(0f, groupSpace, barSpace)
        return barData
    }

    fun showCombinedChart(
        maxisMaximum: Float,
        barChartYs: List<List<Float>>,
        lineChartYs: List<List<Float?>>,
        barNames: List<String>,
        lineNames: List<String>,
        barColors: List<Int>,
        lineColors: List<Int>
    ) {
        initChart()
        setXAxis(maxisMaximum)
        val combinedData = CombinedData()
        combinedData.setData(getBarData(barChartYs, barNames, barColors))
        combinedData.setData(getLineData(lineChartYs, lineNames, lineColors))
        mCombinedChart.data = combinedData
        mCombinedChart.invalidate()
    }
}