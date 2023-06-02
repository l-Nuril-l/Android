package com.example.email.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.email.Email
import java.util.*

@Dao
interface EmailDao {

    @Query("select * from email")
    fun getEmails(): LiveData<List<Email>>

    @Query("select * from email where id = (:id)")
    fun getEmail(id: UUID): LiveData<Email?>

    @Insert
    fun addEmail(email: Email)

    @Update
    fun updateEmail(email: Email)
}