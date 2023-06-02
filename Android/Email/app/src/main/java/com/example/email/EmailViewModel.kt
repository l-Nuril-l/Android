package com.example.email

import android.text.format.DateFormat
import java.util.*
private const val DATE_FORMAT ="EEE, MMM, dd, HH:mm"

class EmailViewModel {
    var email: Email? = null
        set(sound) {
            field = sound
        }

    val title: String?
        get() = email?.title

    val sender: String?
        get() = email?.sender

    val text: String?
        get() = email?.text

    val receiver: String?
        get() = email?.receiver
    val date: String?
        get() = DateFormat.format(DATE_FORMAT, email?.date).toString()

    val isImportant: Boolean?
        get() = email?.isImportant
    val id: UUID?
        get() = email?.id
}