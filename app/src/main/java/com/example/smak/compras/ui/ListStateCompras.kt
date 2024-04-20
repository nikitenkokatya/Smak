package com.example.smak.compras.ui

sealed class ListStateCompras {
    data object NoData:ListStateCompras()
    data object Success:ListStateCompras()
}