package io.github.abdullahkhan118.kmpbasics.di

import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ksp.generated.module

expect class PlatformModule() {

    fun getPlatformName(): String

}

val platformModule = module {
    single { PlatformModule() }
}


object InitKoin {
    fun initKoin() {
        startKoin {
            modules(platformModule)
            modules(AppModule().module)
        }
    }
}