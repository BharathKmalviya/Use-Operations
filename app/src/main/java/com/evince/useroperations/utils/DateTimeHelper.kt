package com.evince.useroperations.utils

import java.text.SimpleDateFormat

object DateTimeHelper {

    annotation class DATE_FORMAT {
        companion object {
            var DD = "dd"
            var MM = "MM"
            var YYYY = "yyyy"
            var MMM_YYYY = "MMM yyyy"
            var DD_MM_YYYY = "dd/MM/yyyy"
            var DD_MMMM_YYYY = "dd MMMM, yyyy"
            var MMMM_DD_COMMA_YYYY = "MMMM dd, yyyy"
            var DD_MMM_COMMA_YYYY = "dd MMM, yyyy"
            var MMM_DD_COMMA_YYYY = "MMM dd, yyyy"
            var DD_MMM_YYYY = "dd MMM yyyy"
            var DD_MMM = "dd MMM"
            var EEEE = "EEEEE"
            var MMMM_YYYY = "MMMM yyyy"
            var MMMM_DD_YYYY = "MMMM dd, yyyy"
            var EEE_DD_MMM_YYYY = "EEE, dd MMM yyyy"
            var EEE_DD_MMMM_YYYY = "EEE, dd MMMM yyyy"
            var EEE_DD_MMM = "EEE, dd MMM"
            var EEEE_DD_MMM = "EEEE, dd MMM"
            var EEEE_MMMM_DD = "EEEE, MMMM dd"
            var EEE_MMMM_DD_YYYY = "EEE, MMM dd yyyy"
            var YYYY_MM_DD = "yyyy-MM-dd"
            var MM_YY = "MM/YY"
            val SERVER_DATE_FORMAT = "yyyy-MM-dd"
            val LOCAL_DATE_FORMAT = "MMM dd, yyyy"
            val LOCAL_DATE_TIME_FORMAT_2 = "MMM dd, yyyy hh:mm aa"
            val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
            val YYYY_MM_DD_HH_MM_SS_2 = "yyyy-MM-dd hh:mm:ss"
            val YYYY_MM_DD_HH_MM_AA = "yyyy-MM-dd hh:mm aa"
            val LOCAL_DATE_TIME_FORMAT = "dd MMM yyyy, hh:mm aa"
            val LOCAL_DATE_NEW_TIME_FORMAT = "dd MMM yyyy\n hh:mm aa"
            val LOCAL_DATE_TIME_SECONDS_FORMAT = "dd_MMM_yyyy_hh_mm_ss_aa"
            val DD_MMM_HH_MM_AA = "dd MMM, hh:mm aa"
        }
    }

    annotation class TIME_FORMAT {
        companion object {
            var HH_MM_AA = "hh:mm aa"
            var HH_MM_SS_AA = "hh:mm:ss aa"
            var HH_MM_SS = "hh:mm:ss"
            var HH_MM = "HH:mm"
            val SERVER_TIME_FORMAT = "HH:mm"
            val LOCAL_TIME_FORMAT = "hh:mm aa"

        }
    }

    // convert date format to another format
    fun convertDateFormat(
        date: String?,
        currentFormat: String?,
        newFormat: String?,
    ): String? {
        var date = date
        try {
            var spf = SimpleDateFormat(currentFormat)
            val newDate = spf.parse(date)
            spf = SimpleDateFormat(newFormat)
            date = spf.format(newDate)
            return date
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
        return ""
    }


}