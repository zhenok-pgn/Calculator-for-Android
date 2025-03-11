package com.example.calculator_for_android

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkScreenResolution()
        setButtonClickListeners()
    }

    //Проверка разрешения экрана устройства
    private fun checkScreenResolution() {
        val display = resources.displayMetrics
        var stop = true
        if (((display.heightPixels >= 1920) and (display.widthPixels >= 1000)) ||
            ((display.heightPixels >= 1000) and (display.widthPixels >= 1920))
        ) {
            stop = false
        }
        if (stop) {
            showErrorMessage("Несовместимое устройство! ($display.widthPixels x $display.heightPixels)")
        }
    }

    private fun showErrorMessage(message:String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Ошибка!")
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton("Выход"
            ) { dialog, id ->
                finishAffinity() // или другой метод
            } //builder
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun setButtonClickListeners() {
        findViewById<Button>(R.id.exit).setOnClickListener{finishAffinity()}
        findViewById<Button>(R.id.toCalculator).setOnClickListener{
            val intent = Intent(this, CalculatorActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)}
        findViewById<Button>(R.id.toGraphic).setOnClickListener{finishAffinity()}
    }
}