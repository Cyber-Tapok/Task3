package com.example.task3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.task3.di.ViewModelFactory
import com.example.task3.model.GithubIssue
import com.example.task3.model.Status
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

const val SELECTED_ITEM_KEY = "selectItem"

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    IssuesAdapter.DetailInfo {

    private lateinit var recyclerAdapter: IssuesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var issueViewModel: IssueViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        IssueApplication.instance.databaseComponent.inject(this)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerAdapter = IssuesAdapter(this, savedInstanceState?.getInt(SELECTED_ITEM_KEY) ?: -1)
        issueViewModel = ViewModelProviders.of(this, viewModelFactory)[IssueViewModel::class.java]
        swipeRefreshLayout = findViewById(R.id.swipe_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        loadIssue()
        recyclerView.adapter = recyclerAdapter
    }


    override fun onRefresh() {
        issueViewModel.getAllIssue()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_ITEM_KEY, recyclerAdapter.selectedPosition)
    }

    private fun loadIssue() {
        swipeRefreshLayout.isRefreshing = true
        recyclerAdapter.setList(issueViewModel.getAllIssueDb().value!!)
        issueViewModel.getAllIssue().observe(
            this,
            Observer { issues ->
                if (recyclerAdapter.issueList.isEmpty()) {
                    recyclerAdapter.setList(issues)
                } else {
                    recyclerAdapter.updateList(issues)
                }
                swipeRefreshLayout.isRefreshing = false
            })
        issueViewModel.internetStatus().observe(this, Observer { status ->
            val snackBarMessage = when (status) {
                Status.FAILED -> getString(R.string.internet_connection)
                Status.SUCCESS -> getString(R.string.load_success)
                Status.LIMIT -> getString(R.string.limit_reached)
                Status.EMPTY -> getString(R.string.is_empty)
                else -> null
            }
            snackBarMessage?.let {
                Snackbar.make(
                    recyclerView,
                    snackBarMessage,
                    Snackbar.LENGTH_LONG
                ).show()
            }
            swipeRefreshLayout.isRefreshing = false
        })
    }

    override fun openDetails(issue: GithubIssue) {
        val fragment = IssueDetailFragment()
        val bundle = Bundle()
        bundle.putParcelable(FRAGMENT_BUNDLE_ISSUE_KEY, issue)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_view, fragment).commit()
    }

    fun resetAdapterSelectPosition() {
        recyclerAdapter.resetSelectItem()
    }
}
