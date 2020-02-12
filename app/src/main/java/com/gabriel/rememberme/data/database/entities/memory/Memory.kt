package com.gabriel.rememberme.data.database.entities.memory

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memory (
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var text: String,
    var image_url: String
)