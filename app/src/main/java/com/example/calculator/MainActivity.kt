package com.example.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.IllegalArgumentException


class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultView = findViewById<TextView>(R.id.result)
        val intermedView = findViewById<TextView>(R.id.intermediate_result)
        val operators = arrayOf('+', '-', '/', '*')

        // All buttons (except =) have an opacity animation when touched and
        // its text is added to intermediate result
        val allButtons: ArrayList<Button> =
            (findViewById<View>(R.id.parent_container)).touchables as ArrayList<Button>
        for (btn in allButtons) {
            btn.setOnTouchListener { v, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> v?.alpha = 0.5f
                    MotionEvent.ACTION_UP -> {
                        v?.alpha = 1.0f
                        if (btn.text != "=") {
                            intermedView.text = ("" + intermedView.text + btn.text)
                        } else { // display solution
                            val stringResult = intermedView.text.toString()
                            try {
                                val e = ExpressionBuilder(stringResult).build()
                                val result = e.evaluate()
                                resultView.text = result.toString()
                                intermedView.text = resultView.text
                            } catch (e: IllegalArgumentException) {
                                Log.i("DEBUG", "Invalid input: $stringResult")
                                intermedView.text = ""
                            }
                        }
                    }
                }
                v?.onTouchEvent(event) ?: true
            }
        }

        // Cancel
        val cancelBtn = findViewById<Button>(R.id.cancel)
        cancelBtn.setOnClickListener {
            resultView.setText("0.0")
            intermedView.setText("")
        }

        // Backspace
        val backspaceBtn = findViewById<Button>(R.id.back)
        backspaceBtn.setOnClickListener {
            if (intermedView.text.isNotEmpty()) {
                intermedView.text = intermedView.text.substring(0, intermedView.text.lastIndex)
            }
        }
    }
}