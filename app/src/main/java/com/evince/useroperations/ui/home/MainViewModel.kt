package com.evince.useroperations.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evince.useroperations.models.UserModel
import com.evince.useroperations.repositories.UsersRepo
import com.evince.useroperations.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usersRepo: UsersRepo
) : ViewModel() {

    private val _userListResponse = MutableLiveData<Resource<List<UserModel>>>()
    val userListResponse: LiveData<Resource<List<UserModel>>> = _userListResponse

    private val _userUpdateResponse = MutableLiveData<Resource<UserModel>>()
    val userUpdateResponse: LiveData<Resource<UserModel>> = _userUpdateResponse

    private val _userDeleteResponse = MutableLiveData<Resource<UserModel>>()
    val userDeleteResponse: LiveData<Resource<UserModel>> = _userDeleteResponse

    fun getUsersList(page: String) {
        viewModelScope.launch {
            _userListResponse.postValue(Resource.loading())
            val response = usersRepo.getUsers(page)
            _userListResponse.postValue(
                Resource(
                    response.status,
                    response.data?.data.orEmpty(),
                    response.message
                )
            )
        }
    }

    fun updateUser(id: String, firstName: String, lastName: String, email: String) {
        viewModelScope.launch {
            _userUpdateResponse.postValue(Resource.loading())
            val response = usersRepo.updateUser(id, firstName, lastName, email)
            _userUpdateResponse.postValue(
                Resource(
                    response.status,
                    response.data,
                    response.message
                )
            )
        }
    }

    fun deleteUser(id: String) {
        viewModelScope.launch {
            _userDeleteResponse.postValue(Resource.loading())
            val response = usersRepo.deleteUser(id)
            _userDeleteResponse.postValue(
                Resource(
                    response.status,
                    response.data,
                    response.message
                )
            )
        }
    }
}