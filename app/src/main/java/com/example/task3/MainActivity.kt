package com.example.task3

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager
    private val fragment = IssueDetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val button = findViewById<MaterialButton>(R.id.fragment_button)
            button.setOnClickListener {
                fragmentManager.beginTransaction().addToBackStack(null).add(R.id.fragment_view, fragment)
                    .commit()
            }
        } else {
            fragmentManager.beginTransaction().addToBackStack(null).add(R.id.fragment_view, fragment)
                .commit()
        }
    }
}
