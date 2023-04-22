package com.evince.useroperations.utils

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Show toast message with short duration
fun AppCompatActivity.showShortToast(message:String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// Show toast message with long duration
fun AppCompatActivity.showLongToast(message:String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}