package io.github.abdullahkhan118.kmpbasics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.*
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import io.github.abdullahkhan118.kmpbasics.screens.ui.SunflowerApp
import kotlinx.serialization.Serializable


/*
Each screen should define its destinations. This setup allows for screens and their destinations to grow independently.
This is based on design pattern that takes creates relation between abstract types so each can grow independently, allowing multiple combinations.
Each screen can have the following
1- Navigation and One or More Destinations/Screens. (The implementation should have navigateUp setup by default. The class should take the backStack and backPress should remove the top item. The navigate function must take a screen)
2- Optionally ViewModel. (Default Should be a NoOp viewmodel. Passed value should be class type and not an instance)
3- Optionally arguments. (Should be defined by an Args abstract class with all params being defined in a subtype's )
*/

/*
Scene strategy needs scene and scene needs entries.
Implementation of a strategy requires implementation of scene.
*/


data class SinglePaneScene<T : Any>(
    override val key: Any,
    val entry: NavEntry<T>,
    override val previousEntries: List<NavEntry<T>>,
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(entry)
    override val content: @Composable () -> Unit = { entry.Content() }
}

class SinglePaneSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T> = SinglePaneScene(
        key = entries.last().contentKey, entry = entries.last(), previousEntries = entries.dropLast(1)
    )
}

data class DoublePaneScene<T : Any>(
    override val key: Any,
    val leftEntry: NavEntry<T>,
    val rightEntry: NavEntry<T>? = null,
    override val previousEntries: List<NavEntry<T>>,
) : Scene<T> {
    override val entries: List<NavEntry<T>> = buildList {
        add(leftEntry)
        rightEntry?.let { add(rightEntry) }
    }
    override val content: @Composable () -> Unit = {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.weight(if (rightEntry == null) 1f else 0.5f)) {
                leftEntry.Content()
            }
            rightEntry?.let {
                Column(Modifier.weight(0.5f)) {
                    rightEntry.Content()
                }
            }
        }
    }
}


class DoublePaneSceneStrategy<T : Any>(val windowSizeClass: WindowSizeClass) : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {

        if (entries.size <= 1 || !windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) return null

        val rightEntry = entries.last()

        val leftEntry = entries.first()

        val key = rightEntry.contentKey.toString() + leftEntry.contentKey.toString()

        return DoublePaneScene(
            key = key,
            leftEntry = leftEntry,
            rightEntry = rightEntry,
            previousEntries = entries.dropLast(5)
        )
    }
}

@Composable
fun rememberDoublePaneSceneStrategy(): DoublePaneSceneStrategy<Any> {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass

    return remember(windowSizeClass) {
        DoublePaneSceneStrategy(windowSizeClass)
    }
}


@Serializable
sealed class Screen : NavKey {

    @Serializable
    data object Home : Screen()


    @Serializable
    data class PlantDetail(val plantId: String) : Screen() {

    }


    @Serializable
    data class Gallery(
        val plantName: String
    ) : Screen() {

    }
}


@Composable
//@Preview
fun App(
    backStack: BackStack<Screen>,
) {
    SunflowerApp(backStack)
}