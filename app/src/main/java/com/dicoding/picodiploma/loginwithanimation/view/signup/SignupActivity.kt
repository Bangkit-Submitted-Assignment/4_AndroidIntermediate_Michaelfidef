package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.dicoding.picodiploma.loginwithanimation.view.customButton.MyEditText
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.customButton.MyEmailText
import com.dicoding.picodiploma.loginwithanimation.view.customButton.MyPassText

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    val viewModel: SignupViewModel by viewModels { ViewModelFactory.getInstance(this) }

    private lateinit var myButton: MyButton
    private lateinit var myNameEditText: MyEditText
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
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            val myName = myNameEditText.text.toString()
//            if (myName.isEmpty()) {
//                myNameEditText.error = "Nama tidak boleh kosong!"
//            } else {
//                myNameEditText.error = null
//            }
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
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X,  -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(100)
        val passText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val pass = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
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

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val name = binding.edRegisterName.text.toString()
            val pass = binding.edRegisterPassword.text.toString()

            viewModel.register(name, email, pass)

            viewModel.getRegisterResponse().observe(this) { response ->
                if (response != null) {
                    if (response.error && response.message == "Akun sudah ada") {
                        AlertDialog.Builder(this).apply {
                            setTitle("Oops!")
                            setMessage("Akun dengan email $email sudah terdaftar.")
                            setPositiveButton("OK") { _, _ ->
                            }
                            create()
                            show()
                        }
                    } else if (!response.error) {
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Akun dengan email $email berhasil dibuat. Yuk, login dan belajar coding.")
                            setPositiveButton("Lanjut") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }
}