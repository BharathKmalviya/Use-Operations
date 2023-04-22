package com.evince.useroperations.extensions

import android.content.Context
import com.evince.useroperations.R
import com.evince.useroperations.enums.Status
import com.evince.useroperations.utils.LogHelper
import com.evince.useroperations.utils.Resource
import com.evince.useroperations.utils.isConnectedToInternet
import com.google.gson.Gson
import com.google.gson.JsonElement
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.net.ssl.SSLException

// this is to handle the API calls with exception handling
suspend fun <T> Context.safeApiCall(
    objectType: Class<T>,
    process: suspend () -> Response<*>
): Resource<T> {
    return try {
        if (isConnectedToInternet(this)) {
            process().getResponse(this, objectType)
        } else {
            Resource.error(
                this.getString(R.string.no_internet_connection),
                data = null
            )
        }
    } catch (e: Exception) {
        LogHelper.printStackTrace(e)
        return if (isConnectedToInternet(this)) {
            Resource.error(this.getString(R.string.default_error_message), data = null)
        } else {
            Resource.error(this.getString(R.string.no_internet_connection), data = null)
        }
    } catch (e: SocketTimeoutException) {
        LogHelper.printStackTrace(e)
        return if (isConnectedToInternet(this)) {
            Resource.error(this.getString(R.string.default_error_message), data = null)
        } else {
            Resource.error(this.getString(R.string.no_internet_connection), data = null)
        }

    } catch (e: SSLException) {
        LogHelper.printStackTrace(e)
        return if (isConnectedToInternet(this)) {
            Resource.error(getString(R.string.default_error_message), data = null)
        } else {
            Resource.error(this.getString(R.string.no_internet_connection), data = null)
        }
    }
}

// this is to parse the response from API with data wrapped in Resource class with status and message
fun <T> Response<*>.getResponse(context: Context, objectType: Class<T>): Resource<T> {
    if (isConnectedToInternet(context)) {
        val response = this
        return try {
            if (response.isSuccessful) {
                if (response.code() == 204) {
                    Resource.create(Status.SUCCESS, data = null, message = response.message())
                }else{
                    val formattedModel = Gson().fromJson(response.body() as JsonElement, objectType)
                    Resource.create(Status.SUCCESS, data = formattedModel, message = response.message())
                }
            } else {
                Resource.error(context.getString(R.string.default_error_message), data = null)
            }
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
            Resource.error(context.getString(R.string.default_error_message), data = null)
        }
    } else {
        return Resource.error(context.getString(R.string.no_internet_connection), data = null)
    }
}