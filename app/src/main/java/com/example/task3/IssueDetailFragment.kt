package com.example.task3

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.task3.databinding.IssueBindingFragment
import com.example.task3.model.GithubIssue
import com.google.android.material.appbar.MaterialToolbar
import com.squareup.picasso.Picasso

class IssueDetailFragment : Fragment() {

    private lateinit var binding: IssueBindingFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_issue_detail,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.issueDetail = arguments?.getParcelable("TEST")
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.title = binding.issueDetail?.title
            toolbar.setNavigationOnClickListener { activity!!.onBackPressed() }
        }
        val authorAvatar: ImageView = view.findViewById(R.id.author_avatar)
        Picasso.get().load(binding.issueDetail?.user?.avatar_url).centerCrop().placeholder(R.drawable.ic_image_placeholder).resize(128, 128).into(authorAvatar)
    }
}