package com.softwaremq.myzemogaapp.posts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.softwaremq.myzemogaapp.R
import com.softwaremq.myzemogaapp.databinding.CommentItemBinding
import com.softwaremq.myzemogaapp.databinding.HomeItemBinding
import com.softwaremq.myzemogaapp.models.CommentData
import com.softwaremq.myzemogaapp.models.PostData


class CommentsAdapter() :
    ListAdapter<CommentData, CommentsAdapter.ViewHolder?>(
        diffCallback
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.comment_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val comment = getItem(position)
        holder.binding.tvComment.text = comment.body
        "By ${comment.name}".also { holder.binding.tvUser.text = it }

    }

    inner class ViewHolder(var binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val diffCallback: DiffUtil.ItemCallback<CommentData> =
            object : DiffUtil.ItemCallback<CommentData>() {
                override fun areItemsTheSame(
                    oldItem: CommentData,
                    newItem: CommentData
                ): Boolean {
                    return oldItem == newItem
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: CommentData,
                    newItem: CommentData
                ): Boolean {
                    return oldItem.id == newItem.id
                }
            }
    }
}