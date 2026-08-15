package io.github.abdullahkhan118.kmpbasics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.*
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.github.abdullahkhan118.kmpbasics.screens.args.EmptyScreenArgument
import io.github.abdullahkhan118.kmpbasics.screens.args.GreetingArgument
import io.github.abdullahkhan118.kmpbasics.screens.args.ScreenArgument
import io.github.abdullahkhan118.kmpbasics.viewmodel.AppViewModel
import io.github.abdullahkhan118.kmpbasics.viewmodel.NoOpViewModel
import io.github.abdullahkhan118.kmpbasics.viewmodel.ScreenSpecificViewModel
import kotlinx.serialization.Serializable

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


@Serializable
sealed class Screen : NavKey {
    @Serializable
    data object Home : Screen()

    @Serializable
    data class Greeting(val name: String) : Screen()
}


@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val viewModel: AppViewModel = viewModel(factory = viewModelFactory)
        val backStack by remember { mutableStateOf(BackStack<Screen>(Screen.Home)) }
        val entries by backStack.entries.collectAsStateWithLifecycle()


        NavDisplay(
            backStack = entries,
            onBack = { backStack.pop() },
            entryProvider = entryProvider {
                entry<Screen.Home> {
                    viewModel(AppViewModel::class)
                    Column(modifier = Modifier.safeContentPadding().fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        Text("Home")
                        Button(onClick = {
                            backStack.push(Screen.Greeting("Abdullah Khan"))
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
                    Column(modifier = Modifier.safeContentPadding().fillMaxSize(), verticalArrangement = Arrangement.Center) {
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
            }
        )
    }
}