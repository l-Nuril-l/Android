package com.example.testapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

private const val EXTRA_ANSWER_IS_TRUE = "com.example.testapplication.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.testapplication.extra_answer_shown"
private const val CHEATED = "cheated"

class CheatActivity : AppCompatActivity() {
    private var answerIsTrue = false
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private var cheated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        cheated = savedInstanceState?.getBoolean(CHEATED,false) ?: false
        setAnswerShownResult(cheated)


        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {
            cheated = true
            setAnswerShownResult(cheated)
            answerTextView.setText(
                when {
                    answerIsTrue -> R.string.true_button
                    else -> R.string.false_button
                }
            )
        }
    }

    private fun setAnswerShownResult(cheated: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN,cheated)
        }
        setResult(Activity.RESULT_OK,data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(CHEATED,cheated)
    }

    companion object{
        fun newIntent(packageContext: Context, answerIsTrue: Boolean) : Intent
        {
            return Intent(packageContext,CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue)
            }
        }
    }
}