package com.evince.useroperations.models


import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user", primaryKeys = ["id"])
data class UserModel(
    val avatar: String = "",
    var email: String = "",
    @SerializedName("first_name")
    var firstName: String = "",
    val id: Int = 0,
    @SerializedName("last_name")
    var lastName: String = ""
) : Parcelable