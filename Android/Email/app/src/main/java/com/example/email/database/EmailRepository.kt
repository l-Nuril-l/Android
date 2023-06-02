package com.example.email.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.Update
import com.example.email.Email
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "email-database"

class EmailRepository private constructor(context: Context) {

    private val database: EmailDatabase = Room.databaseBuilder(context.applicationContext,
        EmailDatabase::class.java,
        DATABASE_NAME).addMigrations(migration_1_2).build()
    private val emailDao = database.emailDao();
    private val executor = Executors.newSingleThreadExecutor();

    fun addEmail(email: Email)
    {
        executor.execute{
            emailDao.addEmail(email)
        }
    }

    fun updateEmail(email: Email)
    {
        executor.execute{
            emailDao.updateEmail(email)
        }
    }

    fun getEmails(): LiveData<List<Email>>
    {
        return emailDao.getEmails();
    }

    fun getEmail(uuid: UUID) : LiveData<Email?>{
        return emailDao.getEmail(uuid);
    }

    companion object{
        private var INSTANCE: EmailRepository? = null;

        fun initialize(context: Context)
        {
            if (INSTANCE == null)
                INSTANCE = EmailRepository(context);

            //INSTANCE?.addEmail(Email(title = "MyMail",sender = "Microsoft",text = "win key: 5hetr-45hhy-h3j5h-hhy35-5hh5h",isImportant = true));
            //INSTANCE?.addEmail(Email(title = "MyMail"));
            //INSTANCE?.addEmail(Email(title = "MyMail",text = "help me",isImportant = true));
            //INSTANCE?.addEmail(Email(title = "MyMail"));
        }

        fun get():EmailRepository{
            return  INSTANCE ?: throw IllegalStateException("TaskRepository must be initialized")
        }



    }

}