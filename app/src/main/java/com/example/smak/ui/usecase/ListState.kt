package com.example.smak.ui.usecase

sealed class ListState {
    data object Error:ListState()
    data object Success:ListState()
}