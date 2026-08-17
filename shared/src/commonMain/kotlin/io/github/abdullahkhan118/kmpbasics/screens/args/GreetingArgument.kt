package io.github.abdullahkhan118.kmpbasics.screens.args

import kotlinx.serialization.Serializable

@Serializable
data class GreetingArgument(
    val name: String
): ScreenArgument()
