package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.response.Story
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.di.ResultState
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_LOGIN) ?: ""
        viewModel.getDetailUser(id).observe(this) { detail ->
            if (detail != null) {
                when (detail) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        val story = detail.data.story
                        setDetailUser(story)
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        AlertDialog.Builder(this).apply {
                            setTitle("Oops!")
                            setMessage(detail.error)
                            setPositiveButton("Ok") { _, _ ->
                            }
                            show()
                        }
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun setDetailUser(story: Story) {
        binding.tvProfileDetail.text = story.name
        binding.tvDesciptionDetail.text = story.description
        Glide.with(binding.root.context)
            .load(story.photoUrl)
            .into(binding.ivStoryDetail)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val EXTRA_LOGIN = "extra_login"
    }
}