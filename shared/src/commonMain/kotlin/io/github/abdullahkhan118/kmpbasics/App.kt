package io.github.abdullahkhan118.kmpbasics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.*
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import io.github.abdullahkhan118.kmpbasics.di.PlatformModule
import io.github.abdullahkhan118.kmpbasics.di.platformModule
import io.github.abdullahkhan118.kmpbasics.screens.args.EmptyScreenArgument
import io.github.abdullahkhan118.kmpbasics.screens.args.GreetingArgument
import io.github.abdullahkhan118.kmpbasics.screens.args.ScreenArgument
import io.github.abdullahkhan118.kmpbasics.viewmodel.AppViewModel
import io.github.abdullahkhan118.kmpbasics.viewmodel.NoOpViewModel
import io.github.abdullahkhan118.kmpbasics.viewmodel.ScreenSpecificViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.koin.core.context.startKoin

val viewModelFactory = viewModelFactory {
    initializer {
        AppViewModel()
    }
}

/*
Each screen should define its destinations. This setup allows for screens and their destinations to grow independently.
This is based on design pattern that takes creates relation between abstract types so each can grow independently, allowing multiple combinations.
Each screen can have the following
1- Navigation and One or More Destinations/Screens. (The implementation should have navigateUp setup by default. The class should take the backStack and backPress should remove the top item. The navigate function must take a screen)
2- Optionally ViewModel. (Default Should be a NoOp viewmodel. Passed value should be class type and not an instance)
3- Optionally arguments. (Should be defined by an Args abstract class with all params being defined in a subtype's constructor)
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

        if(entries.size <= 1 || !windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) return null

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
    data class Greeting(val name: String) : Screen()
}


@Composable
//@Preview
fun App(
    backStack: BackStack<Screen>,
) {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val viewModel: AppViewModel = viewModel(factory = viewModelFactory)
        val entries = remember { backStack.entries }
        val currentEntry = remember { mutableIntStateOf(1) }
        val platformModule = koinInject<PlatformModule>()

        NavDisplay(
            backStack = entries,
            onBack = { backStack.pop() },
            sceneStrategies = listOf(
                rememberDoublePaneSceneStrategy(),
                SinglePaneSceneStrategy(),
            ),
            entryProvider = entryProvider {
                entry<Screen.Home> {
                    Column(
                        modifier = Modifier.safeContentPadding().fillMaxSize(), verticalArrangement = Arrangement.Center
                    ) {
                        Text("Home")
                        Button(onClick = {
                            backStack.push(Screen.Greeting(platformModule.getPlatformName()))
                        }) {
                            Text("Greeting")
                        }
                        Button(onClick = {
                            backStack.pop()
                        }) {
                            Text("Back")
                        }
                    }
                }
                entry<Screen.Greeting> { screen ->
                    Column(
                        modifier = Modifier.safeContentPadding().fillMaxSize(), verticalArrangement = Arrangement.Center
                    ) {
                        var greeting by remember { mutableStateOf(TextFieldState("")) }
                        Text("Hello, ${screen.name}!")
                        TextField(state = greeting)
                        Button(onClick = {
                            backStack.push(Screen.Greeting(greeting.text.toString()))
                        }) {
                            Text("Greeting Another")
                        }
                        Button(onClick = {
                            backStack.pop()
                        }) {
                            Text("Back")
                        }
                    }
                }
            })
    }
}