package com.example.smak.perfil.ui.usecase

sealed class CreadasListState {
    data object Error:CreadasListState()
    data object Success:CreadasListState()
}