package com.example.musicbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.SeekBarBindingAdapter.setOnSeekBarChangeListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicbox.databinding.ActivityMainBinding
import com.example.musicbox.databinding.ListItemSoundBinding

class MainActivity : AppCompatActivity() {

    private lateinit var musicBox: MusicBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        musicBox = MusicBox(assets)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = SoundAdapter(musicBox.sounds)
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                musicBox.setVolume(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                //TODO("Not yet implemented")
            }

        })
    }


    override fun onDestroy() {
        super.onDestroy()
        musicBox.release()
    }


    private inner class SoundHolder(private val binding: ListItemSoundBinding)
        : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = SoundViewModel(musicBox)
        }

        fun bind(sound: Sound) {
            binding.apply {
                viewModel?.sound = sound
                executePendingBindings()
            }
        }

    }

    private inner class SoundAdapter(private val sounds: List<Sound>) : RecyclerView.Adapter<SoundHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(
                layoutInflater,
                R.layout.list_item_sound,
                parent,
                false
            )
            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            holder.bind(sound)
        }

        override fun getItemCount(): Int {
            return sounds.size
        }

    }
}


