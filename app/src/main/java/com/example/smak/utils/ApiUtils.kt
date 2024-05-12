package com.example.smak.utils

import com.google.gson.Gson

class ApiUtils {
    companion object {
        fun <T> getResponseData(responseData: String?, type: Class<T>?): T? {
            val gson = Gson()
            val data = gson.fromJson(responseData, type)
            return data
        }
    }
}