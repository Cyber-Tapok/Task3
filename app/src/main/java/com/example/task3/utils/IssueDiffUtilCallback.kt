package com.example.task3.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.task3.model.GithubIssue

class IssueDiffUtilCallback(
    private val oldList: List<GithubIssue>,
    private val newList: List<GithubIssue>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].number == newList[newItemPosition].number
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}