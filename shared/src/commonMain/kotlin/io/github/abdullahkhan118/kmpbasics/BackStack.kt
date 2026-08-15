package io.github.abdullahkhan118.kmpbasics

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BackStack<T: Screen>(
    initialScreen: T,
) {

    val entries: StateFlow<List<T>> field = MutableStateFlow(mutableListOf(initialScreen))

    fun push(screen: T, singleTop: Boolean = false) {
        if (singleTop) {
            popUntilFirst(screen)
        } else {
            entries.value.add(screen)
        }
    }

    fun pop() {
        entries.value.removeLastOrNull()
    }

    fun popUpTo(screen: T) {
        while (entries.value.isNotEmpty() && entries.value.last()::class != screen::class) {
            entries.value.removeLastOrNull()
        }
    }

    fun popUntilFirst(screen: T) {
        val index =
            entries.value.indexOfFirst { it::class == screen::class }.takeIf { it >= 0 } ?: run {
                entries.value.add(screen)
                return
            }
        while (entries.value.isNotEmpty() && index != entries.value.size - 1) {
            entries.value.removeLastOrNull()
        }
    }


}