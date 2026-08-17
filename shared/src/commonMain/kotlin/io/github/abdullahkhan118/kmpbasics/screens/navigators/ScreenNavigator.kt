package io.github.abdullahkhan118.kmpbasics.screens.navigators

import io.github.abdullahkhan118.kmpbasics.BackStack
import io.github.abdullahkhan118.kmpbasics.Screen

abstract class ScreenNavigator(
    private val backStack: BackStack<Screen>,
) {

    protected fun pop() {
        backStack.pop()
    }

    protected fun popUpTo(navigationAction: NavigationAction.PopUpTo) {
        backStack.popUpTo(navigationAction.destination)
    }

    protected fun navigateTo(navigationAction: NavigationAction.NavigateTo) {
        backStack.push(navigationAction.destination, navigationAction.singleTop)
    }

    fun navigateTo(navigationAction: NavigationAction) {

        when (navigationAction) {
            NavigationAction.Pop -> pop()
            is NavigationAction.PopUpTo -> popUpTo(navigationAction)
            is NavigationAction.NavigateTo -> navigateTo(navigationAction)
        }

    }

    protected abstract fun navigateTo()

}