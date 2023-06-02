package com.example.email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.email.database.EmailRepository
import java.util.*

class EmailDetailViewModel : ViewModel(){
    private val emailRepository = EmailRepository.get()
    private val emailIdLiveData = MutableLiveData<UUID>()

    var emailLiveData: LiveData<Email?> =
        Transformations.switchMap(emailIdLiveData) { taskId ->
            emailRepository.getEmail(taskId)
        }

    fun loadEmail(emailId : UUID)
    {
        emailIdLiveData.value = emailId;
    }

    fun saveEmail(email: Email){
        emailRepository.updateEmail(email)
    }
}