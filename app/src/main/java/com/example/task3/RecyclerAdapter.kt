package com.example.task3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.task3.databinding.IssueBinding
import com.example.task3.model.GithubIssue


class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.IssueViewHolder>() {

    private var issueList: List<GithubIssue> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<IssueBinding>(layoutInflater, R.layout.recyclerview_issue_item, parent, false)

        return IssueViewHolder(binding)
    }

    fun setList(issueList: List<GithubIssue>) {
        this.issueList = issueList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return issueList.size
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        holder.binding.issue = issueList[position]
    }

    class IssueViewHolder(val binding: IssueBinding) : RecyclerView.ViewHolder(binding.root)
}