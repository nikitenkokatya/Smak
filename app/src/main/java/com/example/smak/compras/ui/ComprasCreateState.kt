package com.example.smak.compras.ui

sealed class ComprasCreateState {
    data object NombreEmptyError:ComprasCreateState()

    data class Error(val ex:Exception):ComprasCreateState()
    data class Success<T>(val data:T):ComprasCreateState()
}