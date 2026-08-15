package io.github.abdullahkhan118.kmpbasics

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}