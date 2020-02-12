package com.gabriel.rememberme.ui.home.memory

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.gabriel.rememberme.data.database.entities.memory.Memory
import com.gabriel.rememberme.data.repositories.MemoryRepository
import com.gabriel.rememberme.data.repositories.UserRepository
import com.gabriel.rememberme.util.Coroutines
import com.gabriel.rememberme.util.lazyDeferred
import com.gabriel.rememberme.util.startLoginActivity


class MemoryViewModel (

    private val memoryRepository: MemoryRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun logout(context: Context) {
        userRepository.logout()
        context.startLoginActivity()
    }

    fun addMemory(text: String, imageUrl: String) {

        Coroutines.io {
            val memory = Memory(0, text, imageUrl)
            memoryRepository.addMemory(user?.uid!!, memory)
        }

    }

    fun removeMemory(memoryId: Long) {
        Coroutines.io {
            memoryRepository.removeMemory(user?.uid!!, memoryId)
        }
    }

    private val user by lazy {
        userRepository.currentUser()
    }

    val memories by lazyDeferred {
        memoryRepository.getMemories(user?.uid!!)
    }

    fun uploadImage(imageUri: Uri, uploadListener: UploadListener) = memoryRepository.uploadImage(imageUri, uploadListener)
}