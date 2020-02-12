package com.gabriel.rememberme.data.database.entities.memory

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gabriel.rememberme.data.database.entities.memory.Memory

@Dao
interface MemoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMemory(memory : Memory) : Long

    @Query("SELECT * FROM Memory")
    fun getAllMemories() : LiveData<List<Memory>>

    @Query("SELECT * FROM Memory WHERE id = :memoryId")
    fun getMemory(memoryId: Long): LiveData<Memory>

    @Query("DELETE FROM Memory WHERE id = :memoryId")
    fun removeMemory(memoryId: Long)
}