package com.evince.useroperations.utils

import android.util.Log


// LogHelper is a helper class to print logs in debug mode
object LogHelper {
    fun systemOutPrint(log_str: String?) {
        println(log_str)
    }

    fun systemErrPrint(log_str: String?) {
        System.err.println(log_str)
    }

    fun printStackTrace(e: Exception) {
         e.printStackTrace()
    }

    fun printStackTrace(t: Throwable) {
      t.printStackTrace()
    }

    fun logD(tag: String?, log_str: String?) {
        Log.d(tag, log_str!!)
    }

    fun logE(tag: String?, log_str: String?) {
        Log.e(tag, log_str!!)
    }

    fun logI(tag: String?, log_str: String?) {
       Log.i(tag, log_str!!)
    }

    fun logV(tag: String?, log_str: String?) {
       Log.v(tag, log_str!!)
    }

    fun logW(tag: String?, log_str: String?) {
       Log.w(tag, log_str!!)
    }
}