package com.gabriel.rememberme.ui.home.memory

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

interface UploadListener : OnCompleteListener<Uri> {
    override fun onComplete(task: Task<Uri>)
}