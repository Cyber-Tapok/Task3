package com.example.task3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.task3.database.Database
import com.example.task3.database.DummyApp
import com.example.task3.model.GithubIssue
import com.example.task3.model.GithubUser
import com.example.task3.model.Status
import com.google.android.material.snackbar.Snackbar

const val SELECTED_ITEM_KEY = "selectItem"

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    IssuesAdapter.DetailInfo {

    private lateinit var recyclerAdapter: IssuesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var issueViewModel: IssueViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerAdapter = IssuesAdapter(this, savedInstanceState?.getInt(SELECTED_ITEM_KEY) ?: -1)
        issueViewModel = ViewModelProviders.of(this)[IssueViewModel::class.java]
        swipeRefreshLayout = findViewById(R.id.swipe_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        val db: Database = DummyApp.instance.database
        var issueDao = db.issueDao
        var user = GithubUser("TEST", ")")
        var issue = GithubIssue("issue", 228,"open", user, "mmmmmmm","1","2", "3")
        issueDao.insert(issue)

//        loadIssue()
        recyclerView.adapter = recyclerAdapter
        recyclerAdapter.setList(issueDao.getAllIssue())
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
        issueViewModel.getAllIssue().observe(
            this,
            Observer { issues ->
                if (recyclerAdapter.issueList.isEmpty()) {
                    recyclerAdapter.setList(issues)
                } else {
                    recyclerAdapter.updateList(issues)
                }
                if (recyclerAdapter.issueList.isEmpty()) {
                    Snackbar.make(
                        recyclerView,
                        getString(R.string.is_empty),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                swipeRefreshLayout.isRefreshing = false
            })
        issueViewModel.internetStatus().observe(this, Observer { status ->
            val snackBarMessage = when (status) {
                Status.FAILED -> getString(R.string.internet_connection)
                Status.SUCCESS -> getString(R.string.load_success)
                Status.LIMIT -> getString(R.string.limit_reached)
                else -> null
            }
            snackBarMessage?. let {
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
