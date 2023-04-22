package com.evince.useroperations.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersListModel(
    var `data`: ArrayList<UserModel> = ArrayList(),
    val page: Int = 0,
    @SerializedName("per_page")
    val perPage: Int = 0,
    val support: Support = Support(),
    val total: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 0
) : Parcelable {

    @Parcelize
    data class Support(
        val text: String = "",
        val url: String = ""
    ) : Parcelable
}