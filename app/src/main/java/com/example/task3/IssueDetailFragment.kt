package com.example.task3

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar

class IssueDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_issue_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.title = "Работает"
            toolbar.setNavigationOnClickListener { activity!!.onBackPressed() }
        }
    }
}