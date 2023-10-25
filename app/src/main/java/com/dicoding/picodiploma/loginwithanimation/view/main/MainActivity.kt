package com.dicoding.picodiploma.loginwithanimation.view.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.story.StoryAdapter
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.di.ResultState
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private val storyAdapter = StoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val recyclerView = binding.rvStory
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = storyAdapter

        viewModel.getUserStory().observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    val stories = result.data.listStory
                    setUserStories(stories)
                    viewModel.getSession()
                    showLoading(false)
                }
                is ResultState.Error -> {
                    AlertDialog.Builder(this).apply {
                        setTitle("Oops!")
                        setMessage(result.error)
                        setPositiveButton("Ok") { _, _ ->
                        }
                        show()
                    }
                    showLoading(false)
                }
            }
        }

//        val sessionLiveData = viewModel.getSession()
//        sessionLiveData.observe(this) { user ->
//            if (!user.isLogin) {
//                startActivity(Intent(this, WelcomeActivity::class.java))
//                finish()
//            } else {
//
//            }
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_story -> {

            }

            R.id.action_setting -> {

            }

            R.id.action_logout -> {
                viewModel.logout()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserStories(stories: List<ListStoryItem>) {
        storyAdapter.submitList(stories)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}