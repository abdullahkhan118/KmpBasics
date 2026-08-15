package io.github.abdullahkhan118.kmpbasics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.*
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.github.abdullahkhan118.kmpbasics.viewmodel.AppViewModel
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource

import kmpbasics.shared.generated.resources.Res
import kmpbasics.shared.generated.resources.compose_multiplatform

val viewModelFactory = viewModelFactory {
    initializer {
        AppViewModel()
    }
}

/*
Each screen should define its destinations. This setup allows for screens and their destinations to grow independently.
This is based on design pattern that takes creates relation between abstract types so each can grow independently, allowing multiple combinations.
*/


@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen

    @Serializable
    data class Greeting(val name: String) : Screen
}


@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val viewModel: AppViewModel = viewModel(factory = viewModelFactory)
        val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }

        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeAt(backStack.lastIndex) },
            entryProvider = entryProvider {
                entry<Screen.Home> {
                    Text("Home")
                }
                entry<Screen.Greeting> { screen ->
                    Text("Hello, ${screen.name}!")
                }
            }
        )

    }
}