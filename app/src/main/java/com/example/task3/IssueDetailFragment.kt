package com.example.task3

import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.task3.databinding.IssueBindingFragment
import com.google.android.material.appbar.MaterialToolbar
import com.squareup.picasso.Picasso


const val FRAGMENT_BUNDLE_ISSUE_KEY = "issueDetail"

class IssueDetailFragment : Fragment() {

    private var binding: IssueBindingFragment? = null

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
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.issueDetail = arguments?.getParcelable(FRAGMENT_BUNDLE_ISSUE_KEY)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar)
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.setNavigationOnClickListener {
                beginDestroyFragment()
            }

        }
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    beginDestroyFragment()
                    return true
                }
                return false
            }
        })

        val authorAvatar: ImageView = view.findViewById(R.id.author_avatar)
        Picasso.get().load(binding?.issueDetail?.user?.avatar_url)
            .resize(128, 128)
            .centerCrop()
            .placeholder(R.drawable.ic_image_placeholder)
            .into(authorAvatar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun beginDestroyFragment() {
        fragmentManager!!.beginTransaction().remove(this@IssueDetailFragment).commit()
    }
}