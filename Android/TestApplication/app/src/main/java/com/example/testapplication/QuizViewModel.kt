package com.example.testapplication

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val questionList = listOf(
        Question(R.string.question_cpp,false),
        Question(R.string.question_js,true),
        Question(R.string.question_php,false),
        Question(R.string.question_margo,true),
        Question(R.string.question_system,true),
        Question(R.string.question_dl2,true),
        Question(R.string.question_web,true),
        Question(R.string.question_pi,true),
        Question(R.string.question_test,false),
        Question(R.string.question_100,false),
    )

    private val cheatedList = hashSetOf<Int>()
    var currentIndex = 0

    private var answers = mutableMapOf<Int,Boolean>()

    var isCheated: Boolean
        get() = cheatedList.contains(currentIndex)
        set(value) { if(value) cheatedList.add(currentIndex) }

    val currentQuestionAnswer: Boolean
        get() = questionList[currentIndex].answer

    val currentQuestionText: Int
        get() = questionList[currentIndex].textResId


    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionList.size
    }

    fun moveToPrev(){
        currentIndex = if(currentIndex == 0) questionList.size - 1 else currentIndex - 1
    }

    fun addAnswer(answer: Boolean){
        answers[currentIndex] = answer
    }

    fun finish(): Boolean {
        return answers.size == 10
    }

    fun getResultString(): String {
        var sum = 0
        answers.forEach{
            if (questionList[it.key].answer == it.value) sum++
        }
        var res = sum.toString() + " / " + questionList.size
        answers.clear()
        if(cheatedList.size > 0) res += " Cheated: " + cheatedList.size + " times!"
        cheatedList.clear()
        return res
    }

    fun hasAnswer(): Boolean {
        return answers.containsKey(currentIndex)
    }

    fun hasCheat(): Boolean {
        return cheatedList.size > 2
    }


}