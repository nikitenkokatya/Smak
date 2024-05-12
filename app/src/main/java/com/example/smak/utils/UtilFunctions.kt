package com.example.smak.utils

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import java.util.Arrays
import java.util.concurrent.ExecutionException


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

