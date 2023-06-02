package com.example.email.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.email.Email

@Database(entities = [Email::class], version = 2)
@TypeConverters(EmailTypeConverters::class)
abstract class EmailDatabase : RoomDatabase(){
    abstract fun emailDao(): EmailDao
}

val migration_1_2 = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("alter table task add column receiver text not null default '' ")
    }

}