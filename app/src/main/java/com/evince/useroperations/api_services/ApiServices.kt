package com.evince.useroperations.api_services

import androidx.room.Delete
import androidx.room.Update
import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap
import retrofit2.http.Url

/*
* ApiServices interface is to declare all the api calls using retrofit
* */
interface ApiServices {

    @GET("users")
    suspend fun getUsers(@QueryMap params: HashMap<String, String>): Response<JsonElement>

    @PUT
    suspend fun updateUser(@Url url:String,@QueryMap params: HashMap<String, String>): Response<JsonElement>

    @DELETE
    suspend fun deleteUser(@Url url:String): Response<JsonElement>
}