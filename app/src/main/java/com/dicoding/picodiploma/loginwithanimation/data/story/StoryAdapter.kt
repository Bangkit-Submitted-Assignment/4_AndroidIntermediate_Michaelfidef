package com.dicoding.picodiploma.loginwithanimation.data.story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemUserBinding

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.UserViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val list = getItem(position)
        holder.bind(list)
//        holder.itemView.setOnClickListener {
//            val detailIntent = Intent(holder.itemView.context, DetailUserActivity::class.java)
//            detailIntent.putExtra(DetailUserActivity.EXTRA_LOGIN, list.login)
//            holder.itemView.context.startActivities(arrayOf(detailIntent))
//        }
    }

    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(binding.root.context)
                .load(story.photoUrl).into(binding.ciProfileImage)
            binding.tvProfileName.text = story.name
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}