package com.gabriel.rememberme.ui.auth.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.gabriel.rememberme.data.repositories.UserRepository
import com.gabriel.rememberme.ui.auth.LoginActivity
import com.gabriel.rememberme.ui.auth.SignUpActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel(
    private val repository: UserRepository
): ViewModel() {

    // Email and password for the input
    var email: String? = null
    var password: String? = null
    var confirmPassword: String? = null

    // Auth listener
    var authListener: AuthListener? = null


    // Disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    val user by lazy {
        repository.currentUser()
    }

    // Function to perform login
    fun login() {

        // Validate email and password
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            authListener?.onFailure("Invalid email or password")
            return
        }

        // Authentication started
        authListener?.onStarted()

        // Calling login from repository to perform the actual authentication
        val disposable = repository.login(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Sending a success callback
                authListener?.onSuccess()
            }, {
                // Send a failure callback
                authListener?.onFailure(it.message!!)
            })

        disposables.add(disposable)
    }

    // Function to perform sign up
    fun signUp() {
        // Validate email and password
        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            authListener?.onFailure("Invalid email or password")
            return
        }

        // Password and confirm password must match
        if (!password.equals(confirmPassword)) {
            authListener?.onFailure("Invalid email or password")
            return
        }

        // Sign up started
        authListener?.onStarted()

        // Calling login from repository to perform the actual sign up
        val disposable = repository.register(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Sending a success callback
                authListener?.onSuccess()
            }, {
                // Send a failure callback
                authListener?.onFailure(it.message!!)
            })

        disposables.add(disposable)
    }

    // Launch sign up activity
    fun goToSignUp(view: View) {
        Intent(view.context, SignUpActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    // Launch login activity
    fun goToLogin(view: View) {
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    // Dispose the disposables
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
