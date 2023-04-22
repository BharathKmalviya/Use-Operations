package com.evince.useroperations.extensions

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

// this binding adapter is to load the image from the url or any path to set with placeholder from both xml and code
@SuppressLint("CheckResult")
@BindingAdapter("app:imageUrl", "app:placeholder", "app:placeholderId", requireAll = false)
fun ImageView.loadImage(imageUrl: Any?, placeHolder: Drawable? = null, placeholderId: Int? = null) {
    post {
        if (placeHolder != null || placeholderId != null) {
            val glide = Glide.with(this.context.applicationContext).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
            if (placeholderId != null) {
                glide.placeholder(placeholderId)
                glide.error(placeholderId)
            } else if (placeHolder != null) {
                glide.placeholder(placeHolder)
                glide.error(placeHolder)
            }
            glide.into(this)
        } else {
            Glide.with(this.context.applicationContext).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
        }
    }
}