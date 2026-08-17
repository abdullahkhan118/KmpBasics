package io.github.abdullahkhan118.kmpbasics

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class BackStack<T: Screen>(
    vararg initialScreen: T,
    private val onEmptyStack: () -> Unit
) {
    val entries: SnapshotStateList<T> field = mutableStateListOf(*initialScreen)

    fun push(screen: T, singleTop: Boolean = false) {
//        if(entries.last().toString() == screen.toString()) return

        if (singleTop) {
            popUntilFirst(screen)
        } else {
            entries.add(screen)
        }
    }

    fun pop() {
        if(entries.size == 1) {
            onEmptyStack()
            return
        }
        entries.removeLastOrNull()
    }

    fun popUpTo(screen: T) {
        while (entries.isNotEmpty() && entries.last()::class != screen::class) {
            entries.removeLastOrNull()
        }
    }

    fun popUntilFirst(screen: T) {
        val index =
            entries.indexOfFirst { it::class == screen::class }.takeIf { it >= 0 } ?: run {
                entries.add(screen)
                return
            }
        while (entries.isNotEmpty() && index != entries.size - 1) {
            entries.removeLastOrNull()
        }
    }

}