package com.example.task3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.task3.databinding.IssueBinding
import com.example.task3.model.GithubIssue


class RecyclerAdapter(private var callDetailInfo: CallDetailInfo) :
    RecyclerView.Adapter<RecyclerAdapter.IssueViewHolder>() {

    var issueList: List<GithubIssue> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<IssueBinding>(
            layoutInflater,
            R.layout.recyclerview_issue_item,
            parent,
            false
        )

        return IssueViewHolder(binding, callDetailInfo)
    }

    fun setList(issueList: List<GithubIssue>) {
        this.issueList = issueList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return issueList.size
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class IssueViewHolder(
        private val binding: IssueBinding,
        private val callDetailInfo: CallDetailInfo
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(position: Int) {
            binding.issue = issueList[position]
        }

        override fun onClick(v: View?) {
            callDetailInfo.call(issueList[adapterPosition])
        }
    }

    interface CallDetailInfo {
        fun call(issue: GithubIssue)
    }
}