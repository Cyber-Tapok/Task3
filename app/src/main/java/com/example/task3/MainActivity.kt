package com.example.task3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task3.model.GithubIssue


class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager
    private val fragment = IssueDetailFragment()
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter
        var issueViewModel = ViewModelProviders.of(this)[IssueViewModel::class.java]
        issueViewModel.getAllIssue ().observe(
            this,
            Observer { issues ->
                if (issues != null) {
                    recyclerAdapter.setList(issues)
                }
            })
//        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            val button = findViewById<MaterialButton>(R.id.fragment_button)
//            button.setOnClickListener {
//                fragmentManager.beginTransaction().addToBackStack(null).add(R.id.fragment_view, fragment)
//                    .commit()
//            }
//        } else {
//            fragmentManager.beginTransaction().addToBackStack(null).add(R.id.fragment_view, fragment)
//                .commit()
//        }

    }
}
