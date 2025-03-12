package com.example.calculator_for_android

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalculatorActivity : AppCompatActivity() {
    private lateinit var inputField: EditText
    private lateinit var calculator: Calculator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.calculator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calculator)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Находим поле ввода
        inputField = findViewById(R.id.inputField)

        // Создаем экземпляр калькулятора
        calculator = Calculator()

        setButtonClickListeners()
    }

    private fun setButtonClickListeners() {
        // Находим все кнопки и назначаем обработчик
        val buttonIds = arrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide,
            R.id.btnDot, R.id.btnOpenBracket, R.id.btnCloseBracket, R.id.btnClear, R.id.btnEquals, R.id.btnBack
        )

        for (id in buttonIds) {
            val button = findViewById<Button>(id)
            button.setOnClickListener(this::onButtonClick)
        }
    }

    // Обработчик нажатий на кнопки
    private fun onButtonClick(view: View) {
        val button = view as Button

        when ( val buttonText = button.text.toString()) {
            "=" -> inputField.setText(calculator.calculateResult())
            "C" -> inputField.setText(calculator.clearInput())
            "←" -> finish()
            else -> inputField.setText(calculator.appendToInput(buttonText))
        }
    }
}