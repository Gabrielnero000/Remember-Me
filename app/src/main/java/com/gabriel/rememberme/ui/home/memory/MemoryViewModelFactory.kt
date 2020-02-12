package com.gabriel.rememberme.ui.home.memory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gabriel.rememberme.data.repositories.MemoryRepository
import com.gabriel.rememberme.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class MemoryViewModelFactory (
    private val memoryRepository: MemoryRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemoryViewModel(
            memoryRepository,
            userRepository
        ) as T
    }
}