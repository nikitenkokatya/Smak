package com.example.smak.data

data class RecetaAPI(
    override val id: Int,
    override val title: String,
    override val image: String
) : RecetaItem

interface RecetaItem {
    val id: Int
    val title: String
    val image: String
}