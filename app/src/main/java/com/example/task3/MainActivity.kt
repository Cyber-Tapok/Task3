package com.example.task3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val fragmentManager = supportFragmentManager
    private val fragment = IssueDetailFragment()
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var issueViewModel: IssueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = RecyclerAdapter()
        recyclerView.adapter = recyclerAdapter
        issueViewModel = ViewModelProviders.of(this)[IssueViewModel::class.java]
        swipeRefreshLayout = findViewById(R.id.swipe_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary
        )
        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
            issueViewModel.getAllIssue().observe(
                this,
                Observer { issues ->
                    if (issues != null) {
                        recyclerAdapter.setList(issues)
                        swipeRefreshLayout.isRefreshing = false
                    }
                })
        }

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

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        issueViewModel.getAllIssue().observe(
            this,
            Observer { issues ->
                if (issues != null) {
                    recyclerAdapter.setList(issues)
                    swipeRefreshLayout.isRefreshing = false
                }
            })
    }
}
