package com.gabriel.rememberme.data.network

import com.gabriel.rememberme.data.database.entities.memory.Memory
import com.gabriel.rememberme.data.network.responses.MemoryResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RememberMeApi {

    @GET("users/{userId}.json")
    suspend fun getMemories(@Path("userId") userId: String) : Response<MemoryResponse>

    @PUT("users/{userId}/memories/{index}.json")
    suspend fun addMemory(@Path("userId") userId: String, @Path("index") index: Long, @Body memory: Memory) : Response<ResponseBody>

    @DELETE("users/{userId}/memories/{index}.json")
    suspend fun deleteMemory(@Path("userId") userId: String, @Path("index") index: Long) : Response<ResponseBody>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : RememberMeApi {

            val okkHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .client(okkHttpClient)
                .baseUrl("https://rememberme-67b80.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RememberMeApi::class.java)
        }
    }
}