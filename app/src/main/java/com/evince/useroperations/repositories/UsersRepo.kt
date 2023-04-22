package com.evince.useroperations.repositories

import android.content.Context
import com.evince.useroperations.R
import com.evince.useroperations.api_services.ApiServices
import com.evince.useroperations.database.AppDatabase
import com.evince.useroperations.extensions.safeApiCall
import com.evince.useroperations.models.UserModel
import com.evince.useroperations.models.UsersListModel
import com.evince.useroperations.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersRepo @Inject constructor(
    @ApplicationContext val context: Context,
    private val apiServices: ApiServices,
    private val appDatabase: AppDatabase
) {


    suspend fun getUsers(page:String): Resource<UsersListModel>  = withContext(Dispatchers.IO){
        val params = HashMap<String, String>()
        params["page"] = page
        val response = context.safeApiCall(UsersListModel::class.java) {
            apiServices.getUsers(params)
        }

        appDatabase.usersDao().insertUserAll(response.data?.data.orEmpty())
        val list = appDatabase.usersDao().getAllUsers()
        response.data?.data = ArrayList(list)

        return@withContext  if (response.message == context.getString(R.string.no_internet_connection)){
            Resource.success(response.data)
        }else{
            response
        }
    }

    suspend fun updateUser(
        id:String,
        firstName:String,
        lastName:String,
        email:String,
    ): Resource<UserModel>  = withContext(Dispatchers.IO){
        val params = HashMap<String, String>()
        params["first_name"] = firstName
        params["last_name"] = lastName
        params["email"] = email
        val url = "https://reqres.in/api/users/$id"
        return@withContext context.safeApiCall(UserModel::class.java) {
            apiServices.updateUser(url,params)
        }
    }

    suspend fun deleteUser(
        id:String,
    ): Resource<UserModel>  = withContext(Dispatchers.IO){
        val url = "https://reqres.in/api/users/$id"
        return@withContext context.safeApiCall(UserModel::class.java) {
            apiServices.deleteUser(url)
        }
    }
}