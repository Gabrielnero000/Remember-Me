package com.gabriel.rememberme.data.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gabriel.rememberme.data.database.RememberMeDatabase
import com.gabriel.rememberme.data.database.entities.memory.Memory
import com.gabriel.rememberme.data.network.RememberMeApi
import com.gabriel.rememberme.data.network.SafeApiRequest
import com.gabriel.rememberme.data.network.firebase.FirebaseSource
import com.gabriel.rememberme.data.preferences.PreferenceProvider
import com.gabriel.rememberme.util.Coroutines
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private const val MINIMUM_INTERVAL = 6

class MemoryRepository(
    private val firebase: FirebaseSource,
    private val api: RememberMeApi,
    private val db: RememberMeDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest() {

    private val memories = MutableLiveData<List<Memory>>()

    init {
        memories.observeForever {
            saveMemories(it)
        }
    }

    suspend fun addMemory(userId: String, memory: Memory) {
        return withContext(Dispatchers.IO) {
            memory.id = db.getMemoryDao().addMemory(memory)
            uploadMemory(userId, memory.id, memory)
        }
    }

    suspend fun getMemories(userId: String): LiveData<List<Memory>> {
        return withContext(Dispatchers.IO) {
            fetchMemories(userId)
            db.getMemoryDao().getAllMemories()
        }
    }


    suspend fun removeMemory(userId: String, memoryId: Long) {
        return withContext(Dispatchers.IO) {
            db.getMemoryDao().removeMemory(memoryId)
            deleteMemory(userId, memoryId)
        }
    }

    fun uploadImage(uri: Uri, listener: OnCompleteListener<Uri>) = firebase.uploadImage(uri, listener)

    private suspend fun uploadMemory(userId: String, index: Long, memory: Memory) {
        try {
            apiRequest { api.addMemory(userId, index, memory) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun fetchMemories(userId: String, force: Boolean=false) {
        val lastSavedAt = prefs.getLastSavedAt()

        if (force || lastSavedAt == null || isFetchNeeded(LocalDateTime.parse(lastSavedAt))) {
            try {
                val response = apiRequest { api.getMemories(userId) }
                memories.postValue(response.memories.filterNotNull())
            } catch (e: KotlinNullPointerException) {
                Log.println(Log.WARN, "WARNING", "Request resulted in a empty list.")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun deleteMemory(userId: String, index: Long) {
        try {
            apiRequest { api.deleteMemory(userId, index) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isFetchNeeded(savedAt: LocalDateTime): Boolean {
        return ChronoUnit.SECONDS.between(savedAt, LocalDateTime.now()) > MINIMUM_INTERVAL
    }

    private fun saveMemories(memories: List<Memory>) {
        Coroutines.io {
            for (memory in memories)
                db.getMemoryDao().addMemory(memory)
        }
    }
}