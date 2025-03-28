package com.example.calculator_for_android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GraphicActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private val scale = 100f // Масштаб графика
    private val stepLenX = 5f / scale // Размер шага по Ox (Детализация функции)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_graphic)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        // Устанавливаем кастомный View для Canvas
        findViewById<FrameLayout>(R.id.canvasView).addView(GraphView(this))

        // Обработка нажатия на кнопку "Выход"
        findViewById<Button>(R.id.exitButton).setOnClickListener {
            finish() // Закрыть активность и вернуться на предыдущую
        }
    }

    // Кастомный View для рисования
    private inner class GraphView(context: Context) : View(context) {

        private val paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 2f
            isAntiAlias = true
        }

        private val gridPaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 1f
        }

        private val axisPaint = Paint().apply {
            color = Color.CYAN
            strokeWidth = 2f
        }

        private val graphPaint = Paint().apply {
            color = Color.RED
            strokeWidth = 3f
        }

        private val textPaint = Paint().apply {
            color = Color.CYAN
            textSize = 20f

        }

        private  val extremePaint = Paint().apply {
            color = Color.WHITE
            strokeWidth = 20f
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            // Рисуем координатную сетку
            drawGrid(canvas)

            // Очистка бд
            dbHelper.clearTable()
            // Рисуем график функции
            drawSinGraph(canvas)

            // рисуем экстремумы
            val extremes = findExtremes(dbHelper.getAllPoints())
            drawExtremes(extremes, canvas)
        }

        private fun findExtremes(points: List<PointF>): List<PointF> {
            val extremes = mutableListOf<PointF>()
            for (i in 1 until points.size - 1) {
                val yPrev = points[i - 1].y
                val yCurr = points[i].y
                val yNext = points[i + 1].y

                if ((yCurr > yPrev && yCurr > yNext) || (yCurr < yPrev && yCurr < yNext)) {
                    extremes.add(PointF(points[i].x, yCurr))
                }
            }
            return extremes
        }

        private  fun drawExtremes(points: List<PointF>, canvas: Canvas){
            val centerY = height / 2
            val stepCountX = (width.toFloat() / scale / stepLenX).toInt()
            for (point in points){
                val i = (point.x + stepCountX * stepLenX / 2) / stepLenX

                val drawPointX = i * stepLenX * scale
                val drawPointY = centerY - point.y * scale

                canvas.drawPoint(drawPointX, drawPointY, extremePaint)
            }
        }

        private fun drawGrid(canvas: Canvas) {
            val step = 30f // Шаг сетки
            val height = height.toFloat()
            val width = width.toFloat()

            // Вертикальные линии
            var x = 0f
            while (x <= width) {
                canvas.drawLine(x, 0f, x, height, gridPaint)
                x += step
            }

            // Горизонтальные линии
            var y = 0f
            while (y <= height) {
                canvas.drawLine(0f, y, width, y, gridPaint)
                y += step
            }

            // Абсцисса и ордината
            canvas.drawLine(width / 2, 0f, width / 2, height, axisPaint)
            canvas.drawLine(0f, height / 2, width, height / 2, axisPaint)
        }

        private fun drawSinGraph(canvas: Canvas) {
            val height = height.toFloat()
            val width = width.toFloat()
            val centerY = height / 2

            var prevDrawPointX = 0f
            var prevDrawPointY = centerY

            val stepCountX = (width / scale / stepLenX).toInt()
            for (i in 0..stepCountX) {
                val x = (i - stepCountX / 2) * stepLenX
                val y = kotlin.math.sin(2 * x) + kotlin.math.cos(3 * x)

                // добавить в бд
                dbHelper.insertPoint(x.toDouble(), y.toDouble())

                val drawPointX = i * stepLenX * scale
                val drawPointY = centerY - y * scale
                canvas.drawLine(prevDrawPointX, prevDrawPointY, drawPointX, drawPointY, graphPaint)
                if(x % (50f / scale) == 0f)
                    canvas.drawText(x.toString(), drawPointX, centerY, textPaint)

                prevDrawPointX = drawPointX
                prevDrawPointY = drawPointY
            }
        }
    }
}