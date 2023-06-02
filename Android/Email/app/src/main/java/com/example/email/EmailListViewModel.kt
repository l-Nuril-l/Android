package com.example.email

import androidx.lifecycle.ViewModel
import com.example.email.database.EmailRepository

class EmailListViewModel : ViewModel() {
    private val  emailRepository = EmailRepository.get()
    val emailListLiveData =  emailRepository.getEmails()

    fun addEmail(email: Email)
    {
        emailRepository.addEmail(email)

    }

//    init {
//        for (i in 0..100)
//        {
//            val email = Email()
//            email.sender = "Sender $i"
//            email.text = "Text $i"
//            email.title = "Title $i"
//            email.isImportant = i % 2 == 0
//            emails += email
//        }
//    }
}