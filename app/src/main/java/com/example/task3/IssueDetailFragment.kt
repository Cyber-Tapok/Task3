package com.example.task3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.task3.databinding.IssueBindingFragment
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
        binding?.issueDetail = arguments?.getParcelable(FRAGMENT_BUNDLE_ISSUE_KEY)
        binding?.toolbar?.setNavigationOnClickListener {
            closeFragment()
        }

        Picasso.get().load(binding?.issueDetail?.user?.avatar_url)
            .resize(128, 128)
            .noFade()
            .centerCrop()
            .placeholder(R.drawable.ic_image_placeholder)
            .into(binding!!.authorAvatar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closeFragment()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun closeFragment() {
        (activity as MainActivity?)?.resetAdapterSelectPosition()
        requireFragmentManager().beginTransaction().remove(this).commit()
    }
}