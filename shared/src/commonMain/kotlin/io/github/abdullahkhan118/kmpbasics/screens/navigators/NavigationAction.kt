package io.github.abdullahkhan118.kmpbasics.screens.navigators

import io.github.abdullahkhan118.kmpbasics.Screen

sealed class NavigationAction {

    data class NavigateTo(val destination: Screen, val singleTop: Boolean = false) : NavigationAction()
    data object Pop : NavigationAction()
    data class PopUpTo(val destination: Screen) : NavigationAction()

}