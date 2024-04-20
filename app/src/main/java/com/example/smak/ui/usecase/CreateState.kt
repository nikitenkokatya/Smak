package com.example.smak.ui.usecase

sealed class CreateState {
    data object NombreEmptyError:CreateState()
    data object IngredientesEmptyError:CreateState()
    data object PasosError:CreateState()
    data object TiempoError:CreateState()
    data object ImagenesEmptyError:CreateState()
    data class Error(val ex:Exception):CreateState()
    data class Success<T>(val data:T):CreateState()
}