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
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker
import com.treebo.internetavailabilitychecker.InternetConnectivityListener


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    RecyclerAdapter.CallDetailInfo, InternetConnectivityListener {

    private val fragmentManager = supportFragmentManager
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var issueViewModel: IssueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        issueViewModel = ViewModelProviders.of(this)[IssueViewModel::class.java]
        swipeRefreshLayout = findViewById(R.id.swipe_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary
        )
        recyclerAdapter = issueViewModel.adapter
        recyclerView.adapter = recyclerAdapter
        recyclerAdapter.setListener(this)
        InternetAvailabilityChecker.init(this)
        val mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance()
        mInternetAvailabilityChecker.addInternetConnectivityListener(this)
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

    override fun openDetails(issue: GithubIssue) {
        val fragment = IssueDetailFragment()
        val bundle = Bundle()
        bundle.putParcelable(FRAGMENT_BUNDLE_ISSUE_KEY, issue)
        fragment.arguments = bundle
        fragmentManager.beginTransaction().replace(R.id.fragment_view, fragment).commit()
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        if (isConnected) {
            swipeRefreshLayout.post {
                loadIssue()
            }
        } else {
            Snackbar.make(
                recyclerView,
                getString(R.string.internet_connection),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    fun resetAdapterSelectPosition() {
        recyclerAdapter.resetSelectItem(recyclerAdapter.selectedPosition)
    }

}
