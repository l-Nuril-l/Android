package com.example.email

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Email (@PrimaryKey val id: UUID = UUID.randomUUID(),
                  var sender: String ="",
                  var title: String ="",
                  var text: String ="",
                  var isImportant: Boolean = false,
                  var date: Date = Date(),
                  var receiver: String ="")