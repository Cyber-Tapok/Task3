package com.example.task3

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.task3.databinding.IssueBinding
import com.example.task3.model.GithubIssue


class RecyclerAdapter :
    RecyclerView.Adapter<RecyclerAdapter.IssueViewHolder>() {

    var selectedPosition: Int = -1
    private lateinit var callDetailInfo: CallDetailInfo

    var issueList: List<GithubIssue> = emptyList()
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

    fun setListener(callDetailInfo: CallDetailInfo) {
        this.callDetailInfo = callDetailInfo
    }
    fun resetSelectItem(position: Int) {
        selectedPosition = -1
        notifyItemChanged(position)
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

    inner class IssueViewHolder(
        private val binding: IssueBinding,
        private val callDetailInfo: CallDetailInfo
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(position: Int) {
            itemView.setBackgroundColor(if (selectedPosition == position) Color.GREEN else Color.TRANSPARENT)
            binding.issue = issueList[position]
        }

        override fun onClick(v: View?) {
            notifyItemChanged(selectedPosition)
            selectedPosition = adapterPosition
            notifyItemChanged(selectedPosition)
            callDetailInfo.openDetails(issueList[adapterPosition])
        }
    }

    interface CallDetailInfo {
        fun openDetails(issue: GithubIssue)
    }
}