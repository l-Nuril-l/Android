package com.example.musicbox

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class SoundViewModelTest {

    private lateinit var musicBox: MusicBox
    private lateinit var sound: Sound
    private lateinit var subject: SoundViewModel

    @Before
    fun setUp() {
        musicBox = mock(MusicBox::class.java)
        sound = Sound("assetPath")
        subject = SoundViewModel(musicBox)
        subject.sound = sound
    }

    @Test
    fun exposesSoundNameAsTitle() {
        assertThat(subject.title, `is`(sound.name))
    }

    @Test
    fun callsMusicBoxPlayOnButtonClicked() {
        subject.onButtonClicked()

        verify(musicBox).play(sound)
    }
}