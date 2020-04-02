package com.example.task3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.task3.model.GithubIssue
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    RecyclerAdapter.CallDetailInfo {

    private val fragmentManager = supportFragmentManager
    private var recyclerAdapter: RecyclerAdapter = RecyclerAdapter(this)
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var issueViewModel: IssueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        issueViewModel = ViewModelProviders.of(this)[IssueViewModel::class.java]
        swipeRefreshLayout = findViewById(R.id.swipe_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary
        )
        swipeRefreshLayout.post {
            loadIssue()
        }
        recyclerAdapter.setHasStableIds(true)
        recyclerView.adapter = recyclerAdapter
    }

    override fun onRefresh() {
        issueViewModel.getAllIssue()
    }


    private fun loadIssue() {
        swipeRefreshLayout.isRefreshing = true
        issueViewModel.getAllIssue().observe(
            this,
            Observer { issues ->
                if (issues != null) {
                    recyclerAdapter.setList(issues)
                    if (recyclerAdapter.issueList.isEmpty()) {
                        Snackbar.make(
                            recyclerView,
                            getString(R.string.is_empty),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
                swipeRefreshLayout.isRefreshing = false
            })
    }

    override fun call(issue: GithubIssue) {
        val fragment = IssueDetailFragment()
        fragmentManager.beginTransaction().detach(fragment).commit()
        fragmentManager.beginTransaction().replace(R.id.fragment_view, fragment).commit()
        fragmentManager.beginTransaction().attach(fragment).commit()
        val bundle = Bundle()
        bundle.putParcelable(FRAGMENT_BUNDLE_ISSUE_KEY, issue)
        fragment.arguments = bundle
    }
}
