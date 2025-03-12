package com.example.calculator_for_android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GraphicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_graphic)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val width = width.toFloat()
            val height = height.toFloat()

            // Рисуем координатную сетку
            drawGrid(canvas, width, height)

            // Рисуем график функции y = sin(x)
            drawSinGraph(canvas, width, height)
        }

        private fun drawGrid(canvas: Canvas, width: Float, height: Float) {
            val step = 30f // Шаг сетки

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

        private fun drawSinGraph(canvas: Canvas, width: Float, height: Float) {
            val centerY = height / 2 // Центр по оси Y
            val scale = 100f // Масштаб графика
            val stepLenX = 5f / scale // Размер шага по Ox (Детализация функции)

            var prevDrawPointX = 0f
            var prevDrawPointY = centerY

            val stepCountX = (width / scale / stepLenX).toInt()
            for (i in 0..stepCountX) {
                val x = (i - stepCountX / 2) * stepLenX
                val y = kotlin.math.sin(2 * x) + kotlin.math.cos(3 * x)

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