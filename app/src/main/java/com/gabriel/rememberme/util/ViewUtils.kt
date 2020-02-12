package com.gabriel.rememberme.util

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gabriel.rememberme.ui.auth.LoginActivity
import com.gabriel.rememberme.ui.home.HomeActivity

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.startHomeActivity() =
    Intent(this, HomeActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Fragment.launchImageGetter() =
    Intent().also {
        it.type = "image/*"
        it.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                it,
                "Select image from..."
            ), Constants.GET_IMAGE_INTENT
        )
    }

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun View.toggle() {
    if (visibility == View.INVISIBLE)
        visibility = View.VISIBLE
    else if (visibility == View.VISIBLE)
        visibility = View.INVISIBLE
}
