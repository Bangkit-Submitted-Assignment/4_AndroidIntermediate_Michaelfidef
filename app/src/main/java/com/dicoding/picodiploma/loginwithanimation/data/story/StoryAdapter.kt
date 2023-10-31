package com.dicoding.picodiploma.loginwithanimation.data.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemUserBinding
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import com.dicoding.picodiploma.loginwithanimation.R

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.UserViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val list = getItem(position)
        if (list != null) {
            holder.bind(list)
        }
        holder.itemView.setOnClickListener {
            val imgPhoto: ImageView = holder.itemView.findViewById(R.id.iv_item_photo)
            val tvName: TextView = holder.itemView.findViewById(R.id.tv_item_name)
            val detailIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            if (list != null) {
                detailIntent.putExtra(DetailActivity.EXTRA_LOGIN, list.id)
            }
            holder.itemView.context.startActivities(arrayOf(detailIntent))

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(imgPhoto, "profile"),
                    Pair(tvName, "name"),
                )
            holder.itemView.context.startActivity(detailIntent, optionsCompat.toBundle())
        }
    }

    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(binding.root.context)
                .load(story.photoUrl).into(binding.ivItemPhoto)
            binding.tvItemName.text = story.name
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}