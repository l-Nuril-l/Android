package com.example.testapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val quiz: QuizViewModel by lazy {  ViewModelProvider(this).get(QuizViewModel::class.java) }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                quiz.isCheated = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN,false) ?: false
            }
        }

        quiz.currentIndex = savedInstanceState?.getInt(KEY_INDEX,0) ?: 0

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)

        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)


        trueButton.setOnClickListener {
            checkAnswer(true)
            quiz.moveToNext()
            updateQuestion()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            quiz.moveToNext()
            updateQuestion()
        }

        nextButton.setOnClickListener {
            quiz.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quiz.moveToPrev()
            updateQuestion()
        }

        questionTextView.setOnClickListener{
            quiz.moveToNext()
            updateQuestion()
        }

        cheatButton.setOnClickListener{
            resultLauncher.launch(CheatActivity.newIntent(this@MainActivity,quiz.currentQuestionAnswer))
        }

        updateQuestion()
    }
    private fun updateQuestion() {
        questionTextView.setText(quiz.currentQuestionText)
        trueButton.isEnabled = !quiz.hasAnswer()
        falseButton.isEnabled = !quiz.hasAnswer()
        cheatButton.isEnabled = !quiz.hasCheat()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX,quiz.currentIndex)
    }


    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quiz.currentQuestionAnswer

        val messageResId: Int = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }


        if(quiz.isCheated)
            Toast.makeText(this,  R.string.cheat_is_bad_text, Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this,  messageResId, Toast.LENGTH_SHORT).show()

        quiz.addAnswer(userAnswer)

        if(quiz.finish())
        {
            Toast.makeText(this,  quiz.getResultString(), Toast.LENGTH_SHORT).show()
        }
    }
}