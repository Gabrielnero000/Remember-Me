package com.gabriel.rememberme.ui.auth.viewmodel

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}