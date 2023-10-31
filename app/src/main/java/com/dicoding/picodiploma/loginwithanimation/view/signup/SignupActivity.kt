package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.view.customButton.MyButton
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.di.ResultState
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.customButton.MyEmailText
import com.dicoding.picodiploma.loginwithanimation.view.customButton.MyPassText
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    val viewModel: SignupViewModel by viewModels { ViewModelFactory.getInstance(this) }

    private lateinit var myButton: MyButton
    private lateinit var myNameEditText: MyEmailText
    private lateinit var myEmailEditText: MyEmailText
    private lateinit var myPassEditText: MyPassText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myButton = binding.signupButton
        myNameEditText = binding.edRegisterName
        myEmailEditText = binding.edRegisterEmail
        myPassEditText = binding.edRegisterPassword
        myNameEditText.addTextChangedListener(textWatcher)
        myEmailEditText.addTextChangedListener(textWatcher)
        myPassEditText.addTextChangedListener(textWatcher)
        setMyButtonEnable()

        setupView()
        setupAction()
        playAnimation()
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val myName = myNameEditText.text.toString()
            if (myName.isEmpty()) {
                myNameEditText.error = "Nama tidak boleh kosong!"
            } else {
                myNameEditText.error = null
            }
        }

        override fun afterTextChanged(s: Editable?) {
            setMyButtonEnable()
        }
    }

    private fun setMyButtonEnable() {
        val nameResult = myNameEditText.text
        val emailResult = myEmailEditText.text
        val passResult = myPassEditText.text
        myButton.isEnabled = nameResult != null && nameResult.toString().isNotEmpty() &&
                emailResult != null && emailResult.toString().isNotEmpty()
                && passResult != null && passResult.toString().isNotEmpty()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(100)
        val passText =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val pass =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val button = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(login, signup, title, desc, passText, pass, button)
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val name = binding.edRegisterName.text.toString()
            val pass = binding.edRegisterPassword.text.toString()

            viewModel.register(name, email, pass)

            viewModel.register(name, email, pass).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeah!")
                                setMessage("Anda berhasil login. Mari kita buat Story!")
                                setPositiveButton("Lanjut") { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
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
            }
        }
    }
}