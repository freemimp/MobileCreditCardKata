package com.freemimp.mobilecreditcardkata.ui

import androidx.lifecycle.ViewModel
import com.freemimp.mobilecreditcardkata.domain.CardType
import com.freemimp.mobilecreditcardkata.domain.DetectCardTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CardTypeViewModel @Inject constructor(private val detectCardTypeUseCase: DetectCardTypeUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow<CardTypeState>(CardTypeState.Initial)
    val state: StateFlow<CardTypeState> = _state

    fun handleUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.OnCardNumberChange -> if (uiEvent.searchQuery.length in 0..16) {
                _state.value = CardTypeState.Success(
                    cardNumber = uiEvent.searchQuery,
                    detectCardTypeUseCase.execute(uiEvent.searchQuery)
                )
            }
        }
    }
}

sealed class UiEvent {
    data class OnCardNumberChange(val searchQuery: String) : UiEvent()
}

sealed class CardTypeState {
    data object Initial : CardTypeState()
    data class Success(val cardNumber: String, val cardType: CardType) : CardTypeState()
}
