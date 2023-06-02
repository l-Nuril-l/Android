package com.example.email

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import java.util.*

class MainActivity : AppCompatActivity() ,EmailListFragment.Callbacks{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null)
        {
            val fragment = EmailListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container ,fragment)
                .commit()
        }
    }

    override fun onEmailSelected(emailId: UUID) {
        var fragment = EmailFragment().newInstance(emailId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()
    }
}