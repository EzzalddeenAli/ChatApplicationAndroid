package com.coders.chatapplication.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.coders.chatapplication.R
import com.coders.chatapplication.presentation.commons.bindView
import com.coders.chatapplication.presentation.ui.register.RegisterActivity
import com.coders.chatapplication.presentation.ui.rooms.RoomsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

	private val emailInput by bindView<EditText>(R.id.input_email)
	private val passwordInput by bindView<EditText>(R.id.input_password)
	private val loginButton by bindView<Button>(R.id.btn_login)
	private val signupLink by bindView<TextView>(R.id.link_signup)

	private val viewModel: LoginViewModel by viewModel()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		loginButton.setOnClickListener {
			doLogin()
		}
		viewModel.loginSuccess.observe(this, Observer {
			startActivity(Intent(this@LoginActivity, RoomsActivity::class.java))
			finish()
		})
		viewModel.failure.observe(this, Observer {
			Toast.makeText(this, it.exception.message ?: "Error", Toast.LENGTH_SHORT).show()
		})
		signupLink.setOnClickListener {
			startActivity(Intent(this, RegisterActivity::class.java))
		}
	}

	private fun doLogin() {
		emailInput.error = null
		passwordInput.error = null
		val email = emailInput.text.toString()
		if (TextUtils.isEmpty(email)) {
			emailInput.error = "Email empty"
			return
		}
		val pass = passwordInput.text.toString()
		if (TextUtils.isEmpty(pass)) {
			passwordInput.error = "Email password"
			return
		}
		viewModel.doLogin(email, pass)
	}
}
