package com.gabriel.rememberme.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.gabriel.rememberme.R
import com.gabriel.rememberme.databinding.ActivityLoginBinding
import com.gabriel.rememberme.ui.auth.viewmodel.AuthListener
import com.gabriel.rememberme.ui.auth.viewmodel.AuthViewModel
import com.gabriel.rememberme.ui.auth.viewmodel.AuthViewModelFactory
import com.gabriel.rememberme.util.hide
import com.gabriel.rememberme.util.show
import com.gabriel.rememberme.util.startHomeActivity
import com.gabriel.rememberme.util.toast
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()

    private val factory : AuthViewModelFactory by instance()

    private lateinit var viewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.authListener = this
    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess() {
        progress_bar.hide()
        startHomeActivity()
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        toast(message)
    }

    override fun onStart() {
        super.onStart()
        viewModel.user?.let {
            startHomeActivity()
        }
    }
}
