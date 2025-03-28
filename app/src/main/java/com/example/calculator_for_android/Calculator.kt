package com.example.calculator_for_android

class Calculator {
    private var currentExpression = StringBuilder()

    // Добавляем символ в выражение
    fun appendToInput(text: String): String {
        currentExpression.append(text)
        return currentExpression.toString()
    }

    // Очищаем выражение
    fun clearInput(): String {
        currentExpression.setLength(0)
        return ""
    }

    // Вычисляем результат выражения
    fun calculateResult(): String {
        return try {
            val expression = currentExpression.toString()
            val result = eval(expression)
            currentExpression.setLength(0)
            currentExpression.append(result)
            result.toString()
        } catch (e: Exception) {
            "Ошибка"
        }
    }

    // Функция для вычисления выражения
    private fun eval(expression: String): Double {
        return object {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < expression.length) expression[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expression.length) throw RuntimeException("Непредвиденный символ: ${ch.toChar()}")
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+'.code) -> x += parseTerm() // Сложение
                        eat('-'.code) -> x -= parseTerm() // Вычитание
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('*'.code) -> x *= parseFactor() // Умножение
                        eat('/'.code) -> x /= parseFactor() // Деление
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor() // Унарный плюс
                if (eat('-'.code)) return -parseFactor() // Унарный минус

                val startPos = pos
                return when {
                    eat('('.code) -> { // Скобки
                        val x = parseExpression()
                        eat(')'.code)
                        x
                    }
                    ch in '0'.code..'9'.code || ch == '.'.code -> { // Числа
                        while (ch in '0'.code..'9'.code || ch == '.'.code) nextChar()
                        expression.substring(startPos, pos).toDouble()
                    }
                    else -> throw RuntimeException("Непредвиденный символ: ${ch.toChar()}")
                }
            }
        }.parse()
    }
}