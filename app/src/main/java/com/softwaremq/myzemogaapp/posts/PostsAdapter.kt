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
import com.softwaremq.myzemogaapp.databinding.HomeItemBinding
import com.softwaremq.myzemogaapp.models.PostData


class PostsAdapter(private val mOnClick: View.OnClickListener) :
    ListAdapter<PostData, PostsAdapter.ViewHolder?>(
        diffCallback
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.home_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val post = getItem(position)
        holder.binding.tvTitle.text = post.title
        holder.binding.tvBody.text = post.body
        holder.binding.clItem.tag = post
        if(post.favorite)
            holder.binding.ivFav.visibility = View.VISIBLE
        else
            holder.binding.ivFav.visibility = View.INVISIBLE
        holder.binding.clItem.setOnClickListener(mOnClick)

    }

    fun clearAll(){
        var count = 0
        for(item in currentList){
            currentList.remove(item)
            notifyItemRemoved(count)
            count++
        }
    }

    inner class ViewHolder(var binding: HomeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val diffCallback: DiffUtil.ItemCallback<PostData> =
            object : DiffUtil.ItemCallback<PostData>() {
                override fun areItemsTheSame(
                    oldItem: PostData,
                    newItem: PostData
                ): Boolean {
                    return oldItem == newItem
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: PostData,
                    newItem: PostData
                ): Boolean {
                    return oldItem.id == newItem.id
                }
            }
    }


}