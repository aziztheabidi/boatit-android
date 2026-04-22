package com.boatit.boatsharing.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialState: State,
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _uiEffect = Channel<Effect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    protected val currentState: State get() = _uiState.value

    abstract fun onEvent(event: Event)

    protected fun updateState(reducer: State.() -> State) {
        _uiState.update(reducer)
    }

    protected fun emitEffect(effect: Effect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}

interface UiState

interface UiEvent

interface UiEffect
