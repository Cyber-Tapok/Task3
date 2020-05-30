package com.example.task3

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.task3.databinding.IssueBinding
import com.example.task3.model.GithubIssue
import com.example.task3.utils.IssueDiffUtilCallback


class IssuesAdapter(private val detailInfo: DetailInfo, var selectedPosition: Int) :
    RecyclerView.Adapter<IssuesAdapter.IssueViewHolder>() {

    var issueList: List<GithubIssue> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<IssueBinding>(
            layoutInflater,
            R.layout.recyclerview_issue_item,
            parent,
            false
        )
        return IssueViewHolder(binding, detailInfo)
    }

    fun setList(issueList: List<GithubIssue>) {
        this.issueList = issueList
        notifyDataSetChanged()
    }

    fun updateList(issueList: List<GithubIssue>) {
        val issueDiffUtilCallBack =
            IssueDiffUtilCallback(
                this.issueList,
                issueList
            )
        val issueDiffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(issueDiffUtilCallBack, false)
        this.issueList = issueList
        issueDiffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return issueList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        holder.bind(position)
    }

    fun resetSelectItem() {
        val position = selectedPosition
        selectedPosition = -1
        notifyItemChanged(position)
    }

    inner class IssueViewHolder(
        private val binding: IssueBinding,
        private val detailInfo: DetailInfo
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(position: Int) {
            if (itemView.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                itemView.isSelected = selectedPosition == position
            }
            binding.issue = issueList[position]
        }

        override fun onClick(v: View?) {
            notifyItemChanged(selectedPosition)
            selectedPosition = adapterPosition
            notifyItemChanged(selectedPosition)
            detailInfo.openDetails(issueList[adapterPosition])
        }
    }

    interface DetailInfo {
        fun openDetails(issue: GithubIssue)
    }
}