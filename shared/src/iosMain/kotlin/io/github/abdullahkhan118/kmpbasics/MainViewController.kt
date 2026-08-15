package io.github.abdullahkhan118.kmpbasics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import platform.UIKit.navigationController

fun MainViewController(): UIViewController {

    var uiController: UIViewController? = null

    uiController = ComposeUIViewController {

        val backStack by remember(uiController) {
            mutableStateOf(
                BackStack<Screen>(
                    Screen.Home
                ) {
                    uiController?.navigationController?.popViewControllerAnimated(true)
                }
            )
        }

        App(backStack)
    }



    return uiController
}