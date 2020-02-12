package com.gabriel.rememberme.data.network.responses

import com.gabriel.rememberme.data.database.entities.memory.Memory

data class MemoryResponse(
    val memories: List<Memory?>
)