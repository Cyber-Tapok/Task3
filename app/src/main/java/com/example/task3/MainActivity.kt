package com.example.task3

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.task3.di.ViewModelFactory
import com.example.task3.enums.IssueState
import com.example.task3.enums.Status
import com.example.task3.model.GithubIssue
import com.example.task3.workManager.UpdateDbWorkerRequest
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


const val SELECTED_ITEM_KEY = "selectedItem"
const val SELECTED_STATE_ISSUES = "selectedState"

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    IssuesAdapter.DetailInfo {

    private lateinit var recyclerAdapter: IssuesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var issueViewModel: IssueViewModel
    private var issueListState = IssueState.ALL

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as IssueApplication).databaseComponent.inject(this)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerAdapter = IssuesAdapter(this, savedInstanceState?.getInt(SELECTED_ITEM_KEY) ?: -1)
        issueViewModel = ViewModelProviders.of(this, viewModelFactory)[IssueViewModel::class.java]
        issueListState = (savedInstanceState?.getSerializable(SELECTED_STATE_ISSUES)
            ?: IssueState.ALL) as IssueState
        recyclerView.adapter = recyclerAdapter
        UpdateDbWorkerRequest().schedule()
        swipeRefreshLayout = findViewById(R.id.swipe_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        loadIssue()
        val toolbar: MaterialToolbar = findViewById(R.id.issue_toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onRefresh() {
        issueViewModel.updatedIssues()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_ITEM_KEY, recyclerAdapter.selectedPosition)
        outState.putSerializable(SELECTED_STATE_ISSUES, issueListState)
    }

    override fun openDetails(issue: GithubIssue) {
        val fragment = IssueDetailFragment()
        val bundle = Bundle()
        bundle.putParcelable(FRAGMENT_BUNDLE_ISSUE_KEY, issue)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_view, fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.sort_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        issueListState = when (item.itemId) {
            R.id.item_all -> IssueState.ALL
            R.id.item_open -> IssueState.OPEN
            R.id.item_close -> IssueState.CLOSED
            else -> return super.onOptionsItemSelected(item)
        }
        issueViewModel.getIssues(issueListState)
        return true
    }

    private fun loadIssue() {
        issueViewModel.issues.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = true
            it?.let {
                if (recyclerAdapter.issueList.isEmpty()) {
                    recyclerAdapter.setList(it)
                } else {
                    recyclerAdapter.updateList(it)
                }
            }
            swipeRefreshLayout.isRefreshing = false
        })
        issueViewModel.updatedIssues()
        issueViewModel.internetStatus().observe(this, Observer { status ->
            val snackBarMessage = when (status) {
                Status.FAILED -> getString(R.string.internet_connection)
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
        })
        swipeRefreshLayout.isRefreshing = false
    }

    fun resetAdapterSelectPosition() {
        recyclerAdapter.resetSelectItem()
    }

}
