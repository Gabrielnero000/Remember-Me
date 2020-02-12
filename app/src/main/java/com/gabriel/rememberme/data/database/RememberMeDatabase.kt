package com.gabriel.rememberme.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gabriel.rememberme.data.database.entities.memory.MemoryDao
import com.gabriel.rememberme.data.database.entities.memory.Memory

@Database(
    entities = [Memory::class],
    version = 1
)

abstract class RememberMeDatabase : RoomDatabase() {

    abstract fun getMemoryDao(): MemoryDao

    companion object {

        @Volatile
        private var instance: RememberMeDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK) {
            instance?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RememberMeDatabase::class.java,
                "RememberMeDatabase.db"
            ).build()
    }
}