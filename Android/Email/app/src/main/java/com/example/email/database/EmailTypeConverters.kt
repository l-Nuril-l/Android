package com.example.email.database

import androidx.room.TypeConverter
import java.util.*

class EmailTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long?{
        return date?.time
    }
    @TypeConverter
    fun toDate(date: Long?): Date?{
        return date?.let {
            Date(it)
        }
    }
    @TypeConverter
    fun fromUuid(uuid: UUID?): String?{
        return uuid?.toString();
    }
    @TypeConverter
    fun toUuid(uuid: String?): UUID?{
        return uuid?.let {
             UUID.fromString(it)
        }
    }
}