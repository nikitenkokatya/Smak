package com.example.smak.database.resource

sealed class Resource {
    data class Error(val ex:Exception):Resource()
    data class Success<T>(val data:T):Resource()
}