package io.github.abdullahkhan118.kmpbasics

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform