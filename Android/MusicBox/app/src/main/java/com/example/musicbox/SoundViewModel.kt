package com.example.musicbox

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class SoundViewModel(private val musicBox: MusicBox) : BaseObservable() {
    fun onButtonClicked() {
        sound?.let {
            musicBox.play(it)
        }
    }

    var sound: Sound? = null
        set(sound) {
            field = sound
            notifyChange()
        }

    @get:Bindable
    val title: String?
        get() = sound?.name
}