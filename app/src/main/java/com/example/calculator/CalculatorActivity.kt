package com.example.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.*
import kotlin.collections.ArrayList

// CalculatorActivity acts as a Controller between CalculatorModel and activity_main (Views)
class CalculatorActivity : AppCompatActivity() {

    private lateinit var cm: CalculatorModel
    private var mResultView: TextView? = null
    private var mIntermediateView: TextView? = null
    private var mButtons: ArrayList<Button>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cm = CalculatorModel()
        setViews()
    }

    // Sets Views, behaviors, descriptions for Calculator components
    private fun setViews() {
        mResultView = findViewById(R.id.result)
        mIntermediateView = findViewById(R.id.intermediate_result)

        mButtons = (findViewById<View>(R.id.parent_container)).touchables as ArrayList<Button>
        for (btn in mButtons!!) {
            btn.contentDescription = btn.text
            btn.setOnClickListener {
                if (btn.text != "=") {
                    cm.mIntermediateResult += btn.text
                    updateViews()
                } else {
                    cm.mFinalResult = cm.mIntermediateResult
                    calculate(cm.mFinalResult)
                }
            }
        }

        findViewById<Button>(R.id.left_par).contentDescription = "Left parentheses"
        findViewById<Button>(R.id.right_par).contentDescription = "Right parentheses"

        findViewById<Button>(R.id.div).contentDescription = "divide"
        findViewById<Button>(R.id.mult).contentDescription = "multiply"

        findViewById<Button>(R.id.sub).contentDescription = "subtract"
        findViewById<Button>(R.id.add).contentDescription = "add"

        findViewById<Button>(R.id.dot).contentDescription = "decimal point"

        // Cancel
        val cancelBtn = findViewById<Button>(R.id.cancel)
        cancelBtn.contentDescription = "cancel"
        cancelBtn.setOnClickListener {
            reset()
        }

        // Backspace
        val backspaceBtn = findViewById<Button>(R.id.back)
        backspaceBtn.contentDescription = "backspace"
        backspaceBtn.setOnClickListener {
            if (cm.mIntermediateResult.isNotEmpty()) {
                cm.mIntermediateResult = cm.mIntermediateResult.substring(0,
                        cm.mIntermediateResult.lastIndex)
                updateViews()
            }
        }
    }

    // Calculates and outputs a result for a given equation
    private fun calculate(stringResult: String) {
        try {
            val e = ExpressionBuilder(stringResult).build()
            val result = e.evaluate()
            cm.mFinalResult = result.toString()
            cm.mIntermediateResult = cm.mFinalResult
            updateViews()
        } catch (e: IllegalArgumentException) {
            handleError(stringResult)
        } catch (e: EmptyStackException) {
            handleError(stringResult)
        }
    }

    // Resets state of calculator model
    private fun reset() {
        cm.mIntermediateResult = ""
        cm.mFinalResult = "0.0"
        updateViews()
    }

    // Catches errors in expression parsing
    private fun handleError(causeOfError: String) {
        Log.i("DEBUG", "Invalid input: $causeOfError")
        mIntermediateView?.text = ""
    }

    // updates Views
    private fun updateViews() {
        mResultView?.text = cm.mFinalResult
        mIntermediateView?.text = cm.mIntermediateResult
    }
}