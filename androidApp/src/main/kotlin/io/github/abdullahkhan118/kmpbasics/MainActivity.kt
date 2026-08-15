package io.github.abdullahkhan118.kmpbasics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val backStack by remember {
                mutableStateOf(
                    BackStack<Screen>(
                        Screen.Home,
                    ) {
                        finish()
                    }
                )
            }

            App(backStack)
        }
    }
}