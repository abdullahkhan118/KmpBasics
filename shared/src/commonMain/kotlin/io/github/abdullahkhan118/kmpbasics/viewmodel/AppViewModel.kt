package io.github.abdullahkhan118.kmpbasics.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppViewModel: ViewModel() {

    val state: StateFlow<String> field = MutableStateFlow("Hello")

    fun sayHello(name: String) {
        state.value = "Hello, $name!"
    }

}